package com.docker.app.controllers.DTO;

import com.docker.app.entities.Funcionario;
import com.docker.app.entities.enums.CargoFuncionario;

public record FuncionarioResponse(String id, String registro, String nome, String email, String telefone, CargoFuncionario cargo) {
    public static FuncionarioResponse fromEntity(Funcionario f) {
        return new FuncionarioResponse(f.getId(), f.getRegistro(), f.getNome(), f.getEmail(), f.getTelefone(), f.getCargo());
    }
}


