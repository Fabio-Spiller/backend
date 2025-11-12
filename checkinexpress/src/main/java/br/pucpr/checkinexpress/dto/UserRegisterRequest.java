package br.pucpr.checkinexpress.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal; // Import para Salário
import lombok.Data; // Se você estiver usando Lombok

// @Data é ideal para DTOs (se estiver usando Lombok)
@Data
public class UserRegisterRequest {

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O formato do email é inválido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String senha;

    // --- CAMPOS OPCIONAIS PARA FUNCIONÁRIO/ADMIN ---
    // Deixados aqui para que a rota /register-funcionario possa usá-los.
    // Para HÓSPEDES, esses campos serão nulos ou vazios no JSON.
    private String cargo;
    private Double salario; // Ou use BigDecimal para maior precisão, mas Double é comum para exemplos.
}