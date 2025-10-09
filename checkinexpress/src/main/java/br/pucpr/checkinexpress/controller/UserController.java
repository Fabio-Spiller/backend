package br.pucpr.checkinexpress.controller;

import br.pucpr.checkinexpress.dto.UserDTO;
import br.pucpr.checkinexpress.model.User;
import br.pucpr.checkinexpress.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    @Autowired
    private UserService userService;

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
    @PostMapping
    public ResponseEntity<UserDTO> criar(@RequestBody UserDTO userDTO) {
        UserDTO novo = userService.salvar(userDTO);
        return ResponseEntity.ok(novo);
    }

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
