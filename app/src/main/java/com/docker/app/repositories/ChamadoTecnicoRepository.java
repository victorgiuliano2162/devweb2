package com.docker.app.repositories;

import com.docker.app.entities.ChamadoTecnico;
import com.docker.app.entities.Funcionario;
import com.docker.app.entities.enums.Setores;
import com.docker.app.entities.enums.TipoChamado;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;

@Repository
public interface ChamadoTecnicoRepository extends JpaRepository<ChamadoTecnico, String> {
    Page<ChamadoTecnico> findBySetor_Nome(Setores setor, Pageable pageable);
    Page <ChamadoTecnico> findByResponsavelPelaAbertura(Funcionario funcionario, Pageable pageable);
    Page<ChamadoTecnico> findByResponsavelPelaExecucao(Funcionario funcionario, Pageable pageable);
    Page<ChamadoTecnico> findByTipoChamado(TipoChamado tipoChamado, Pageable pageable);
    Page<ChamadoTecnico> findByAtivoTrueAndResponsavelPelaExecucao(Funcionario funcionario, Pageable pageable);
    Page<ChamadoTecnico> findByAtivoTrueAndResponsavelPelaAbertura(Funcionario funcionario, Pageable pageable);
    Page<ChamadoTecnico> findByAtivoTrue(Pageable pageable);
    Page<ChamadoTecnico> findByAtivoFalse(Pageable pageable);
    Page<ChamadoTecnico> findByDataCriacaoBetween(LocalDate dataCriacao, LocalDate dataCriacao2, Pageable pageable);
    Page<ChamadoTecnico> findAllByDataCriacaoBetweenAndAtivoTrue(LocalDate start, LocalDate end, Pageable pageable);
    Page<ChamadoTecnico> findAllByDataCriacaoGreaterThanEqual(LocalDate startInclusive, Pageable pageable);
    Page<ChamadoTecnico> findByDataCriacaoAndSetor_Nome(LocalDate dataCriacao, String setor, Pageable pageable);
    Page<ChamadoTecnico> findByDataCriacaoAndResponsavelPelaExecucao(LocalDate dataCriacao, Funcionario funcionario, Pageable pageable);
    Page<ChamadoTecnico> findAll(Specification<ChamadoTecnico> spec, Pageable pageable);



}
