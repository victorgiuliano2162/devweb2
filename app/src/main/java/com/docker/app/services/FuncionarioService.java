package com.docker.app.services;


import com.docker.app.entities.Funcionario;
import com.docker.app.repositories.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioService {
    private final FuncionarioRepository funcionarioRepository;

    public FuncionarioService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }
    @Autowired
    PasswordEncoder passwordEncoder;

    public Funcionario getFuncionarioPorId(String id) {
        return funcionarioRepository.findById(id).get();
    }

    public Funcionario salvarFuncionario(Funcionario funcionario) {
        funcionario.setSenha(passwordEncoder.encode(funcionario.getSenha()));
        return  funcionarioRepository.save(funcionario);
    }

    public void excluirFuncionario(String id) {
        funcionarioRepository.deleteById(id);
    }

    public Funcionario getByEmail(String email) {
        return funcionarioRepository.findByEmail(email);
    }

    public void saveAll(List<Funcionario> funcionarios) {
        List<Funcionario> funcionariosComSenhaCriptografada = funcionarios.stream()
                .map(funcionario -> {
                    funcionario.setSenha(passwordEncoder.encode(funcionario.getSenha()));
                    return funcionario;
                })
                .toList();
        funcionarioRepository.saveAll(funcionariosComSenhaCriptografada);
    }

    public List<Funcionario> getAll() {
        return funcionarioRepository.findAll();
    }



}
