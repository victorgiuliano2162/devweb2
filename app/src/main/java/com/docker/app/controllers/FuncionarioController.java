package com.docker.app.controllers;


import com.docker.app.entities.Funcionario;
import com.docker.app.services.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/funcionario")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @PostMapping
    public Funcionario salvarFuncionario(@RequestBody Funcionario funcionario) {
        return funcionarioService.salvarFuncionario(funcionario);
    }

    @DeleteMapping
    public void excluirFuncionario(@RequestParam String id) {
        funcionarioService.excluirFuncionario(id);
    }

    @PatchMapping
    public Funcionario alterarFuncionario(@RequestBody Funcionario funcionario) {
        //o service faz a chamada do método save() do JPA reprository
        //ele automaticamente verifica a existência da entendidade e altera os atributos necessários caso ela já exista no banco
        return funcionarioService.salvarFuncionario(funcionario);
    }

    @GetMapping("/all")
    public List<Funcionario> listarFuncionario() {
        return funcionarioService.getAll();
    }

    @GetMapping
    public Funcionario getFuncionario(String id) {
        return funcionarioService.getFuncionarioPorId(id);
    }
}
