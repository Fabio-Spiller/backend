package br.pucpr.checkinexpress.config;

import br.pucpr.checkinexpress.security.JwtAuthenticationFilter;
import br.pucpr.checkinexpress.service.UserDetailsServiceImplementation; // Necessário para autenticação
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsServiceImplementation userDetailsService; // Implementação customizada

    // O UserDetailsServiceImplementation deve ser injetado para uso
    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, UserDetailsServiceImplementation userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    // 1. BEAN: Password Encoder (CRUCIAL para criptografia de senha)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. BEAN: Authentication Manager (CRUCIAL para o método login no AuthController)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // 3. BEAN: Provedor de Autenticação (Ensina o Spring a usar o nosso UserDetailsService e PasswordEncoder)
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Informa onde buscar o usuário
        authProvider.setPasswordEncoder(passwordEncoder());     // Informa como checar a senha criptografada
        return authProvider;
    }

    // 4. BEAN: Cadeia de Filtros de Segurança (As Regras de Autorização)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desabilita CSRF (usando a nova sintaxe de lambda)
                .csrf(AbstractHttpConfigurer::disable)

                // Define as regras de Autorização
                .authorizeHttpRequests(auth -> auth
                        // Permite acesso público ao registro e login
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        // Regras baseadas em Role (ADMIN/USER)
                        .requestMatchers("/api/v1/users/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/users/**").hasAnyRole("USER", "ADMIN")
                        // Todas as outras requisições devem estar autenticadas
                        .anyRequest().authenticated()
                )

                // Define a política de sessão como STATELESS (para JWT)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Adiciona o provedor de autenticação customizado
                .authenticationProvider(authenticationProvider())

                // Adiciona o filtro JWT antes do filtro padrão de Username/Password
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}