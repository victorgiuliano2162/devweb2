package com.docker.app.entities;


import com.docker.app.entities.enums.TipoChamado;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

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

    private Date dataCriacao;
    private Date dataAtualizacao;
    private Boolean ativo;

    @ManyToOne(optional = false)
    private Setor setor;

    @Lob
    @Column(columnDefinition = "text")
    private String notas;
}
