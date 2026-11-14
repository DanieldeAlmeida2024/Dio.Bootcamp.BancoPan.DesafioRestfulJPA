package me.dio.academia.digital.service.impl;

import me.dio.academia.digital.entity.Aluno;
import me.dio.academia.digital.entity.AvaliacaoFisica;
import me.dio.academia.digital.entity.form.AlunoForm;
import me.dio.academia.digital.entity.form.AlunoUpdateForm;
import me.dio.academia.digital.infra.utils.JavaTimeUtils;
import me.dio.academia.digital.repository.AlunoRepository;
import me.dio.academia.digital.repository.MatriculaRepository;
import me.dio.academia.digital.service.IAlunoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AlunoServiceImpl implements IAlunoService {

    @Autowired
    private AlunoRepository repository;

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Override
    public Aluno create(AlunoForm form) {
        if (form == null || form.getCpf() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF obrigatório");
        }
        // Formulário é validado via @CPF no controller. O banco usa sempre 11 dígitos.
        String cpf = form.getCpf().trim().replace(".", "").replace("-", "");
        if (!cpf.matches("[0-9]{11}")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF inválido");
        }
        if (repository.existsByCpf(cpf)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado");
        }
        Aluno aluno = new Aluno();
        aluno.setNome(form.getNome());
        aluno.setCpf(cpf);
        aluno.setBairro(form.getBairro());
        aluno.setDataDeNascimento(form.getDataDeNascimento());
        try {
            return repository.saveAndFlush(aluno);
        } catch (DataIntegrityViolationException erro) {
            // Garante conflito mesmo se outro cadastro vencer a corrida entre consulta e INSERT.
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Já existe um aluno com os identificadores informados", erro);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Aluno get(Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID do aluno inválido");
        }
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Aluno> getAll(String dataDeNascimento) {
        if (dataDeNascimento == null || dataDeNascimento.isBlank()) {
            return repository.findAll();
        }
        try {
            LocalDate data = LocalDate.parse(dataDeNascimento, JavaTimeUtils.LOCAL_DATE_FORMATTER);
            return repository.findByDataDeNascimento(data);
        } catch (DateTimeParseException erro) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Data de nascimento inválida", erro);
        }
    }

    @Override
    public Aluno update(Long id, AlunoUpdateForm formUpdate) {
        Aluno aluno = get(id);
        if (formUpdate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados de atualização obrigatórios");
        }
        if (formUpdate.getNome() != null) {
            String nome = formUpdate.getNome().trim();
            if (nome.length() < 3 || nome.length() > 50) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome inválido");
            }
            aluno.setNome(nome);
        }
        if (formUpdate.getBairro() != null) {
            String bairro = formUpdate.getBairro().trim();
            if (bairro.length() < 3 || bairro.length() > 50) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bairro inválido");
            }
            aluno.setBairro(bairro);
        }
        if (formUpdate.getDataDeNascimento() != null) {
            if (!formUpdate.getDataDeNascimento().isBefore(LocalDate.now())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data deve ser anterior a hoje");
            }
            aluno.setDataDeNascimento(formUpdate.getDataDeNascimento());
        }
        return repository.save(aluno);
    }

    @Override
    public void delete(Long id) {
        get(id);
        // Primeiro excluir matrículas associadas: a matrícula não pode apagar aluno
        // por cascade, nem pode permanecer referenciando a linha excluída.
        matriculaRepository.deleteByAluno_Id(id);
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvaliacaoFisica> getAllAvaliacaoFisicaId(Long id) {
        return new ArrayList<>(get(id).getAvaliacoes());
    }
}
