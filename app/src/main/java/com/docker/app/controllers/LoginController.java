package com.docker.app.controllers;


import com.docker.app.controllers.DTO.FuncionarioResponse;
import com.docker.app.controllers.DTO.LoginRequest;
import com.docker.app.controllers.DTO.LoginResponse;
import com.docker.app.entities.Funcionario;
import com.docker.app.repositories.FuncionarioRepository;
import com.docker.app.services.FuncionarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;


@Controller
@RequestMapping("api/login")
public class LoginController {

    private final FuncionarioService funcionarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final FuncionarioRepository funcionarioRepository;

    public LoginController(FuncionarioService funcionarioService,
                           PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder, FuncionarioRepository funcionarioRepository) {
        this.funcionarioService = funcionarioService;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.funcionarioRepository = funcionarioRepository;
    }

    @GetMapping
    public String helloLogin() {
        return "Login end point works";
    }

    @PostMapping()
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        var funcionario = funcionarioRepository.findByEmail(loginRequest.email());
        System.out.println("name: " + loginRequest.email());

        if(funcionario.isEmpty() || !funcionario.get().isLoginCorrect(loginRequest, passwordEncoder)) throw new BadCredentialsException("Username or password invalid");

        var now = Instant.now();
        var expiresIn = 300L;
        var claims = JwtClaimsSet.builder()
                .issuer("my backend hurts")
                .subject(funcionario.get().getId())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        var funcResponse = FuncionarioResponse.fromEntity(funcionario.get());
        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn, funcResponse));
    }
}


