package br.pucpr.checkinexpress.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "TAB_RESERVA")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DATA_CHECKIN", nullable = false)
    private LocalDateTime dataCheckin;

    @Column(name = "DATA_CHECKOUT", nullable = false)
    private LocalDateTime dataCheckout;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusReserva status = StatusReserva.ATIVA;

    // Usuário que fez a reserva
    @ManyToOne
    @JoinColumn(name = "USUARIO_ID", nullable = false)
    private User usuario;

    // Quarto reservado
    @ManyToOne
    @JoinColumn(name = "QUARTO_ID", nullable = false)
    private Quarto quarto;

    // Serviço opcional
    @ManyToOne
    @JoinColumn(name = "SERVICO_ID")
    private Servico servico;

    // Pagamento associado
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PAGAMENTO_ID", nullable = false)
    private Pagamento pagamento;
}
