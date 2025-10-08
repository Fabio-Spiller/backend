package br.pucpr.checkinexpress.security;

import br.pucpr.checkinexpress.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Marca como um componente do Spring
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Tenta recuperar o token do cabeçalho da requisição
        var token = this.recoverToken(request);

        if (token != null) {
            // Valida o token e pega o email (subject)
            var email = tokenService.validateToken(token);
            if (!email.isEmpty()) {
                // Busca o usuário no banco de dados pelo email
                userRepository.findByEmail(email).ifPresent(user -> {
                    // Se encontrou, informa ao Spring que o usuário está autenticado
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                });
            }
        }
        // Continua a execução da requisição
        filterChain.doFilter(request, response);
    }

    /**
     * Extrai o token do cabeçalho "Authorization: Bearer <token>"
     */
    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}
