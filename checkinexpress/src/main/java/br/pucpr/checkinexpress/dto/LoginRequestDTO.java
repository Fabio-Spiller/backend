package br.pucpr.checkinexpress.dto;

import lombok.Getter; // Se estiver usando Lombok
import lombok.Setter; // Se estiver usando Lombok

// Se não estiver usando Lombok, adicione os getters/setters manualmente
@Getter
@Setter
public class LoginRequestDTO {
    private String email;
    private String password;
}