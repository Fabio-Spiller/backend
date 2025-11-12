package br.pucpr.checkinexpress.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tab_funcionario")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // A chave estrangeira que liga à tab_usuario
    // O ID do funcionário será o mesmo ID da tabela User
    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false, unique = true)
    private User usuario;

    // Campos exclusivos do funcionário
    @Column(name = "data_admissao", nullable = false)
    private LocalDate dataAdmissao;

    @Column(nullable = true)
    private String cargo;

    // Exemplo: salários não devem ser nulos
    @Column(nullable = true)
    private Double salario;

    // Construtor padrão
    public Funcionario() {
    }

    // Construtor com campos obrigatórios
    public Funcionario(User usuario, LocalDate dataAdmissao) {
        this.usuario = usuario;
        this.dataAdmissao = dataAdmissao;
    }

    // --- Getters e Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

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