package com.docker.app.services;


import com.docker.app.configuration.SecurityConfig;
import com.docker.app.entities.Funcionario;
import com.docker.app.repositories.FuncionarioRepository;
import com.docker.app.repositories.SetorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

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
}
