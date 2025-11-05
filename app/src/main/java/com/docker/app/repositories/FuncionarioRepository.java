package com.docker.app.repositories;

import com.docker.app.entities.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FuncionarioRepository extends JpaRepository<Funcionario, String> {
    Funcionario findByNome(String nome);
    Funcionario findByEmail(String email);

}
