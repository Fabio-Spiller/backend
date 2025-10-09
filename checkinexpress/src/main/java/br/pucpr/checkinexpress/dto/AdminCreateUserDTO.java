package br.pucpr.checkinexpress.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// Este DTO será usado em um endpoint PROTEGIDO para que um ADMIN possa criar
// outros usuários, especificando a Role.
public class AdminCreateUserDTO {

    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
    private String name;

    @NotBlank(message = "O email é obrigatório.")
    @Email(message = "Formato de email inválido.")
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
    private String password;

    // NOVO CAMPO: Permite ao ADMIN escolher a role do novo usuário.
    @NotBlank(message = "A role (papel) é obrigatória.")
    // Garante que o valor enviado é ou "USER" ou "ADMIN"
    @Pattern(regexp = "USER|ADMIN", message = "A role deve ser 'USER' ou 'ADMIN'.")
    private String role;

    // Construtor, Getters e Setters

    public AdminCreateUserDTO() {
    }

    // Getters e Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    // ... (restante dos getters e setters para email e password)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter e Setter para o novo campo 'role'
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}