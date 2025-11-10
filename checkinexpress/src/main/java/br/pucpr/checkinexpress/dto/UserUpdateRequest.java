package br.pucpr.checkinexpress.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    // Senha é opcional, só deve ser preenchida se o usuário quiser alterar a senha
    private String novaSenha;

}