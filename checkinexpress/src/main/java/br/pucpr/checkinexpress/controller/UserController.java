package br.pucpr.checkinexpress.controller;

import br.pucpr.checkinexpress.dto.AdminCreateUserDTO;
import br.pucpr.checkinexpress.dto.UserDTO;
import br.pucpr.checkinexpress.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    @Autowired
    private UserService userService;

    // 🟢 Criar usuário comum
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // 🔒 Criar usuário como ADMIN (apenas ADMIN pode)
    @PostMapping("/admin-create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> criarPorAdmin(@RequestBody AdminCreateUserDTO dto) {
        return ResponseEntity.ok(userService.criarPorAdmin(dto));
    }

    // 🔒 Listar todos os usuários (apenas ADMIN pode)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> listarTodos() {
        return ResponseEntity.ok(userService.listarTodos());
    }

    // 🔒 Buscar usuário por ID (ADMIN pode ver qualquer um, USER pode ver apenas o próprio)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<UserDTO> buscarPorId(@PathVariable Long id) {
        UserDTO usuario = userService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    // 🔒 Atualizar usuário (apenas o próprio usuário ou ADMIN)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<UserDTO> atualizar(@PathVariable Long id, @RequestBody @Valid UserDTO userDTO) {
        UserDTO atualizado = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(atualizado);
    }
}

