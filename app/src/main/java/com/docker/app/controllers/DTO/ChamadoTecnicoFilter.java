package com.docker.app.controllers.DTO;

import com.docker.app.entities.enums.Setores;
import com.docker.app.entities.enums.TipoChamado;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ChamadoTecnicoFilter {
    private Setores setor;
    private String id;
    private TipoChamado tipoChamado;
    private Boolean ativo;
    private LocalDate date;
}