package br.pucpr.checkinexpress.security;

import br.pucpr.checkinexpress.model.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // Injeta o segredo JWT do application.properties
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Gera um token JWT para o usuário.
     */
    public String generateToken(User user) {
        try {
            // Define o algoritmo de assinatura com o nosso segredo
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("checkin-express-api") // Emissor do token
                    .withSubject(user.getEmail()) // O "dono" do token (aqui, o email)
                    .withExpiresAt(getExpirationDate()) // Data de expiração
                    .sign(algorithm); // Assina o token
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar o token JWT", exception);
        }
    }

    /**
     * Valida um token e retorna o "dono" (subject/email) dele.
     */
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("checkin-express-api")
                    .build()
                    .verify(token) // Verifica a assinatura e a validade
                    .getSubject(); // Retorna o email do usuário
        } catch (Exception exception) {
            return ""; // Retorna vazio se o token for inválido
        }
    }

    /**
     * Calcula a data de expiração do token (aqui, 2 horas a partir de agora).
     */
    private Instant getExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
