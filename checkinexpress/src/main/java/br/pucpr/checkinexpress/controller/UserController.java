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

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    @Autowired
    private UserService userService;

    // ... (Métodos listarTodos, buscarPorId, atualizar, excluir permanecem inalterados) ...

    // 🔴 NOVO ENDPOINT DE ADMIN
    // Este endpoint permite que um ADMIN crie um novo USER ou ADMIN.
    @PostMapping("/admin-create")
    // Somente usuários com a ROLE ADMIN podem chamar este método.
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> criarUsuarioPorAdmin(@RequestBody @Valid AdminCreateUserDTO dto) {

        // Chamamos um novo método no serviço, que vai tratar o mapeamento e a Role.
        UserDTO novo = userService.criarPorAdmin(dto);

        // Usamos HttpStatus.CREATED (201) pois estamos criando um recurso.
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    // 🟢 Criar novo usuário (PODE SER REMOVIDO OU PROTEGIDO)
    // Se o /register (no AuthController) for o único lugar para criar USER,
    // este método aqui pode ser removido.
    @PostMapping
    public ResponseEntity<UserDTO> criar(@RequestBody UserDTO userDTO) {
        UserDTO novo = userService.salvar(userDTO);
        return ResponseEntity.ok(novo);
    }

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
