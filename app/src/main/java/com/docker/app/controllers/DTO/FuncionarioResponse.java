package com.docker.app.controllers.DTO;

import com.docker.app.entities.Funcionario;

public record FuncionarioResponse(String id, String registro, String nome, String email, String telefone) {
    public static FuncionarioResponse fromEntity(Funcionario f) {
        return new FuncionarioResponse(f.getId(), f.getRegistro(), f.getNome(), f.getEmail(), f.getTelefone());
    }
}


