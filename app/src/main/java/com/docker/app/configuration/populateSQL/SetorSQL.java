package com.docker.app.configuration.populateSQL;

import com.docker.app.entities.Setor;
import com.docker.app.entities.enums.Setores;
import com.docker.app.services.SetorService;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class SetorSQL {

    private final SetorService setorService;

    public SetorSQL(SetorService setorService) {
        this.setorService = setorService;
    }

    public void createSetor() {
        Arrays.stream(Setores.values())
                .map(Setor::new)
                .forEach(setorService::save);
    }
}
