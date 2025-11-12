package br.pucpr.checkinexpress.model;

import br.pucpr.checkinexpress.security.Role_disponivel;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "TAB_QUARTO")
public class Quarto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "NUMERO", nullable = false, unique = true)
    private String numero; // Ex: "101", "203B"

    @Column(name = "ANDAR", nullable = false, unique = false )
    private int andar; // Ex: 1, 2, 3

    @Enumerated(EnumType.STRING)
    @Column(name = "DISPONIVEL", nullable = false)
    private Role_disponivel role = Role_disponivel.livre; // Adicionando o campo Role

    // Relacionamento com o tipo de quarto
    @ManyToOne
    @JoinColumn(name = "TIPO_ID", nullable = false)
    private TipoQuarto tipo;

    //relacionamento com o hospede
    @ManyToOne
    @JoinColumn(name = "HOSPEDE_ID", nullable = true)
    private User hospede;
}
