package br.pucpr.checkinexpress.controller;

import br.pucpr.checkinexpress.security.AuthRequest;
import br.pucpr.checkinexpress.security.AuthResponse;
import br.pucpr.checkinexpress.security.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest request) {
        // O AuthService faz a autenticação e retorna o JWT
        AuthResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }
}