package com.docker.app.controllers;


import com.docker.app.entities.ChamadoTecnico;
import com.docker.app.entities.enums.TipoChamado;
import com.docker.app.services.ChamadoTecnicoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/tickets")
public class ChamadoTecnicoController {

    @Autowired
    private ChamadoTecnicoService chamadoTecnicoService;

    @PostMapping
    public ChamadoTecnico salvarChamadoTecnico(@RequestBody ChamadoTecnico chamadoTecnico) {
        return chamadoTecnicoService.criarChamadoTecnico(chamadoTecnico);
    }

    @GetMapping
    public Page<ChamadoTecnico> getChamadoPorSetor(@RequestParam String setor,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "20") int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        return chamadoTecnicoService.findBySetor(setor, pageable);
    }

    @GetMapping
    public Page<ChamadoTecnico> getChamadoPorQuemAbriu(@RequestParam String funcId,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "20") int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        return chamadoTecnicoService.findByResponsavelPelaAbertura(funcId, pageable);
    }

    @GetMapping
    public Page<ChamadoTecnico> getByResponsavelPelaExecucao(@RequestParam String funcId,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "20") int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        return chamadoTecnicoService.findByResponsavelPelaExecucao(funcId, pageable);
    }

    @GetMapping
    public Page<ChamadoTecnico> getByTipoChamado(@RequestParam TipoChamado tipoChamado,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "20") int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        return chamadoTecnicoService.findByTipoChamado(tipoChamado, pageable);
    }

    @GetMapping
    public Page<ChamadoTecnico> getChamadosAtivos(@RequestParam String funcId,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "20") int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        return chamadoTecnicoService.findByAtivoTrue(funcId, pageable);
    }

    @GetMapping
    public Page<ChamadoTecnico> getResponsavelPelaAberturaPorData(@RequestParam String funcId,
                                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "20") int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        return chamadoTecnicoService.findResponsavelPelaAberturaPorData(funcId, date, pageable);
    }
}
