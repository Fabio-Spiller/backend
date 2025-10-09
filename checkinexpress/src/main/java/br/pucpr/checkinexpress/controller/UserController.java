package br.pucpr.checkinexpress.controller;

import br.pucpr.checkinexpress.model.User;
import br.pucpr.checkinexpress.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users") // Caminho base para o CRUD de Usuário: http://localhost:8080/users
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 1. SALVAR (CREATE)
    @PostMapping
    // Exemplo: POST http://localhost:8080/users
    public ResponseEntity<User> create(@RequestBody User user) {
        User savedUser = userService.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED); // Retorna HTTP 201
    }

    // 2. LISTAR TODOS (READ ALL)
    @GetMapping
    // Exemplo: GET http://localhost:8080/users
    public List<User> findAll() {
        return userService.findAll();
    }

    // 3. BUSCAR POR ID (READ ONE)
    @GetMapping("/{id}")
    // Exemplo: GET http://localhost:8080/users/1
    public ResponseEntity<User> findById(@PathVariable Integer id) {
        // O findById no Service lança exceção se não encontrar, resultando em 404 (Not Found)
        User user = userService.findById(id);
        return ResponseEntity.ok(user); // Retorna HTTP 200
    }

    // 4. ALTERAR (UPDATE)
    @PutMapping("/{id}")
    // Exemplo: PUT http://localhost:8080/users/1
    public ResponseEntity<User> update(@PathVariable Integer id, @RequestBody User userDetails) {
        User existingUser = userService.findById(id);

        // Mapeamento dos campos a serem alterados:
        existingUser.setName(userDetails.getName());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setRole(userDetails.getRole());

        // A senha deve ser tratada separadamente e criptografada em um sistema real.

        User updatedUser = userService.save(existingUser);
        return ResponseEntity.ok(updatedUser); // Retorna HTTP 200
    }

    // 5. EXCLUIR (DELETE)
    @DeleteMapping("/{id}")
    // Exemplo: DELETE http://localhost:8080/users/1
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.noContent().build(); // Retorna HTTP 204
    }
}