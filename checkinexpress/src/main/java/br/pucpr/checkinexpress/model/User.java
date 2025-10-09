package br.pucpr.checkinexpress.model;

import br.pucpr.checkinexpress.security.Role; // Mantendo seu pacote original
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false) // Nome não pode ser nulo
    private String name;

    @Column(nullable = false, unique = true) // Email não pode ser nulo e deve ser único
    private String email;

    @Column(nullable = false) // Senha não pode ser nula
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) // Role não pode ser nulo
    private Role role; // ADMIN ou USER

    // Getters e setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public void setNome(String nome) {
    }

    public void setSenha(String senha) {
    }

    public String getNome() {
        return "";
    }

    public String getSenha() {
        return "";
    }
}