package br.pucpr.checkinexpress.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    /**
     * Define a cadeia de filtros de segurança (as regras de acesso).
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Desabilita o CSRF, pois estamos usando JWT (Stateless)
                .csrf(AbstractHttpConfigurer::disable)

                // 1. Define as regras de autorização de requisições (quem pode acessar o quê)
                .authorizeHttpRequests(auth -> auth
                        // Permite acesso público ao Login e ao Registro de Hóspedes
                        .requestMatchers("/api/user/login", "/api/user/register").permitAll()

                        // Permite acesso público a ferramentas de desenvolvimento (Swagger e H2)
                        .requestMatchers("/h2-console/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // --- NOVAS REGRAS DE AUTENTICAÇÃO (PROTEGIDO POR JWT) ---

                        // Rotas do CRUD de Serviços: Exige JWT válido para todas, a restrição de Role
                        .requestMatchers("/api/servicos/**").authenticated()

                        // Rotas de CRUD de Funcionário/Admin (Exige JWT válido, a Role é checada via @Secured no Controller)
                        // Inclui /register-funcionario, /funcionarios/*, /funcionarios/*/*
                        .requestMatchers("/api/user/register-funcionario", "/api/user/register-admin").authenticated()
                        .requestMatchers("/api/user/funcionarios/**").authenticated()

                        // Acesso para FUNCIONARIO e ADMIN (serviços de quarto, etc.)
                        .requestMatchers("/quartos/**").hasAnyRole("ADMIN", "FUNCIONARIO")

                        // Todas as outras requisições devem ser autenticadas (como /profile, /profile/delete, /profile/update)
                        .anyRequest().authenticated()
                )

                // 2. Define a política de sessão como STATELESS (sem estado), essencial para JWT
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Adiciona o filtro JWT customizado antes do filtro de autenticação padrão
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // 4. Permite que o console H2 funcione (necessário para ver a interface gráfica)
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .build();
    }

    /**
     * Cria o gerenciador de autenticação, usado pelo AuthService para validar credenciais.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Define o encoder de senhas (BCrypt) para criptografar as senhas no banco.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}