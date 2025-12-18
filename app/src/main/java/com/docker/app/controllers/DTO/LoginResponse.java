package com.docker.app.controllers.DTO;

import com.docker.app.entities.Funcionario;

public record LoginResponse(String accessToken, Long expiresIn, FuncionarioResponse funcionario) {
}
