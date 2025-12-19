package com.docker.app.controllers;

import com.docker.app.controllers.DTO.ChamadoTecnicoFilter;
import com.docker.app.entities.ChamadoTecnico;
import com.docker.app.entities.enums.Setores;
import com.docker.app.entities.enums.TipoChamado;
import com.docker.app.repositories.ChamadoTecnicoRepository;
import com.docker.app.services.ChamadoTecnicoService;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/tickets")
public class ChamadoTecnicoController {


    private final ChamadoTecnicoService chamadoTecnicoService;

    ChamadoTecnicoController (ChamadoTecnicoService chamadoTecnicoService, ChamadoTecnicoRepository chamadoTecnicoRepository) {
        this.chamadoTecnicoService = chamadoTecnicoService;
    }

    @PostMapping("/end")
    public ChamadoTecnico finalizarChamado(@RequestParam String id) {
        return chamadoTecnicoService.finalizarChamado(id);
    }

    @PostMapping
    public ChamadoTecnico salvarChamadoTecnico(@RequestBody ChamadoTecnico chamadoTecnico) {
        return chamadoTecnicoService.criarChamadoTecnico(chamadoTecnico);
    }

    @PatchMapping
    public ChamadoTecnico atualizarChamadoTecnico(@RequestBody ChamadoTecnico chamadoTecnico) {
        System.out.println("📝 Atualizando ticket ID: " + chamadoTecnico.getId());
        return chamadoTecnicoService.atualizarChamadoTecnico(chamadoTecnico);
    }

    @GetMapping
    public Page<ChamadoTecnico> listarComFiltros(
            @RequestParam(required = false) Setores setor,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) TipoChamado tipoChamado,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PageableDefault(page = 0, size = 20) Pageable pageable) {

        if (ativo != null && ativo == true) {
            return chamadoTecnicoService.findByAtivoTrue(pageable);
        }
        if (ativo != null && !ativo) {
            return chamadoTecnicoService.findByAtivoFalse(pageable);
        }
        if (id != null && date != null) {
            return chamadoTecnicoService.findResponsavelPelaAberturaPorData(id, date, pageable);
        }
        if (tipoChamado != null) {
            return chamadoTecnicoService.findByTipoChamado(tipoChamado, pageable);
        }
        if (setor != null) {
            return chamadoTecnicoService.findBySetor(setor, pageable);
        }
        if (id != null) {
            return chamadoTecnicoService.findByResponsavelPelaAbertura(id, pageable);
        }


        return chamadoTecnicoService.findAll(pageable);
    }

    @DeleteMapping
    public void deletarChamado(
            @RequestParam(required = false) String chamadoTecnicoId,
            @RequestBody(required = false) ChamadoTecnico chamadoTecnico
    ) {
        if (chamadoTecnicoId != null) {
            chamadoTecnicoService.deleteChamadoTecnicoPorId(chamadoTecnicoId);
        }
        if (chamadoTecnico != null) {
            chamadoTecnicoService.deleteChamado(chamadoTecnico);
        }

        throw new IllegalArgumentException("Informações inválidas");
    }

}

//    @GetMapping
//    public Page<ChamadoTecnico> listarComFiltros(
//            @ModelAttribute @Valid ChamadoTecnicoFilter filter,
//            @PageableDefault(page = 0, size = 20, sort = "dataCriacao", direction = Sort.Direction.DESC) Pageable pageable) {
//
//        Specification<ChamadoTecnico> spec = (root, query, cb) -> {
//            List<Predicate> predicates = new ArrayList<>();
//
//            if (filter.getSetor() != null) predicates.add(cb.equal(root.get("setor"), filter.getSetor()));
//            if (filter.getTipoChamado() != null) predicates.add(cb.equal(root.get("tipoChamado"), filter.getTipoChamado()));
//            if (filter.getId() != null) predicates.add(cb.equal(root.join("responsavelPelaAbertura").get("id"), filter.getId()));
//            if (filter.getDate() != null) predicates.add(cb.equal(root.get("dataAbertura"), filter.getDate()));
//            if (filter.getAtivo() != null) predicates.add(cb.equal(root.get("ativo"), filter.getAtivo()));
//
//            return cb.and(predicates.toArray(new Predicate[0]));
//        };
//
//        return chamadoTecnicoService.findAllWithSpec(spec, pageable);
//    }


//    @GetMapping
//    public Page<ChamadoTecnico> listarComFiltros(
//            @Valid ChamadoTecnicoFilter filter,
//            @PageableDefault(page = 0, size = 20, sort = "dataAbertura", direction = Sort.Direction.DESC) Pageable pageable) {
//
//        Specification<ChamadoTecnico> spec = (root, query, cb) -> {
//            List<Predicate> predicates = new ArrayList<>();
//
//            if (filter.getSetor() != null) {
//                predicates.add(cb.equal(root.get("setor"), filter.getSetor()));
//            }
//            if (filter.getTipoChamado() != null) {
//                predicates.add(cb.equal(root.get("tipoChamado"), filter.getTipoChamado()));
//            }
//            if (filter.getFuncId() != null) {
//                predicates.add(cb.equal(root.join("responsavelPelaAbertura").get("id"), filter.getFuncId()));
//            }
//            if (filter.getDate() != null) {
//                predicates.add(cb.equal(root.get("dataAbertura"), filter.getDate()));
//            }
//            if (filter.getAtivo() != null) {
//                predicates.add(cb.equal(root.get("ativo"), filter.getAtivo()));
//            }
//
//            return cb.and(predicates.toArray(new Predicate[0]));
//        };
//
//        return chamadoTecnicoService.findAllWithSpec(spec, pageable);
//    }
//