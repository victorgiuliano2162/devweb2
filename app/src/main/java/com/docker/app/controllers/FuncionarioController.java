package com.docker.app.controllers;


import com.docker.app.entities.Funcionario;
import com.docker.app.services.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('SCOPE_DIRETOR')")
    public ResponseEntity<List<Funcionario>> listarFuncionario() {
        var all =  funcionarioService.getAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping
    public Funcionario getFuncionario(String id) {
        return funcionarioService.getFuncionarioPorId(id);
    }
}
