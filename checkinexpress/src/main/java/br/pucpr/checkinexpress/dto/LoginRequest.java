package br.pucpr.checkinexpress.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// Record é uma forma moderna e concisa de criar classes de dados imutáveis.
// Perfeito para DTOs.
public record LoginRequest(
        @NotBlank(message = "O email não pode estar em branco")
        @Email(message = "O email deve ser válido")
        String email,

        @NotBlank(message = "A senha não pode estar em branco")
        String password
) {
}
