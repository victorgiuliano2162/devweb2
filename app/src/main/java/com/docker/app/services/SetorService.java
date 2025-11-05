package com.docker.app.services;


import com.docker.app.entities.Setor;
import com.docker.app.entities.enums.Setores;
import com.docker.app.repositories.SetorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SetorService {

    @Autowired
    private SetorRepository setorRepository;

    public Setor findByNome(Setores nome) {
        return setorRepository.findByNome(nome);
    }

    public Setor save(Setor setor) {
        return setorRepository.save(setor);
    }
}
