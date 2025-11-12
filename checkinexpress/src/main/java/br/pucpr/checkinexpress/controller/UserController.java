package br.pucpr.checkinexpress.controller;

import br.pucpr.checkinexpress.dto.UserRegisterRequest;
import br.pucpr.checkinexpress.dto.UserUpdateRequest;
import br.pucpr.checkinexpress.dto.FuncionarioUpdateRequest; // NOVO IMPORT
import br.pucpr.checkinexpress.dto.LoginRequest;
import br.pucpr.checkinexpress.dto.LoginResponse;
import br.pucpr.checkinexpress.model.User;
import br.pucpr.checkinexpress.model.Funcionario; // NOVO IMPORT
import br.pucpr.checkinexpress.security.Role;
import br.pucpr.checkinexpress.service.UserService;
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

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Método auxiliar para obter o ID do usuário logado
    private Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserAuthentication) {
            UserAuthentication userAuth = (UserAuthentication) authentication.getPrincipal();
            return userAuth.getId();
        }

        throw new RuntimeException("Não foi possível obter o ID do usuário autenticado.");
    }

// ----------------------------------------------------------------------------------
// --- ENDPOINTS DE AUTENTICAÇÃO E REGISTRO DE HÓSPEDES ---
// ----------------------------------------------------------------------------------

    // --- C: CREATE (Registro de HÓSPEDE) - Rota pública ---
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest request) {
        User newUser = userService.registerUser(request);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    // --- LOGIN: Rota pública para autenticação ---
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = userService.authenticateAndGenerateToken(request);
        return ResponseEntity.ok(response);
    }

// ----------------------------------------------------------------------------------
// --- GERENCIAMENTO DO PRÓPRIO PERFIL ---
// ----------------------------------------------------------------------------------

    // --- R: READ (Ler Perfil do Usuário Logado) ---
    @GetMapping("/profile")
    public ResponseEntity<User> getProfile() {
        Long userId = getAuthenticatedUserId();
        User user = userService.findById(userId);
        return ResponseEntity.ok(user);
    }

    // --- U: UPDATE (Atualizar Perfil) ---
    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(@RequestBody @Valid UserUpdateRequest request) {
        Long userId = getAuthenticatedUserId();
        User updatedUser = userService.update(userId, request);
        return ResponseEntity.ok(updatedUser);
    }

    // --- D: DELETE (Excluir Conta) ---
    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteProfile() {
        Long userId = getAuthenticatedUserId();
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

// ----------------------------------------------------------------------------------
// --- CRUD DE FUNCIONÁRIOS/ADMINS (RESTRITO AO ADMIN) ---
// ----------------------------------------------------------------------------------

    // --- C: CREATE (Registro de FUNCIONÁRIO) ---
    @Secured("ROLE_ADMIN")
    @PostMapping("/register-funcionario")
    public ResponseEntity<User> registerFuncionario(@RequestBody @Valid UserRegisterRequest request) {
        User newFuncionario = userService.registerFuncionario(request);
        return new ResponseEntity<>(newFuncionario, HttpStatus.CREATED);
    }

    // --- C: CREATE (Registro de ADMIN) --- // ADICIONADO
    @Secured("ROLE_ADMIN")
    @PostMapping("/register-admin")
    public ResponseEntity<User> registerAdmin(@RequestBody @Valid UserRegisterRequest request) {
        User newAdmin = userService.registerAdmin(request);
        return new ResponseEntity<>(newAdmin, HttpStatus.CREATED);
    }

    // --- R: READ (Listar todos os FUNCIONARIOs) ---
    @Secured("ROLE_ADMIN")
    @GetMapping("/funcionarios")
    public ResponseEntity<List<User>> getAllFuncionarios() {
        List<User> funcionarios = userService.findByRole(Role.FUNCIONARIO);
        return ResponseEntity.ok(funcionarios);
    }

    // --- R: READ (Ler um FUNCIONARIO/ADMIN pelo ID) ---
    @Secured("ROLE_ADMIN")
    @GetMapping("/funcionarios/{id}")
    public ResponseEntity<User> getFuncionarioById(@PathVariable Long id) {
        User funcionario = userService.findById(id);

        if (funcionario.getRole() == Role.HOSPEDE) {
            throw new BusinessException("Acesso negado: ID pertence a um Hóspede.");
        }

        return ResponseEntity.ok(funcionario);
    }

    // --- U: UPDATE (Atualizar Nome e Senha do FUNCIONARIO/ADMIN por ID) ---
    @Secured("ROLE_ADMIN")
    @PutMapping("/funcionarios/{id}")
    public ResponseEntity<User> updateFuncionario(@PathVariable Long id, @RequestBody @Valid UserUpdateRequest request) {
        User updatedUser = userService.update(id, request);

        if (updatedUser.getRole() == Role.HOSPEDE) {
            throw new BusinessException("Acesso negado: ID pertence a um Hóspede.");
        }

        return ResponseEntity.ok(updatedUser);
    }

    // --- NOVO ENDPOINT: Atualizar Cargo e Salário do FUNCIONARIO/ADMIN por ID ---
    @Secured("ROLE_ADMIN")
    @PutMapping("/funcionarios/{id}/details")
    public ResponseEntity<Funcionario> updateFuncionarioDetails(@PathVariable Long id, @RequestBody @Valid FuncionarioUpdateRequest request) {
        // O Service já faz a verificação se o usuário é FUNCIONARIO/ADMIN
        Funcionario updatedFuncionario = userService.updateFuncionarioDetails(id, request);
        return ResponseEntity.ok(updatedFuncionario);
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