package me.dio.academia.digital.service.impl;

import me.dio.academia.digital.entity.Aluno;
import me.dio.academia.digital.entity.Matricula;
import me.dio.academia.digital.entity.form.MatriculaForm;
import me.dio.academia.digital.repository.AlunoRepository;
import me.dio.academia.digital.repository.MatriculaRepository;
import me.dio.academia.digital.service.IMatriculaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class MatriculaServiceImpl implements IMatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Override
    public Matricula create(MatriculaForm form) {
        if (form == null || form.getAlunoId() == null || form.getAlunoId() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID do aluno inválido");
        }
        Aluno aluno = alunoRepository.findById(form.getAlunoId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado"));
        if (matriculaRepository.existsByAluno_Id(aluno.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Aluno já matriculado");
        }
        Matricula matricula = new Matricula();
        matricula.setAluno(aluno);
        return matriculaRepository.save(matricula);
    }

    @Override
    @Transactional(readOnly = true)
    public Matricula get(Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID da matrícula inválido");
        }
        return matriculaRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Matrícula não encontrada"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Matricula> getAll(String bairro) {
        if (bairro == null || bairro.isBlank()) {
            return matriculaRepository.findAll();
        }
        return matriculaRepository.findAlunosMatriculadosBairro(bairro.trim());
    }

    @Override
    public void delete(Long id) {
        get(id);
        matriculaRepository.deleteById(id);
    }
}
