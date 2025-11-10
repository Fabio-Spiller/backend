package br.pucpr.checkinexpress.controller;

import br.pucpr.checkinexpress.dto.UserRegisterRequest;
import br.pucpr.checkinexpress.dto.UserUpdateRequest;
import br.pucpr.checkinexpress.dto.LoginRequest; // Import Novo
import br.pucpr.checkinexpress.dto.LoginResponse; // Import Novo
import br.pucpr.checkinexpress.model.User;
import br.pucpr.checkinexpress.service.UserService; // O serviço que irá autenticar
import br.pucpr.checkinexpress.security.UserAuthentication;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import br.pucpr.checkinexpress.exception.BusinessException;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    // Assumindo que o UserService tem o método de login
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Método auxiliar para obter o ID do usuário logado
    private Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verifica se o objeto principal é a sua implementação UserAuthentication
        if (authentication != null && authentication.getPrincipal() instanceof UserAuthentication) {
            UserAuthentication userAuth = (UserAuthentication) authentication.getPrincipal();
            return userAuth.getId();
        }

        // Se, por algum motivo, não conseguir obter o usuário, lança um erro
        throw new RuntimeException("Não foi possível obter o ID do usuário autenticado.");
    }

// ----------------------------------------------------------------------------------

    // --- C: CREATE (Registro) - Rota pública ---
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest request) {
        User newUser = userService.registerUser(request);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

// ----------------------------------------------------------------------------------

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        // Você PRECISA implementar o método authenticateAndGenerateToken no UserService
        LoginResponse response = userService.authenticateAndGenerateToken(request);
        return ResponseEntity.ok(response);
    }

    // --- R: READ (Ler Perfil do Usuário Logado) - Rota Protegida ---
    @GetMapping("/profile")
    public ResponseEntity<User> getProfile() {
        // Usa o ID do usuário logado para buscar os dados
        Long userId = getAuthenticatedUserId();
        User user = userService.findById(userId);
        return ResponseEntity.ok(user);
    }

// ----------------------------------------------------------------------------------

    // --- U: UPDATE (Atualizar Perfil) - Rota Protegida ---
    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(@RequestBody @Valid UserUpdateRequest request) {
        Long userId = getAuthenticatedUserId();
        // Chama o serviço para atualizar o nome e/ou a senha
        User updatedUser = userService.update(userId, request);
        return ResponseEntity.ok(updatedUser);
    }

// ----------------------------------------------------------------------------------

    // --- D: DELETE (Excluir Conta) - Rota Protegida ---
    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteProfile() {
        Long userId = getAuthenticatedUserId();
        userService.delete(userId);
        // Retorna 204 No Content, que é o padrão para exclusão bem-sucedida
        return ResponseEntity.noContent().build();
    }
}