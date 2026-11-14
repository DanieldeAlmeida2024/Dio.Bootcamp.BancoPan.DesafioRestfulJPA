package me.dio.academia.digital;

import me.dio.academia.digital.entity.Aluno;
import me.dio.academia.digital.repository.AlunoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class AcademiaDigitalApplicationTests {
    @Autowired
    private AlunoRepository alunoRepository;

    @Test
    void contextLoads() {
    }

    @Test
    @Transactional
    void salvaEConsultaAlunoEmBancoIsolado() {
        Aluno aluno = new Aluno();
        aluno.setNome("Pessoa de Teste");
        aluno.setCpf(UUID.randomUUID().toString());
        aluno.setBairro("Centro");
        aluno.setDataDeNascimento(LocalDate.of(2000, 2, 1));

        Aluno salvo = alunoRepository.saveAndFlush(aluno);
        assertNotNull(salvo.getId());
        Aluno consultado = alunoRepository.findById(salvo.getId()).orElseThrow();
        assertEquals("Pessoa de Teste", consultado.getNome());
        assertEquals(LocalDate.of(2000, 2, 1), consultado.getDataDeNascimento());
    }
}
