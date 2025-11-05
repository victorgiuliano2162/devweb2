package com.docker.app.entities;


import com.docker.app.entities.enums.Setores;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Setor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    private Setores nome;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ChamadoTecnico> chamadosTecnicos = new ArrayList<>();

    @OneToMany
    private List<Funcionario> funcionarios = new ArrayList<>();

    public void adicionarChamado(ChamadoTecnico chamadoTecnico) {
        this.chamadosTecnicos.add(chamadoTecnico);
    }

    public void adicionarFuncionario(Funcionario funcionario) {
        this.funcionarios.add(funcionario);
    }

    public Setor (Setores nome) {
        this.nome = nome;
    }
}
