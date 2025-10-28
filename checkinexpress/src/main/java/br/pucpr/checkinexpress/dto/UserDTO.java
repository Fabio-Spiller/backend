package br.pucpr.checkinexpress.dto;

import br.pucpr.checkinexpress.model.User;
import br.pucpr.checkinexpress.security.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private Role role;

    public UserDTO(User user) {
        this.id = Long.valueOf(user.getId());
        this.nome = user.getName();
        this.email = user.getEmail();
        this.senha = user.getPassword();
        this.role = user.getRole();
    }

    public static UserDTO fromEntity(User user) {
        return new UserDTO(user); // Usando o construtor para criar um UserDTO
    }
}
