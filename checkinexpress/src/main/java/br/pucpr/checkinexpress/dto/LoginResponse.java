package br.pucpr.checkinexpress.dto;

import br.pucpr.checkinexpress.model.Role;

// A resposta do login vai conter o token JWT e algumas informações do usuário
public record LoginResponse(
        String token,
        Long userId,
        String name,
        Role role
) {
}
