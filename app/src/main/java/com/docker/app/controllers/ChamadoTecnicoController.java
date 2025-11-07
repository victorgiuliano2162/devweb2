package com.docker.app.controllers;

import com.docker.app.entities.ChamadoTecnico;
import com.docker.app.entities.enums.Setores;
import com.docker.app.entities.enums.TipoChamado;
import com.docker.app.repositories.ChamadoTecnicoRepository;
import com.docker.app.services.ChamadoTecnicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("api/tickets")
public class ChamadoTecnicoController {

    @Autowired
    private ChamadoTecnicoService chamadoTecnicoService;

    @PostMapping("/end")
    public ChamadoTecnico finalizarChamado(@RequestParam String id) {
        return chamadoTecnicoService.finalizarChamado(id);
    }

    @PostMapping
    public ChamadoTecnico salvarChamadoTecnico(@RequestBody ChamadoTecnico chamadoTecnico) {
        return chamadoTecnicoService.criarChamadoTecnico(chamadoTecnico);
    }

    @GetMapping
    public Page<ChamadoTecnico> listarComFiltros(
            @RequestParam(required = false) Setores setor,
            @RequestParam(required = false) String funcId,
            @RequestParam(required = false) TipoChamado tipoChamado,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PageableDefault(page = 0, size = 20) Pageable pageable) {

        if (tipoChamado != null) {
            return chamadoTecnicoService.findByTipoChamado(tipoChamado, pageable);
        }
        if (setor != null) {
            return chamadoTecnicoService.findBySetor(setor, pageable);
        }
        if (funcId != null && date != null) {
            return chamadoTecnicoService.findResponsavelPelaAberturaPorData(funcId, date, pageable);
        }
        if (funcId != null) {
            return chamadoTecnicoService.findByResponsavelPelaAbertura(funcId, pageable);
        }
        if (ativo != null && ativo == true) {
            return chamadoTecnicoService.findByAtivoTrue(pageable);
        }
        if (ativo != null && !ativo) {
            return chamadoTecnicoService.findByAtivoFalse(pageable);
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