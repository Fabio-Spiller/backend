package br.pucpr.checkinexpress.controller;

import br.pucpr.checkinexpress.security.AuthRequest;
import br.pucpr.checkinexpress.security.AuthResponse;
import br.pucpr.checkinexpress.security.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login") // Corresponde ao .requestMatchers("/api/login/**").permitAll() no SecurityConfig
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        // Chama o serviço para autenticar e gerar o token
        AuthResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }
}