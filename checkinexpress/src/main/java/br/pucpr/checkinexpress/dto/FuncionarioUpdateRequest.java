package br.pucpr.checkinexpress.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// DTO para atualizar dados específicos da tabela Funcionario (Cargo e Salario)
public class FuncionarioUpdateRequest {

    // Cargo não pode ser vazio
    @NotBlank(message = "O cargo é obrigatório.")
    private String cargo;

    // Salário deve ser fornecido e ser um valor positivo
    @NotNull(message = "O salário é obrigatório.")
    @Positive(message = "O salário deve ser um valor positivo.")
    private Double salario;

    // Construtor padrão
    public FuncionarioUpdateRequest() {
    }

    // --- Getters e Setters ---

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }
}