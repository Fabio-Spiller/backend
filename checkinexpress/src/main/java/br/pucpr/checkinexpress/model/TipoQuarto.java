package br.pucpr.checkinexpress.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "TAB_TIPO_QUARTO")
public class TipoQuarto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "NOME", nullable = false, unique = true)
    private String nome; // Ex: Standard, Luxo, Suíte Master

    @Column(name = "DESCRICAO")
    private String descricao; // Texto descritivo

    @Column(name = "VALOR_DIARIA", nullable = false)
    private double valorDiaria; // Valor da diária (ex: 350.00)
}
