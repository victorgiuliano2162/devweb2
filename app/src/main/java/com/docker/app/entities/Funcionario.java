package com.docker.app.entities;

import com.docker.app.entities.enums.CargoFuncionario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Entity
@NoArgsConstructor
@Getter
@Setter
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String registro;
    private String nome;
    private String cpf;
    private String email;
    private String senha;
    private String telefone;
    private Date dataNascimento;
    private Date dataContratacao;

    @Enumerated(EnumType.STRING)
    private CargoFuncionario cargo;

    Funcionario(String nome,
                String cpf,
                String email,
                String senha,
                CargoFuncionario cargo,
                Date dataNascimento,
                Date dataContratacao) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.cargo = cargo;
        this.dataNascimento = dataNascimento;
        this.dataContratacao = dataContratacao;
    }

    public void alterarCargo(CargoFuncionario cargo) {
        this.cargo = cargo;
    }
}
