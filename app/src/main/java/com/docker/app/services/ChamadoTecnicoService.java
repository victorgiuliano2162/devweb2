package com.docker.app.services;

import com.docker.app.entities.ChamadoTecnico;
import com.docker.app.entities.Funcionario;
import com.docker.app.entities.enums.TipoChamado;
import com.docker.app.repositories.ChamadoTecnicoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ChamadoTecnicoService {

    @Autowired
    private ChamadoTecnicoRepository chamadoTecnicoRepository;
    @Autowired
    private FuncionarioService funcionarioService;

    public ChamadoTecnico criarChamadoTecnico(ChamadoTecnico chamadoTecnico) {
        return chamadoTecnicoRepository.save(chamadoTecnico);
    }

    public Page<ChamadoTecnico> findBySetor(String setor, Pageable pageable) {
        return chamadoTecnicoRepository.findBySetor_Nome(setor.toUpperCase(), pageable);
    }

    public Page<ChamadoTecnico> findByResponsavelPelaAbertura(String funcionarioId, Pageable pageable) {
        Funcionario funcionario = funcionarioService.getFuncionarioPorId(funcionarioId);
        return chamadoTecnicoRepository.findByResponsavelPelaAbertura(funcionario, pageable);
    }

    public Page<ChamadoTecnico> findByResponsavelPelaExecucao(String funcionarioId, Pageable pageable) {
        Funcionario funcionario = funcionarioService.getFuncionarioPorId(funcionarioId);
        return chamadoTecnicoRepository.findByResponsavelPelaExecucao(funcionario, pageable);
    }

    public Page<ChamadoTecnico> findByTipoChamado(TipoChamado tipoChamado, Pageable pageable) {

        return chamadoTecnicoRepository.findByTipoChamado(tipoChamado, pageable);
    }

    public Page<ChamadoTecnico> findByAtivoTrue(String funcionarioId, Pageable pageable) {
        Funcionario funcionario = funcionarioService.getFuncionarioPorId(funcionarioId);
        return chamadoTecnicoRepository.findByAtivoTrueAndByResponsavelPelaExecucao(funcionario, pageable);
    }

    public Page<ChamadoTecnico> findByAtivoFalse(Pageable pageable) {
        return chamadoTecnicoRepository.findByAtivoFalse(pageable);
    }

    public Page<ChamadoTecnico> findByDataCriacaoBetween(LocalDate inicio, LocalDate fim, Pageable pageable) {
        return chamadoTecnicoRepository.findByDataCriacaoBetween(inicio, fim, pageable);
    }

    public Page<ChamadoTecnico> findAllByDataCriacaoBetweenAndAtivoTrue(LocalDate start, LocalDate end, Pageable pageable) {
        return chamadoTecnicoRepository.findAllByDataCriacaoBetweenAndAtivoTrue(start, end, pageable);
    }

    public Page<ChamadoTecnico> findAllByDataCriacaoGreaterThanEqual(LocalDate startInclusive, Pageable pageable) {
        return chamadoTecnicoRepository.findAllByDataCriacaoGreaterThanEqual(startInclusive, pageable);
    }

    public Page<ChamadoTecnico> findResponsavelPelaAberturaTrue(String funcionarioId, Pageable pageable) {
        Funcionario funcionario = funcionarioService.getFuncionarioPorId(funcionarioId);
        return chamadoTecnicoRepository.findByAtivoTrueAndByResponsavelPelaAbertura(funcionario, pageable);
    }

    public Page<ChamadoTecnico> findResponsavelPelaAberturaTrue(LocalDate date, String setor, Pageable pageable) {
        return chamadoTecnicoRepository.findByDataCriacaoAndSetor_Nome(date, setor, pageable);
    }

    public Page<ChamadoTecnico> findResponsavelPelaAberturaPorData(LocalDate date, String funcionarioId, Pageable pageable) {
        Funcionario funcionario = funcionarioService.getFuncionarioPorId(funcionarioId);
        return chamadoTecnicoRepository.findByDataCriacaoAndResponsavelPelaExecucao(date, funcionario, pageable);
    }
}
