package br.pucpr.checkinexpress.controller;

import br.pucpr.checkinexpress.dto.CreateUserRequest;
import br.pucpr.checkinexpress.dto.UserResponse;
import br.pucpr.checkinexpress.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador responsável por expor os endpoints (rotas) relacionados a usuários.
 */
@RestController // Anotação que combina @Controller e @ResponseBody, indicando que os retornos dos métodos serão o corpo da resposta HTTP.
@RequestMapping("/api/users") // Define que todos os endpoints nesta classe começarão com "/api/users".
public class UserController {

    private final UserService userService;

    // Injetamos o UserService para poder usar sua lógica de negócio.
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint para criar um novo usuário.
     * Mapeado para requisições POST em "/api/users".
     * @param request O corpo da requisição, contendo os dados do novo usuário.
     * @return uma ResponseEntity com os dados do usuário criado e o status HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        // Delega a lógica de criação para a camada de serviço.
        UserResponse response = userService.create(request);

        // Retorna uma resposta de sucesso, com o status 201 (Criado) e os dados do usuário no corpo.
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
