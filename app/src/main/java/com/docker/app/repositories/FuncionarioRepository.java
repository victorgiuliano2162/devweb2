package com.docker.app.repositories;

import com.docker.app.entities.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, String> {

    Optional<Funcionario> findByNome(String nome);
    Optional<Funcionario> findByEmail(String email);
}
