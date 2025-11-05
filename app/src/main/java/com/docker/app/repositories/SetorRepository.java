package com.docker.app.repositories;


import com.docker.app.entities.Setor;
import com.docker.app.entities.enums.Setores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SetorRepository extends JpaRepository<Setor, String> {
    Setor findByNome(Setores nome);
}
