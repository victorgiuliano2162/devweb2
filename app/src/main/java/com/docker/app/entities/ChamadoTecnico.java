package com.docker.app.entities;


import com.docker.app.entities.enums.TipoChamado;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class ChamadoTecnico {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    private Funcionario responsavelPelaAbertura;
    private String contatoDoResponsavelPelaAbertura;
    @ManyToOne
    private Funcionario responsavelPelaExecucao;

    @Enumerated(EnumType.STRING)
    private TipoChamado tipoChamado;

    private LocalDate dataCriacao;
    private LocalDate dataAtualizacao;
    private Boolean ativo;

    @ManyToOne(optional = false)
    private Setor setor;

    @Lob
    @Column(columnDefinition = "text")
    private String notas;

    public ChamadoTecnico(Funcionario responsavelPelaAbertura,
                          String contatoDoResponsavelPelaAbertura,
                          Funcionario responsavelPelaExecucao,
                          TipoChamado tipoChamado,
                          LocalDate dataCriacao,
                          LocalDate dataAtualizacao,
                          Boolean ativo,
                          Setor setor,
                          String notas) {
        this.responsavelPelaAbertura = responsavelPelaAbertura;
        this.contatoDoResponsavelPelaAbertura = contatoDoResponsavelPelaAbertura;
        this.responsavelPelaExecucao = responsavelPelaExecucao;
        this.tipoChamado = tipoChamado;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.ativo = ativo;
        this.setor = setor;
        this.notas = notas;
    }
}
