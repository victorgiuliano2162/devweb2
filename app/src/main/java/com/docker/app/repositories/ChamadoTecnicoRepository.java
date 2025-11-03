package com.docker.app.repositories;

import com.docker.app.entities.ChamadoTecnico;
import com.docker.app.entities.Funcionario;
import com.docker.app.entities.Setor;
import com.docker.app.entities.enums.TipoChamado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;

public interface ChamadoTecnicoRepository extends JpaRepository<ChamadoTecnico, String> {
    Page<ChamadoTecnico> findBySetor_Nome(String setor, Pageable pageable);
    Page <ChamadoTecnico> findByResponsavelPelaAbertura(Funcionario funcionario, Pageable pageable);
    Page<ChamadoTecnico> findByResponsavelPelaExecucao(Funcionario funcionario, Pageable pageable);
    Page<ChamadoTecnico> findByTipoChamado(TipoChamado tipoChamado, Pageable pageable);
    Page<ChamadoTecnico> findByAtivoTrueAndByResponsavelPelaExecucao(Funcionario funcionario, Pageable pageable);
    Page<ChamadoTecnico> findByAtivoTrueAndByResponsavelPelaAbertura(Funcionario funcionario, Pageable pageable);
    Page<ChamadoTecnico> findByAtivoFalse(Pageable pageable);
    Page<ChamadoTecnico> findByDataCriacaoBetween(LocalDate dataCriacao, LocalDate dataCriacao2, Pageable pageable);
    Page<ChamadoTecnico> findAllByDataCriacaoBetweenAndAtivoTrue(LocalDate start, LocalDate end, Pageable pageable);
    Page<ChamadoTecnico> findAllByDataCriacaoGreaterThanEqual(LocalDate startInclusive, Pageable pageable);
    Page<ChamadoTecnico> findByDataCriacaoAndSetor_Nome(LocalDate dataCriacao, String setor, Pageable pageable);
    Page<ChamadoTecnico> findByDataCriacaoAndResponsavelPelaExecucao(LocalDate dataCriacao, Funcionario funcionario, Pageable pageable);




}
