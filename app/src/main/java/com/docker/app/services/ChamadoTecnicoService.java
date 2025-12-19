package com.docker.app.services;

import com.docker.app.entities.ChamadoTecnico;
import com.docker.app.entities.Funcionario;
import com.docker.app.entities.Setor;
import com.docker.app.entities.enums.Setores;
import com.docker.app.entities.enums.TipoChamado;
import com.docker.app.repositories.ChamadoTecnicoRepository;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@CacheConfig(cacheNames = "chamadosTecnicos")
public class ChamadoTecnicoService {

    private final ChamadoTecnicoRepository chamadoTecnicoRepository;
    private final FuncionarioService funcionarioService;
    private final SetorService setorService;

    public ChamadoTecnicoService(ChamadoTecnicoRepository chamadoTecnicoRepository,
                                 FuncionarioService funcionarioService,
                                 SetorService setorService) {
        this.chamadoTecnicoRepository = chamadoTecnicoRepository;
        this.funcionarioService = funcionarioService;
        this.setorService = setorService;
    }

    @Transactional
    public Page<ChamadoTecnico> findAllWithSpec(Specification<ChamadoTecnico> spec, Pageable pageable) {
        return chamadoTecnicoRepository.findAll(spec, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ChamadoTecnico> findAll(Pageable pageable) {
        return chamadoTecnicoRepository.findAll(PageRequest.of(0, 10));
    }

    @Transactional
    @CachePut
    public ChamadoTecnico criarChamadoTecnico(ChamadoTecnico chamadoTecnico) {
        chamadoTecnico.setAtivo(true);

        String fId = chamadoTecnico.getResponsavelPelaAbertura().getId();
        Funcionario f = funcionarioService.getFuncionarioPorId(fId);
        System.out.println(f.getNome() + " " + f.getEmail() + "!!!!!!!!!!!!!!!!!");
        if (chamadoTecnico.getResponsavelPelaAbertura() != null
                && chamadoTecnico.getResponsavelPelaAbertura().getTelefone() != null) {

            chamadoTecnico.setContatoDoResponsavelPelaAbertura(
                    chamadoTecnico.getResponsavelPelaAbertura().getTelefone()
            );
        } else {
            chamadoTecnico.setContatoDoResponsavelPelaAbertura("");
        }

        if (chamadoTecnico.getSetor() != null) {
            Setores nomeSetor = chamadoTecnico.getSetor().getNome();
            Setor setorPersistente = setorService.findByNome(nomeSetor);
            chamadoTecnico.setSetor(setorPersistente);
        } else {
            throw new IllegalArgumentException("Setor é obrigatório para criar um chamado técnico");
        }

        chamadoTecnico.setResponsavelPelaAbertura(f);
        chamadoTecnico.setDataCriacao(LocalDateTime.now());
        chamadoTecnico.setDataAtualizacao(LocalDateTime.now());

        return chamadoTecnicoRepository.save(chamadoTecnico);
    }


    @Transactional(readOnly = true)
    public Page<ChamadoTecnico> findBySetor(Setores setor, Pageable pageable) {
        return chamadoTecnicoRepository.findBySetor_Nome(setor, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ChamadoTecnico> findByResponsavelPelaAbertura(String funcionarioId, Pageable pageable) {
        Funcionario funcionario = funcionarioService.getFuncionarioPorId(funcionarioId);
        return chamadoTecnicoRepository.findByResponsavelPelaAbertura(funcionario, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ChamadoTecnico> findByResponsavelPelaExecucao(String funcionarioId, Pageable pageable) {
        Funcionario funcionario = funcionarioService.getFuncionarioPorId(funcionarioId);
        return chamadoTecnicoRepository.findByResponsavelPelaExecucao(funcionario, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ChamadoTecnico> findByTipoChamado(TipoChamado tipoChamado, Pageable pageable) {

        return chamadoTecnicoRepository.findByTipoChamado(tipoChamado, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ChamadoTecnico> findByAtivoTrue(Pageable pageable) {
        return chamadoTecnicoRepository.findByAtivoTrue(pageable);
    }

    @Transactional(readOnly = true)
    public Page<ChamadoTecnico> findByAtivoFalse(Pageable pageable) {
        return chamadoTecnicoRepository.findByAtivoFalse(pageable);
    }

    @Transactional(readOnly = true)
    public Page<ChamadoTecnico> findByDataCriacaoBetween(LocalDate inicio, LocalDate fim, Pageable pageable) {
        return chamadoTecnicoRepository.findByDataCriacaoBetween(inicio, fim, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ChamadoTecnico> findAllByDataCriacaoBetweenAndAtivoTrue(LocalDate start, LocalDate end, Pageable pageable) {
        return chamadoTecnicoRepository.findAllByDataCriacaoBetweenAndAtivoTrue(start, end, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ChamadoTecnico> findAllByDataCriacaoGreaterThanEqual(LocalDate startInclusive, Pageable pageable) {
        return chamadoTecnicoRepository.findAllByDataCriacaoGreaterThanEqual(startInclusive, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ChamadoTecnico> findResponsavelPelaAberturaTrue(String funcionarioId, Pageable pageable) {
        Funcionario funcionario = funcionarioService.getFuncionarioPorId(funcionarioId);
        return chamadoTecnicoRepository.findByAtivoTrueAndResponsavelPelaAbertura(funcionario, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ChamadoTecnico> findResponsavelPelaAberturaTrue(LocalDate date, String setor, Pageable pageable) {
        return chamadoTecnicoRepository.findByDataCriacaoAndSetor_Nome(date, setor, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ChamadoTecnico> findResponsavelPelaAberturaPorData( String funcionarioId, LocalDate date, Pageable pageable) {
        Funcionario funcionario = funcionarioService.getFuncionarioPorId(funcionarioId);
        return chamadoTecnicoRepository.findByDataCriacaoAndResponsavelPelaExecucao(date, funcionario, pageable);
    }

    public void deleteChamado(ChamadoTecnico chamadoTecnico) {
        chamadoTecnicoRepository.delete(chamadoTecnico);
    }

    public void deleteChamadoTecnicoPorId(String chamadoId) {
        chamadoTecnicoRepository.deleteById(chamadoId);
    }

    public ChamadoTecnico finalizarChamado(String id) {
        ChamadoTecnico ct = chamadoTecnicoRepository.findById(id).get();
        ct.setAtivo(false);
        return chamadoTecnicoRepository.save(ct);
    }
}
