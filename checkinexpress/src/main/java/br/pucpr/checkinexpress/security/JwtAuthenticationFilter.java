package br.pucpr.checkinexpress.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    // O UserDetailsService é o seu AuthUserDetailService.
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 1. Verifica se o cabeçalho Authorization existe e começa com "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extrai o token JWT (removendo "Bearer ")
        jwt = authHeader.substring(7);

        // 3. Extrai o email/username do token (usando JwtService)
        userEmail = jwtService.extractUsername(jwt);

        // 4. Se o email existir e o usuário NÃO estiver autenticado
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Carrega os detalhes do usuário do banco (usando AuthUserDetailService)
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 5. Valida o token com os detalhes do usuário
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // Cria o objeto de autenticação do Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // A credencial (senha) não é necessária após o login
                        userDetails.getAuthorities()
                );

                // Adiciona detalhes da requisição ao token
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 6. Define o usuário no contexto de segurança (autentica o usuário)
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continua o processamento da requisição (passa para o próximo filtro ou controller)
        filterChain.doFilter(request, response);
    }
}