package com.docker.app.controllers;


import com.docker.app.entities.enums.Setores;
import com.docker.app.entities.enums.StatusChamado;
import com.docker.app.entities.enums.TipoChamado;
import com.docker.app.entities.enums.CargoFuncionario;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/enums")
public class EnumsController {

    @GetMapping
    public Map<String, List<String>> getEnums() {
        Map<String, List<String>> enumsListados = new LinkedHashMap<>();

        enumsListados.put("StatusChamado", Arrays.stream(StatusChamado.values())
                .map(Enum::name)
                .collect(Collectors.toList()));

        enumsListados.put("Setores", Arrays.stream(Setores.values())
                .map(Enum::name)
                .collect(Collectors.toList()));

        enumsListados.put("Tipo Chamado", Arrays.stream(TipoChamado.values())
                .map(Enum::name)
                .collect(Collectors.toList()));

        enumsListados.put("Tipo Funcionario", Arrays.stream(CargoFuncionario.values())
                .map(Enum::name)
                .collect(Collectors.toList()));
        return enumsListados;
    }
}
