package br.pucpr.checkinexpress.dto;

import jakarta.validation.constraints.NotBlank;

// DTO para receber as credenciais do cliente
public class LoginRequest {

    @NotBlank(message = "O email é obrigatório.")
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    private String senha;

    // Construtor padrão necessário para desserialização JSON
    public LoginRequest() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}