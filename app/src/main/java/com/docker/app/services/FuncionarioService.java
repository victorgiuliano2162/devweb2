package com.docker.app.services;


import com.docker.app.entities.Funcionario;
import com.docker.app.repositories.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    public Funcionario getFuncionarioPorId(String id) {
        //TODO
        return funcionarioRepository.findById(id).get();
    }
}
