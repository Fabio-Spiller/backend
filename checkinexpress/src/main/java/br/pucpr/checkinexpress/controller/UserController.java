package br.pucpr.checkinexpress.controller;

import br.pucpr.checkinexpress.dto.AdminCreateUserDTO;
import br.pucpr.checkinexpress.dto.UserDTO;
import br.pucpr.checkinexpress.model.User;
import br.pucpr.checkinexpress.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import br.pucpr.checkinexpress.repository.UserRepository;


import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    @Autowired
    private UserService userService;

    // Métodos para listar, buscar, atualizar, excluir (sem mudanças)

    // NOVO ENDPOINT DE ADMIN
    @PostMapping("/admin-create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> criarUsuarioPorAdmin(@RequestBody @Valid AdminCreateUserDTO dto) {
        UserDTO novo = userService.criarPorAdmin(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    @Autowired

    // 🟢 Listar todos
    @GetMapping
    public ResponseEntity<List<UserDTO>> listarTodos() {
        return ResponseEntity.ok(userService.listarTodos());
    }

    // 🟢 Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> buscarPorId(@PathVariable Long id) {
        UserDTO usuario = userService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    // 🟢 Criar novo usuário
   // @PostMapping
    //public ResponseEntity<UserDTO> criar(@RequestBody UserDTO userDTO) {
      //  UserDTO novo = userService.salvar(userDTO);
        //return ResponseEntity.ok(novo);
    //}

    // 🟢 Atualizar usuário
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> atualizar(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO atualizado = userService.atualizar(id, userDTO);
        return ResponseEntity.ok(atualizado);
    }

    // 🟢 Excluir usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        userService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
