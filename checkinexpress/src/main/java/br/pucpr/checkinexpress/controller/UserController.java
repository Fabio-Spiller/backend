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

    // 🟢 Criar usuário como ADMIN
    @PostMapping("/admin-create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> criarUsuarioPorAdmin(@RequestBody @Valid AdminCreateUserDTO dto) {
        UserDTO novo = userService.criarPorAdmin(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    // 🟢 Listar todos os usuários
    @GetMapping
    public ResponseEntity<List<UserDTO>> listarTodos() {
        List<UserDTO> usuarios = userService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    // 🟢 Buscar usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> buscarPorId(@PathVariable Long id) {
        UserDTO usuario = userService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    // 🟢 Atualizar usuário
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> atualizar(@PathVariable Long id, @RequestBody @Valid UserDTO userDTO) {
        UserDTO atualizado = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(atualizado);
    }

}
