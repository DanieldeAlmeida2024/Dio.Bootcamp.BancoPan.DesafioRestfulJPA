package me.dio.academia.digital;

import me.dio.academia.digital.entity.Aluno;
import me.dio.academia.digital.entity.Matricula;
import me.dio.academia.digital.repository.AlunoRepository;
import me.dio.academia.digital.repository.MatriculaRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AcademiaCrudIntegrationTests {

    @Autowired private MockMvc mvc;
    @Autowired private AlunoRepository alunos;
    @Autowired private MatriculaRepository matriculas;

    private Aluno inserirAluno() {
        Aluno aluno = new Aluno();
        aluno.setNome("Maria de Teste");
        aluno.setBairro("Centro");
        aluno.setCpf(UUID.randomUUID().toString());
        aluno.setDataDeNascimento(LocalDate.of(2001, 4, 2));
        return alunos.saveAndFlush(aluno);
    }

    @Test
    void consultarAlunoNaoVazaCpf() throws Exception {
        Aluno aluno = inserirAluno();
        mvc.perform(get("/alunos/{id}", aluno.getId()))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.nome").value("Maria de Teste"))
           .andExpect(jsonPath("$.cpf").doesNotExist());
    }

    @Test
    void atualizarAlunoPreservaIdentidadeEOutrosCampos() throws Exception {
        Aluno aluno = inserirAluno();
        mvc.perform(put("/alunos/{id}", aluno.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Nome Atualizado\",\"bairro\":\"Bairro Novo\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(aluno.getId()))
            .andExpect(jsonPath("$.nome").value("Nome Atualizado"));

        Aluno atualizado = alunos.findById(aluno.getId()).orElseThrow();
        assertEquals("Bairro Novo", atualizado.getBairro());
        assertEquals(LocalDate.of(2001, 4, 2), atualizado.getDataDeNascimento());
    }

    @Test
    void enderecoInexistenteNaoRetorna500EmConsulta() throws Exception {
        mvc.perform(get("/alunos/{id}", 999999L))
           .andExpect(status().isNotFound());
        mvc.perform(delete("/matriculas/{id}", 999999L))
           .andExpect(status().isNotFound());
    }

    @Test
    void excluirMatriculaNaoExcluiAluno() throws Exception {
        Aluno aluno = inserirAluno();
        mvc.perform(post("/matriculas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"alunoId\":" + aluno.getId() + "}"))
           .andExpect(status().isCreated());
        Matricula matricula = matriculas.findAll().stream()
                .filter(m -> m.getAluno().getId().equals(aluno.getId()))
                .findFirst().orElseThrow();

        mvc.perform(delete("/matriculas/{id}", matricula.getId()))
           .andExpect(status().isNoContent());

        assertTrue(alunos.findById(aluno.getId()).isPresent(),
                "Excluir matrícula jamais deve excluir o aluno");
        assertFalse(matriculas.findById(matricula.getId()).isPresent());
    }

    @Test
    void rejeitarSegundaMatriculaDoMesmoAluno() throws Exception {
        Aluno aluno = inserirAluno();
        String body = "{\"alunoId\":" + aluno.getId() + "}";
        mvc.perform(post("/matriculas").contentType(MediaType.APPLICATION_JSON).content(body))
           .andExpect(status().isCreated());
        mvc.perform(post("/matriculas").contentType(MediaType.APPLICATION_JSON).content(body))
           .andExpect(status().isConflict());
    }

    @Test
    void avaliacaoInvalidaRetornaBadRequest() throws Exception {
        Aluno aluno = inserirAluno();
        mvc.perform(post("/avaliacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"alunoId\":" + aluno.getId() + ",\"peso\":0,\"altura\":1.80}"))
           .andExpect(status().isBadRequest());
    }

    @Test
    void excluirAlunoLimpaMatriculaAssociada() throws Exception {
        Aluno aluno = inserirAluno();
        mvc.perform(post("/matriculas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"alunoId\":" + aluno.getId() + "}"))
            .andExpect(status().isCreated());

        mvc.perform(delete("/alunos/{id}", aluno.getId()))
           .andExpect(status().isNoContent());

        assertFalse(alunos.findById(aluno.getId()).isPresent());
        assertFalse(matriculas.existsByAluno_Id(aluno.getId()));
    }
}
