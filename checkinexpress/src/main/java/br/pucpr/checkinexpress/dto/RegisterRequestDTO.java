package br.pucpr.checkinexpress.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequestDTO {

    // Adiciona validação para garantir que o 'name' não seja nulo nem vazio
    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
    private String name;

    @NotBlank(message = "O email é obrigatório.")
    @Email(message = "Formato de email inválido.")
    private String email;

    // Adiciona validação para garantir que a 'password' não seja nula nem vazia
    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
    private String password;

    // Construtores, Getters e Setters (necessários para o Spring mapear o JSON)

    public RegisterRequestDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
}