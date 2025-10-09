package br.pucpr.checkinexpress.dto;

import br.pucpr.checkinexpress.model.User;
import br.pucpr.checkinexpress.security.Role;
import lombok.*;

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
        this.nome = user.getNome();
        this.email = user.getEmail();
        this.senha = user.getSenha();
        this.role = user.getRole();
    }
}
