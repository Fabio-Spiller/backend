package br.pucpr.checkinexpress.dto;

import br.pucpr.checkinexpress.model.TipoPagamento;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PagamentoRegisterRequest {

    // O valor do pagamento deve ser maior que zero
    @NotNull(message = "O valor é obrigatório.")
    @DecimalMin(value = "0.01", inclusive = true, message = "O valor deve ser maior que zero.")
    private BigDecimal valor;

    // O ID da reserva a que este pagamento se refere (pode ser nulo se for um pagamento avulso)
    private Long reservaId;

    @NotNull(message = "O tipo de pagamento é obrigatório.")
    private TipoPagamento tipoPagamento;

    // Getters e Setters
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public Long getReservaId() { return reservaId; }
    public void setReservaId(Long reservaId) { this.reservaId = reservaId; }

    public TipoPagamento getTipoPagamento() { return tipoPagamento; }
    public void setTipoPagamento(TipoPagamento tipoPagamento) { this.tipoPagamento = tipoPagamento; }
}