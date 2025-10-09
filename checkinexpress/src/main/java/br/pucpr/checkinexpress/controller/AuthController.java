package br.pucpr.checkinexpress.controller;

import br.pucpr.checkinexpress.dto.RegisterRequestDTO;
import br.pucpr.checkinexpress.model.User;
import br.pucpr.checkinexpress.repository.UserRepository;
import br.pucpr.checkinexpress.security.JwtService;
import br.pucpr.checkinexpress.security.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserRepository repo;
    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository repo, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/register")
    public User register(@RequestBody @Valid RegisterRequestDTO dto) { // Usa o DTO

        // 1. Cria a entidade e copia os dados
        User user = new User();
        user.setName(dto.getName());       // <-- OBRIGATÓRIO, o DTO garante que existe!
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // Criptografa

        // 2. Define valores de segurança
        user.setRole(Role.USER);

        // 3. Salva no repositório
        return repo.save(user);
    }


    @PostMapping("/login")
    public String login(@RequestBody User user) {
        Optional<User> found = repo.findByEmail(user.getEmail());

        // Verifica se o usuário existe e compara a senha de forma segura (criptografada)
        if (found.isPresent() && passwordEncoder.matches(user.getPassword(), found.get().getPassword())) {
            return jwtService.generateToken(user.getEmail(), found.get().getRole().name());
        }

        throw new RuntimeException("Credenciais inválidas!");
    }
}
