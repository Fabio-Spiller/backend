package br.pucpr.checkinexpress.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse authenticate(AuthRequest request) {
        // 1. Tenta autenticar o usuário com as credenciais
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        // 2. Pega os detalhes do usuário autenticado
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 3. Gera o JWT
        String token = jwtService.generateToken(userDetails);

        // 4. Retorna a resposta
        return new AuthResponse(token);
    }
}