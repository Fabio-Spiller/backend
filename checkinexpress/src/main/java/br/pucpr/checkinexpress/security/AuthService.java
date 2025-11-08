package br.pucpr.checkinexpress.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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

    public AuthResponse authenticate(AuthRequest request) throws AuthenticationException {
        // 1. Tenta autenticar o usuário usando o email e a senha
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        // 2. Se a autenticação for bem-sucedida, pega os detalhes do usuário
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 3. Gera o JWT
        String token = jwtService.generateToken(userDetails);

        // 4. Retorna a resposta com o token
        return new AuthResponse(token);
    }
}