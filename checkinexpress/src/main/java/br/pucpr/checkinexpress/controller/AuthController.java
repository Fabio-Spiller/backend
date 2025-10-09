package br.pucpr.checkinexpress.controller;

import br.pucpr.checkinexpress.model.User;
import br.pucpr.checkinexpress.repository.UserRepository;
import br.pucpr.checkinexpress.security.JwtService;
import br.pucpr.checkinexpress.security.Role;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserRepository repo;
    private final JwtService jwtService;

    public AuthController(UserRepository repo, JwtService jwtService) {
        this.repo = repo;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        user.setRole(Role.USER);
        return repo.save(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        Optional<User> found = repo.findByEmail(user.getEmail());
        if (found.isPresent() && found.get().getPassword().equals(user.getPassword())) {
            return jwtService.generateToken(user.getEmail(), found.get().getRole().name());
        }
        throw new RuntimeException("Credenciais inválidas!");
    }
}
