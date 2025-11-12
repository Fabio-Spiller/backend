package br.pucpr.checkinexpress.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tab_pagamento")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Valor da transação
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    // Referência à Reserva (assumindo que existirá a tabela Reserva)
    @Column(name = "reserva_id", nullable = true)
    private Long reservaId;

    // O status atual do pagamento
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPagamento status;

    // O método utilizado (Cartão, Pix, Dinheiro)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPagamento tipoPagamento;

    // Data e hora em que o pagamento foi registrado
    @Column(nullable = false)
    private LocalDateTime dataRegistro;

    // Construtor padrão
    public Pagamento() {
        this.status = StatusPagamento.PENDENTE;
        this.dataRegistro = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public Long getReservaId() { return reservaId; }
    public void setReservaId(Long reservaId) { this.reservaId = reservaId; }

    public StatusPagamento getStatus() { return status; }
    public void setStatus(StatusPagamento status) { this.status = status; }

    public TipoPagamento getTipoPagamento() { return tipoPagamento; }
    public void setTipoPagamento(TipoPagamento tipoPagamento) { this.tipoPagamento = tipoPagamento; }

    public LocalDateTime getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(LocalDateTime dataRegistro) { this.dataRegistro = dataRegistro; }
}