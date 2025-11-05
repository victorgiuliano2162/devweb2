package com.docker.app.configuration.populateSQL;

import com.docker.app.entities.Setor;
import com.docker.app.entities.enums.Setores;
import com.docker.app.services.SetorService;

import java.util.Arrays;

public class SetorSQL {

    private final SetorService setorService;

    SetorSQL() {
        this.setorService = new SetorService();
    }
    public void createSetor() {
        Arrays.stream(Setores.values())
                .map(Setor::new)
                .forEach(setorService::save);
    }
}
