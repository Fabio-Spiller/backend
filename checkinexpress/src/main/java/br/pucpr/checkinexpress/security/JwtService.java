package br.pucpr.checkinexpress.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class JwtService {

    // 1. Injeta a chave secreta do application.properties
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    // Define o tempo de expiração do token (Ex: 24 horas)
    public static final long EXPIRATION_TIME = TimeUnit.HOURS.toMillis(24);

    /**
     * Gera o JWT para o usuário.
     */
    public String generateToken(UserDetails userDetails) {
        // As claims são as informações que você quer armazenar no token (além do email/username)
        Map<String, Object> claims = new HashMap<>();

        // Se a sua classe UserDetails for UserAuthentication, você pode adicionar a Role
        if (userDetails instanceof UserAuthentication) {
            UserAuthentication auth = (UserAuthentication) userDetails;
            claims.put("role", auth.getRole().name());
        }

        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername()) // O username (email) é o assunto principal
                .setIssuedAt(new Date(System.currentTimeMillis())) // Data de criação
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Data de expiração
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Assina com a chave secreta
                .compact();
    }

    /**
     * Extrai o nome de usuário (email) do token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Valida se o token é válido para o usuário e se não está expirado.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Converte a chave secreta para o formato Key exigido pelo JJWT.
     * * CORREÇÃO: Foi removida a decodificação Base64 para chaves injetadas como texto simples
     * do application.properties. O texto é convertido diretamente para bytes UTF-8.
     */
    private Key getSigningKey() {
        // Converte a string do application.properties em bytes usando UTF-8
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}