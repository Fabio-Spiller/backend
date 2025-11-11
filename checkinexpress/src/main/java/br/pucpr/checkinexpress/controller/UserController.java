package br.pucpr.checkinexpress.controller;

import br.pucpr.checkinexpress.dto.UserRegisterRequest;
import br.pucpr.checkinexpress.dto.UserUpdateRequest;
import br.pucpr.checkinexpress.dto.LoginRequest; // Import Novo
import br.pucpr.checkinexpress.dto.LoginResponse; // Import Novo
import br.pucpr.checkinexpress.model.User;
import br.pucpr.checkinexpress.security.Role;
import br.pucpr.checkinexpress.service.UserService; // O serviço que irá autenticar
import br.pucpr.checkinexpress.security.UserAuthentication;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import br.pucpr.checkinexpress.exception.BusinessException;
import org.springframework.security.access.annotation.Secured;

import java.util.List;


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

    // --- CRUD DE FUNCIONÁRIOS (RESTRITO AO ADMIN) ---
// ----------------------------------------------------------------------------------
    // --- C: CREATE (Registro de FUNCIONÁRIO) ---
    @Secured("ROLE_ADMIN")
    @PostMapping("/register-funcionario")
    public ResponseEntity<User> registerFuncionario(@RequestBody @Valid UserRegisterRequest request) {
        User newFuncionario = userService.registerFuncionario(request);
        return new ResponseEntity<>(newFuncionario, HttpStatus.CREATED);
    }

    // --- R: READ (Listar todos os FUNCIONARIOs) ---
    @Secured("ROLE_ADMIN")
    @GetMapping("/funcionarios")
    public ResponseEntity<List<User>> getAllFuncionarios() {
        // Busca apenas usuários com a Role FUNCIONARIO
        List<User> funcionarios = userService.findByRole(Role.FUNCIONARIO);
        return ResponseEntity.ok(funcionarios);
    }

    // --- R: READ (Ler um FUNCIONARIO/ADMIN pelo ID) ---
    @Secured("ROLE_ADMIN")
    @GetMapping("/funcionarios/{id}")
    public ResponseEntity<User> getFuncionarioById(@PathVariable Long id) {
        User funcionario = userService.findById(id);

        // Regra de Negócio: Impede que esta rota seja usada para buscar HÓSPEDES
        if (funcionario.getRole() == Role.HOSPEDE) {
            throw new BusinessException("Acesso negado: ID pertence a um Hóspede.");
        }

        return ResponseEntity.ok(funcionario);
    }

    // --- U: UPDATE (Atualizar FUNCIONARIO/ADMIN por ID) ---
    @Secured("ROLE_ADMIN")
    @PutMapping("/funcionarios/{id}")
    public ResponseEntity<User> updateFuncionario(@PathVariable Long id, @RequestBody @Valid UserUpdateRequest request) {
        User updatedUser = userService.update(id, request);

        if (updatedUser.getRole() == Role.HOSPEDE) {
            throw new BusinessException("Acesso negado: ID pertence a um Hóspede.");
        }

        return ResponseEntity.ok(updatedUser);
    }

    // --- D: DELETE (Deletar FUNCIONARIO/ADMIN por ID) ---
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/funcionarios/{id}")
    public ResponseEntity<Void> deleteFuncionario(@PathVariable Long id) {
        User userToDelete = userService.findById(id);
        if (userToDelete.getRole() == Role.HOSPEDE) {
            throw new BusinessException("Acesso negado: Não é permitido deletar um Hóspede por esta rota.");
        }

        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}