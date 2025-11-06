package com.docker.app.configuration.populateSQL;

import com.docker.app.entities.Funcionario;
import com.docker.app.entities.enums.CargoFuncionario;
import com.docker.app.services.FuncionarioService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class FuncionarioSQL {

    private final FuncionarioService funcionarioService;


    public FuncionarioSQL(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    public void createFuncionarios() {

        Funcionario victor = new Funcionario(
                "Victor Giuliano de Freitas Silva",
                "08272635461",
                "vitor.giuliano@gmail.com",
                "153769ab",
                "81988860171",
                CargoFuncionario.DIRETOR,
                LocalDate.of(1998, 3, 19),
                LocalDate.of(2013, 11, 25)
        );

        Funcionario chico = new Funcionario(
                "Arthur Vieira Santos",
                "00000000",
                "arthursantosvs1211@gmail.com",
                "chico123",
                CargoFuncionario.DIRETOR,
                LocalDate.of(1998, 3, 19),
                LocalDate.of(2013, 11, 25)
        );

        Funcionario fAdmin1 = new Funcionario(
                "Carlos Alberto",
                "52998224725",
                "carlos.alberto@empresa.com",
                "senha123",
                CargoFuncionario.ADMINISTRATIVO,
                LocalDate.of(1985, 5, 10),
                LocalDate.of(2015, 7, 1)
        );

        Funcionario fAdmin2 = new Funcionario(
                "Mariana Souza",
                "12345678909",
                "mariana.souza@empresa.com",
                "senha123",
                CargoFuncionario.ADMINISTRATIVO,
                LocalDate.of(1990, 2, 20),
                LocalDate.of(2019, 4, 15)
        );

        Funcionario fTecnico1 = new Funcionario(
                "Paulo Henrique",
                "11144477735",
                "paulo.henrique@empresa.com",
                "senha123",
                CargoFuncionario.COMERCIAL,
                LocalDate.of(1992, 8, 5),
                LocalDate.of(2020, 9, 1)
        );

        Funcionario fTecnico2 = new Funcionario(
                "Ana Clara",
                "52998224725",
                "ana.clara@empresa.com",
                "senha123",
                CargoFuncionario.ADMINISTRATIVO,
                LocalDate.of(1998, 11, 12),
                LocalDate.of(2022, 3, 10)
        );

        Funcionario fTecJunior1 = new Funcionario(
                "Lucas Pereira",
                "12345678909",
                "lucas.pereira@empresa.com",
                "senha123",
                CargoFuncionario.TECNICO_JUNIOR,
                LocalDate.of(2000, 12, 2),
                LocalDate.of(2023, 2, 5)
        );

        Funcionario fTecJunior2 = new Funcionario(
                "Beatriz Lima",
                "11144477735",
                "beatriz.lima@empresa.com",
                "senha123",
                CargoFuncionario.TECNICO_JUNIOR,
                LocalDate.of(1999, 3, 18),
                LocalDate.of(2021, 8, 20)
        );

        Funcionario fTecPleno1 = new Funcionario(
                "Rafael Gomes",
                "52998224725",
                "rafael.gomes@empresa.com",
                "senha123",
                CargoFuncionario.TECNICO_PLENO,
                LocalDate.of(1990, 6, 9),
                LocalDate.of(2018, 10, 12)
        );

        Funcionario fTecPleno2 = new Funcionario(
                "Juliana Rocha",
                "12345678909",
                "juliana.rocha@empresa.com",
                "senha123",
                CargoFuncionario.TECNICO_PLENO,
                LocalDate.of(1988, 9, 30),
                LocalDate.of(2016, 5, 3)
        );

        Funcionario fTecSenior1 = new Funcionario(
                "Fernando Castro",
                "11144477735",
                "fernando.castro@empresa.com",
                "senha123",
                CargoFuncionario.TECNICO_SENIOR,
                LocalDate.of(1981, 1, 1),
                LocalDate.of(2010, 6, 2)
        );

        Funcionario fTecSenior2 = new Funcionario(
                "Patricia Alves",
                "52998224725",
                "patricia.alves@empresa.com",
                "senha123",
                CargoFuncionario.TECNICO_SENIOR,
                LocalDate.of(1982, 4, 14),
                LocalDate.of(2012, 12, 7)
        );

        Funcionario fAnalista1 = new Funcionario(
                "Diego Fernandes",
                "12345678909",
                "diego.fernandes@empresa.com",
                "senha123",
                CargoFuncionario.ANALISTA,
                LocalDate.of(1991, 7, 22),
                LocalDate.of(2019, 11, 1)
        );

        Funcionario fAnalista2 = new Funcionario(
                "Camila Martins",
                "11144477735",
                "camila.martins@empresa.com",
                "senha123",
                CargoFuncionario.ANALISTA,
                LocalDate.of(1993, 10, 4),
                LocalDate.of(2020, 4, 18)
        );

        Funcionario fAnalistaSenior1 = new Funcionario(
                "Roberto Silva",
                "52998224725",
                "roberto.silva@empresa.com",
                "senha123",
                CargoFuncionario.ANALISTA_SENIOR,
                LocalDate.of(1984, 2, 28),
                LocalDate.of(2014, 7, 10)
        );

        Funcionario fAnalistaSenior2 = new Funcionario(
                "Evelyn Costa",
                "12345678909",
                "evelyn.costa@empresa.com",
                "senha123",
                CargoFuncionario.ANALISTA_SENIOR,
                LocalDate.of(1986, 3, 16),
                LocalDate.of(2015, 9, 25)
        );

        Funcionario fCoordenador1 = new Funcionario(
                "Marcos Vinicius",
                "11144477735",
                "marcos.vinicius@empresa.com",
                "senha123",
                CargoFuncionario.SUPERVISOR,
                LocalDate.of(1983, 8, 11),
                LocalDate.of(2013, 10, 3)
        );

        Funcionario fCoordenador2 = new Funcionario(
                "Sofia Ribeiro",
                "52998224725",
                "sofia.ribeiro@empresa.com",
                "senha123",
                CargoFuncionario.TECNICO_JUNIOR,
                LocalDate.of(1987, 11, 29),
                LocalDate.of(2016, 3, 14)
        );

        Funcionario fSupervisor1 = new Funcionario(
                "André Moraes",
                "12345678909",
                "andre.moraes@empresa.com",
                "senha123",
                CargoFuncionario.SUPERVISOR,
                LocalDate.of(1981, 12, 6),
                LocalDate.of(2012, 1, 1)
        );

        Funcionario fSupervisor2 = new Funcionario(
                "Letícia Nunes",
                "11144477735",
                "leticia.nunes@empresa.com",
                "senha123",
                CargoFuncionario.SUPERVISOR,
                LocalDate.of(1989, 4, 8),
                LocalDate.of(2017, 6, 20)
        );

        Funcionario fGestor1 = new Funcionario(
                "Claudio Ramos",
                "52998224725",
                "claudio.ramos@empresa.com",
                "senha123",
                CargoFuncionario.GESTOR,
                LocalDate.of(1979, 10, 2),
                LocalDate.of(2009, 8, 7)
        );

        Funcionario fGestor2 = new Funcionario(
                "Vanessa Pinto",
                "12345678909",
                "vanessa.pinto@empresa.com",
                "senha123",
                CargoFuncionario.GESTOR,
                LocalDate.of(1982, 1, 19),
                LocalDate.of(2012, 11, 10)
        );

        Funcionario fGerente1 = new Funcionario(
                "Victor Almeida",
                "11144477735",
                "victor.almeida@empresa.com",
                "senha123",
                CargoFuncionario.RH,
                LocalDate.of(1978, 6, 21),
                LocalDate.of(2008, 5, 4)
        );

        Funcionario fGerente2 = new Funcionario(
                "Adriana Lopes",
                "52998224725",
                "adriana.lopes@empresa.com",
                "senha123",
                CargoFuncionario.COMERCIAL,
                LocalDate.of(1976, 2, 13),
                LocalDate.of(2006, 10, 30)
        );

// DIRETOR
        Funcionario fDiretor1 = new Funcionario(
                "Sergio Nascimento",
                "12345678909",
                "sergio.nascimento@empresa.com",
                "senha123",
                CargoFuncionario.DIRETOR,
                LocalDate.of(1970, 3, 3),
                LocalDate.of(2000, 2, 1)
        );

        Funcionario fDiretor2 = new Funcionario(
                "Helena Duarte",
                "11144477735",
                "helena.duarte@empresa.com",
                "senha123",
                CargoFuncionario.DIRETOR,
                LocalDate.of(1972, 7, 17),
                LocalDate.of(2001, 7, 12)
        );

        Funcionario fEstagiario1 = new Funcionario(
                "Bruno Teixeira",
                "52998224725",
                "bruno.teixeira@empresa.com",
                "senha123",
                CargoFuncionario.ESTAGIARIO,
                LocalDate.of(2002, 10, 2),
                LocalDate.of(2024, 2, 15)
        );

        Funcionario fEstagiario2 = new Funcionario(
                "Larissa Barros",
                "12345678909",
                "larissa.barros@empresa.com",
                "senha123",
                CargoFuncionario.ESTAGIARIO,
                LocalDate.of(2003, 12, 30),
                LocalDate.of(2024, 7, 1)
        );

        Funcionario fTrainee1 = new Funcionario(
                "Igor Santana",
                "11144477735",
                "igor.santana@empresa.com",
                "senha123",
                CargoFuncionario.TECNICO_JUNIOR,
                LocalDate.of(1998, 5, 14),
                LocalDate.of(2023, 4, 10)
        );

        Funcionario fTrainee2 = new Funcionario(
                "Paula Ramos",
                "52998224725",
                "paula.ramos@empresa.com",
                "senha123",
                CargoFuncionario.TECNICO_JUNIOR,
                LocalDate.of(1999, 8, 9),
                LocalDate.of(2022, 10, 20)
        );

        Funcionario fTemporario1 = new Funcionario(
                "Mauro Silveira",
                "12345678909",
                "mauro.silveira@empresa.com",
                "senha123",
                CargoFuncionario.TECNICO_PLENO,
                LocalDate.of(1995, 11, 25),
                LocalDate.of(2024, 3, 1)
        );

        Funcionario fTemporario2 = new Funcionario(
                "Rita Carvalho",
                "11144477735",
                "rita.carvalho@empresa.com",
                "senha123",
                CargoFuncionario.TECNICO_SENIOR,
                LocalDate.of(1994, 2, 6),
                LocalDate.of(2024, 6, 12)
        );

        Funcionario fHorista1 = new Funcionario(
                "Elias Moreira",
                "52998224725",
                "elias.moreira@empresa.com",
                "senha123",
                CargoFuncionario.RH,
                LocalDate.of(1990, 4, 3),
                LocalDate.of(2020, 8, 7)
        );

        Funcionario fHorista2 = new Funcionario(
                "Nina Gomes",
                "12345678909",
                "nina.gomes@empresa.com",
                "senha123",
                CargoFuncionario.GESTOR,
                LocalDate.of(1996, 9, 19),
                LocalDate.of(2021, 12, 2)
        );

        Funcionario fOperacional1 = new Funcionario(
                "Robson Almeida",
                "11144477735",
                "robson.almeida@empresa.com",
                "senha123",
                CargoFuncionario.OPERACIONAL,
                LocalDate.of(1987, 6, 27),
                LocalDate.of(2017, 9, 8)
        );

        Funcionario fOperacional2 = new Funcionario(
                "Carla Mendes",
                "52998224725",
                "carla.mendes@empresa.com",
                "senha123",
                CargoFuncionario.OPERACIONAL,
                LocalDate.of(1993, 1, 15),
                LocalDate.of(2019, 5, 18)
        );

        Funcionario fComercial1 = new Funcionario(
                "Gustavo Nogueira",
                "12345678909",
                "gustavo.nogueira@empresa.com",
                "senha123",
                CargoFuncionario.COMERCIAL,
                LocalDate.of(1989, 3, 28),
                LocalDate.of(2017, 1, 1)
        );

        Funcionario fComercial2 = new Funcionario(
                "Marisa Fontes",
                "11144477735",
                "marisa.fontes@empresa.com",
                "senha123",
                CargoFuncionario.COMERCIAL,
                LocalDate.of(1991, 7, 6),
                LocalDate.of(2018, 3, 20)
        );

        Funcionario fSuporte1 = new Funcionario(
                "Daniel Oliveira",
                "52998224725",
                "daniel.oliveira@empresa.com",
                "senha123",
                CargoFuncionario.COMERCIAL,
                LocalDate.of(1993, 1, 11),
                LocalDate.of(2020, 11, 5)
        );

        Funcionario fSuporte2 = new Funcionario(
                "Fabiana Pinto",
                "12345678909",
                "fabiana.pinto@empresa.com",
                "senha123",
                CargoFuncionario.COMERCIAL,
                LocalDate.of(1994, 5, 2),
                LocalDate.of(2021, 7, 30)
        );

        Funcionario fDesenv1 = new Funcionario(
                "Thiago Araújo",
                "11144477735",
                "thiago.araujo@empresa.com",
                "senha123",
                CargoFuncionario.DESENVOLVIMENTO,
                LocalDate.of(1990, 10, 7),
                LocalDate.of(2018, 2, 12)
        );

        Funcionario fDesenv2 = new Funcionario(
                "Sabrina Melo",
                "52998224725",
                "sabrina.melo@empresa.com",
                "senha123",
                CargoFuncionario.DESENVOLVIMENTO,
                LocalDate.of(1992, 12, 21),
                LocalDate.of(2019, 10, 9)
        );

        Funcionario fFinanceiro1 = new Funcionario(
                "Leonardo Pinto",
                "12345678909",
                "leonardo.pinto@empresa.com",
                "senha123",
                CargoFuncionario.OPERACIONAL,
                LocalDate.of(1986, 8, 1),
                LocalDate.of(2014, 12, 11)
        );

        Funcionario fFinanceiro2 = new Funcionario(
                "Gisele Araujo",
                "11144477735",
                "gisele.araujo@empresa.com",
                "senha123",
                CargoFuncionario.OPERACIONAL,
                LocalDate.of(1988, 11, 24),
                LocalDate.of(2015, 4, 5)
        );

// RH
        Funcionario fRh1 = new Funcionario(
                "Monica Farias",
                "52998224725",
                "monica.farias@empresa.com",
                "senha123",
                CargoFuncionario.RH,
                LocalDate.of(1985, 2, 2),
                LocalDate.of(2013, 5, 18)
        );

        Funcionario fRh2 = new Funcionario(
                "Pedro Santana",
                "12345678909",
                "pedro.santana@empresa.com",
                "senha123",
                CargoFuncionario.RH,
                LocalDate.of(1987, 4, 13),
                LocalDate.of(2015, 9, 24)
        );

        Funcionario fJuridico1 = new Funcionario(
                "Marcos Oliveira",
                "11144477735",
                "marcos.oliveira@empresa.com",
                "senha123",
                CargoFuncionario.JURIDICO,
                LocalDate.of(1983, 10, 29),
                LocalDate.of(2012, 7, 14)
        );

        Funcionario fJuridico2 = new Funcionario(
                "Renata Sousa",
                "52998224725",
                "renata.sousa@empresa.com",
                "senha123",
                CargoFuncionario.JURIDICO,
                LocalDate.of(1984, 12, 5),
                LocalDate.of(2013, 3, 28)
        );

        List<Funcionario> todosOsFuncionarios = Arrays.asList(
                fAdmin1, fAdmin2,
                fTecnico1, fTecnico2,
                fTecJunior1, fTecJunior2,
                fTecPleno1, fTecPleno2,
                fTecSenior1, fTecSenior2,
                fAnalista1, fAnalista2,
                fAnalistaSenior1, fAnalistaSenior2,
                fCoordenador1, fCoordenador2,
                fSupervisor1, fSupervisor2,
                fGestor1, fGestor2,
                fGerente1, fGerente2,
                fDiretor1, fDiretor2,
                fEstagiario1, fEstagiario2,
                fTrainee1, fTrainee2,
                fTemporario1, fTemporario2,
                fHorista1, fHorista2,
                fOperacional1, fOperacional2,
                fComercial1, fComercial2,
                fSuporte1, fSuporte2,
                fDesenv1, fDesenv2,
                fFinanceiro1, fFinanceiro2,
                fRh1, fRh2,
                fJuridico1, fJuridico2,
                victor, chico
        );

        // Agora você pode usar essa lista, por exemplo, para salvar todos no banco:
        funcionarioService.saveAll(todosOsFuncionarios);
    }
}
