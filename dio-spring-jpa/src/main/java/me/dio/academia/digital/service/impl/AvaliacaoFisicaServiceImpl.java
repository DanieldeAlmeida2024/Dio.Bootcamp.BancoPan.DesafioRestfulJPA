package me.dio.academia.digital.service.impl;

import me.dio.academia.digital.entity.Aluno;
import me.dio.academia.digital.entity.AvaliacaoFisica;
import me.dio.academia.digital.entity.form.AvaliacaoFisicaForm;
import me.dio.academia.digital.entity.form.AvaliacaoFisicaUpdateForm;
import me.dio.academia.digital.repository.AlunoRepository;
import me.dio.academia.digital.repository.AvaliacaoFisicaRepository;
import me.dio.academia.digital.service.IAvaliacaoFisicaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class AvaliacaoFisicaServiceImpl implements IAvaliacaoFisicaService {

    @Autowired
    private AvaliacaoFisicaRepository avaliacaoFisicaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Override
    public AvaliacaoFisica create(AvaliacaoFisicaForm form) {
        if (form == null || form.getAlunoId() == null || form.getAlunoId() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID do aluno inválido");
        }
        validarMedidas(form.getPeso(), form.getAltura());
        Aluno aluno = alunoRepository.findById(form.getAlunoId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado"));
        AvaliacaoFisica avaliacao = new AvaliacaoFisica();
        avaliacao.setAluno(aluno);
        avaliacao.setPeso(form.getPeso());
        avaliacao.setAltura(form.getAltura());
        return avaliacaoFisicaRepository.save(avaliacao);
    }

    @Override
    @Transactional(readOnly = true)
    public AvaliacaoFisica get(Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID da avaliação inválido");
        }
        return avaliacaoFisicaRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Avaliação não encontrada"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvaliacaoFisica> getAll() {
        return avaliacaoFisicaRepository.findAll();
    }

    @Override
    public AvaliacaoFisica update(Long id, AvaliacaoFisicaUpdateForm formUpdate) {
        AvaliacaoFisica avaliacao = get(id);
        if (formUpdate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados da avaliação obrigatórios");
        }
        validarMedidas(formUpdate.getPeso(), formUpdate.getAltura());
        avaliacao.setPeso(formUpdate.getPeso());
        avaliacao.setAltura(formUpdate.getAltura());
        return avaliacaoFisicaRepository.save(avaliacao);
    }

    @Override
    public void delete(Long id) {
        get(id);
        avaliacaoFisicaRepository.deleteById(id);
    }

    private void validarMedidas(double peso, double altura) {
        if (!Double.isFinite(peso) || !Double.isFinite(altura) ||
            peso <= 0 || altura <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Peso e altura devem ser números positivos e finitos");
        }
    }
}
