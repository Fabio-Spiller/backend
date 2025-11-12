package br.pucpr.checkinexpress.dto;

import br.pucpr.checkinexpress.model.StatusPagamento;
import jakarta.validation.constraints.NotNull;

/**
 * DTO (Data Transfer Object) usado para receber o novo status do pagamento
 * no corpo da requisição PATCH.
 */
public class PagamentoUpdateStatusRequest {

    // O novo status deve ser obrigatório. O nome do campo JSON será 'status'.
    @NotNull(message = "O novo status é obrigatório.")
    private StatusPagamento status;

    // Getters e Setters
    public StatusPagamento getStatus() {
        return status;
    }
    public void setStatus(StatusPagamento status) {
        this.status = status;
    }
}