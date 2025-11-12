package br.pucpr.checkinexpress.dto;

import br.pucpr.checkinexpress.model.TipoPagamento;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object) usado para atualizar os dados de um pagamento existente,
 * como valor e tipo de pagamento.
 * Não inclui o campo 'status', que é tratado por uma requisição separada (updateStatus).
 */
public class PagamentoUpdateRequest {

    @NotNull(message = "O valor é obrigatório.")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero.")
    private BigDecimal valor;

    @NotNull(message = "O tipo de pagamento é obrigatório.")
    private TipoPagamento tipoPagamento;

    // Getters e Setters

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(TipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }
}