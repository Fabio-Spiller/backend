package br.pucpr.checkinexpress.dto;

import br.pucpr.checkinexpress.model.Role;
import br.pucpr.checkinexpress.model.User;

import java.time.LocalDate;

// DTO que envia os dados de volta após uma operação.

public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private LocalDate birthDate;
    private String cpf;
    private String imageUrl;
    private Role role;

    // Construtor que converte uma Entidade User para o DTO de resposta.
    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.birthDate = user.getBirthDate();
        this.cpf = user.getCpf();
        this.imageUrl = user.getProfileImageUrl();
        this.role = user.getRole();
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getCpf() { return cpf; }
    public String getImageUrl() { return imageUrl; }
    public Role getRole() { return role; }
}
