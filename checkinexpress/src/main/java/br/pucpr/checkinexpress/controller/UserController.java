package br.pucpr.checkinexpress.controller;

import br.pucpr.checkinexpress.dto.UserRegisterRequest;
import br.pucpr.checkinexpress.model.User;
import br.pucpr.checkinexpress.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint de Registro de Novo Usuário (NÃO precisa de Token para acessar)
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest request) {
        User newUser = userService.registerUser(request);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    // Você pode adicionar um endpoint protegido aqui para testar o login
    @GetMapping("/profile")
    // Este endpoint só será acessível após o login (pois o SecurityConfig exige autenticação)
    public ResponseEntity<String> getProfile() {
        return ResponseEntity.ok("Bem-vindo ao seu perfil! Você está autenticado via JWT.");
    }
}