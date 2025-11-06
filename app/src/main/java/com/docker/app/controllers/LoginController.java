package com.docker.app.controllers;


import com.docker.app.entities.Funcionario;
import com.docker.app.services.FuncionarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("api/login")
public class LoginController {

    //TODO implement login with JWT and spring security

    private final FuncionarioService funcionarioService;
    private final PasswordEncoder passwordEncoder;

    public LoginController(FuncionarioService funcionarioService,
                           PasswordEncoder passwordEncoder) {
        this.funcionarioService = funcionarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody Funcionario funcionario) {
        Funcionario funcionarioResposta = funcionarioService.getByEmail(funcionario.getEmail());

        if (funcionarioResposta == null ||
                !passwordEncoder.matches(funcionario.getSenha(), funcionarioResposta.getSenha())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }

        return ResponseEntity.ok(funcionarioResposta);
    }


}


