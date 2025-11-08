package br.pucpr.checkinexpress.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String senha; // O campo na sua entidade User é 'senha'

}