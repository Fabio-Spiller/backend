package br.pucpr.checkinexpress.controller;

import br.pucpr.checkinexpress.dto.UserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    List<UserDTO> users = new ArrayList<>();

    @GetMapping
    public List<UserDTO> findAll() {
        return users;
    }

    public UserDTO save(UserDTO user) {
        users.add(user);
        return user;
    }
}
