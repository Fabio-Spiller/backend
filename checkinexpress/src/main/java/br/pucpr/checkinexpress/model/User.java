package br.pucpr.checkinexpress.model;

import br.pucpr.checkinexpress.security.Role;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "TAB_USUARIO")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Geralmente IDENTITY, não AUTO
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "EMAIL", unique = true, nullable = false)
    private String email;

    @Column(name = "SENHA", nullable = false)
    private String senha; // O Spring Security espera que esta senha esteja ENCRIPTADA!

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    private Role role = Role.USER; // Adicionando o campo Role

    // Outros campos como idade, etc.
}