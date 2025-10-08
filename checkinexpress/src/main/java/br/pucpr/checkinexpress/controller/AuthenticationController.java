package br.pucpr.checkinexpress.controller;

import br.pucpr.checkinexpress.dto.LoginRequest;
import br.pucpr.checkinexpress.dto.LoginResponse;
import br.pucpr.checkinexpress.model.User;
import br.pucpr.checkinexpress.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest data) {
        // 1. Cria um token de autenticação com email e senha (ainda não validado)
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());

        // 2. O Spring Security usa o AuthenticationManager para validar as credenciais.
        //    Ele chama nosso AuthorizationService para buscar o usuário e compara as senhas.
        var auth = this.authenticationManager.authenticate(usernamePassword);

        // 3. Se a autenticação foi bem-sucedida, pega os dados do usuário.
        var user = (User) auth.getPrincipal();

        // 4. Gera o token JWT para o usuário autenticado.
        var token = tokenService.generateToken(user);

        // 5. Cria a resposta com o token e dados básicos do usuário.
        var response = new LoginResponse(token, user.getId(), user.getName(), user.getRole());

        return ResponseEntity.ok(response);
    }
}
