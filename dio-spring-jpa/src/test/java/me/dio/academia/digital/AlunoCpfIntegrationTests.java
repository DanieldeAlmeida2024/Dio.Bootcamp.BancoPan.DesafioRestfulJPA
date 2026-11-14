package me.dio.academia.digital;

import me.dio.academia.digital.entity.Aluno;
import me.dio.academia.digital.entity.form.AlunoForm;
import me.dio.academia.digital.service.impl.AlunoServiceImpl;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import me.dio.academia.digital.repository.AlunoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AlunoCpfIntegrationTests {
    @Autowired MockMvc mvc;
    @Autowired AlunoRepository alunos;
    @Autowired AlunoServiceImpl alunoService;

    private String alunoJson(String cpf) {
        return "{\"nome\":\"Pessoa Exemplo\",\"cpf\":\"" + cpf +
                "\",\"bairro\":\"Centro\",\"dataDeNascimento\":\"15/01/1999\"}";
    }

    @Test
    void cpfComPontuacaoEhNormalizadoEOcultadoDoJson() throws Exception {
        mvc.perform(post("/alunos").contentType(MediaType.APPLICATION_JSON)
                    .content(alunoJson("529.982.247-25")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.cpf").doesNotExist());

        assertTrue(alunos.existsByCpf("52998224725"));
        Aluno aluno = alunos.findAll().stream()
                .filter(a -> "52998224725".equals(a.getCpf()))
                .findFirst().orElseThrow();
        mvc.perform(get("/alunos/{id}", aluno.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").doesNotExist());
    }

    @Test
    void cpfDuplicadoComFormatoDiferenteRetorna409() throws Exception {
        mvc.perform(post("/alunos").contentType(MediaType.APPLICATION_JSON)
                    .content(alunoJson("529.982.247-25")))
                .andExpect(status().isCreated());
        mvc.perform(post("/alunos").contentType(MediaType.APPLICATION_JSON)
                    .content(alunoJson("52998224725")))
                .andExpect(status().isConflict());
        assertEquals(1, alunos.findAll().stream()
                .filter(a -> "52998224725".equals(a.getCpf())).count());
    }

    @Test
    void cpfComDigitosInvalidosNaoEhPersistido() throws Exception {
        mvc.perform(post("/alunos").contentType(MediaType.APPLICATION_JSON)
                    .content(alunoJson("111.111.111-11")))
                .andExpect(status().isBadRequest());
        assertFalse(alunos.existsByCpf("11111111111"));
    }

    @Test
    void cpfAusenteNaoEhPersistido() throws Exception {
        mvc.perform(post("/alunos").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Pessoa Exemplo\",\"bairro\":\"Centro\",\"dataDeNascimento\":\"1999-01-15\"}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void servicoRejeitaCpfComDigitosVerificadoresInvalidosSemPassarPeloController() {
        AlunoForm form = new AlunoForm();
        form.setNome("Pessoa de Teste");
        form.setBairro("Centro");
        form.setDataDeNascimento(LocalDate.of(1995, 1, 1));

        for (String cpf : new String[] {"11111111111", "52998224726", "00000000000"}) {
            form.setCpf(cpf);
            ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                    () -> alunoService.create(form));
            assertEquals(400, erro.getRawStatusCode());
            assertFalse(alunos.existsByCpf(cpf));
        }
    }

    @Test
    void servicoAceitaCpfValidoEmChamadaDireta() {
        AlunoForm form = new AlunoForm();
        form.setNome("Pessoa de Teste");
        form.setBairro("Centro");
        form.setDataDeNascimento(LocalDate.of(1995, 1, 1));
        form.setCpf("529.982.247-25");
        Aluno salvo = alunoService.create(form);
        assertEquals("52998224725", salvo.getCpf());
        assertTrue(alunos.existsByCpf("52998224725"));
    }

}
