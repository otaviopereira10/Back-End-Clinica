package com.projeto.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "cadastro")
public class Cadastro_login {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "registro", length = 100) // Removendo NOT NULL, pois nem todos precisarão
    private String registro;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "user", nullable = false, length = 100, unique = true)
    private String user;

    @Column(name = "senha", nullable = false, length = 100)
    private String senha;

    @Column(name = "role", nullable = false, length = 20) // Adicionando tipo de usuário
    private String role;

    // Construtores
    public Cadastro_login() {}

    public Cadastro_login(String registro, String nome, String email, String user, String senha, String role) {
        this.registro = registro;
        this.nome = nome;
        this.email = email;
        this.user = user;
        this.senha = senha;
        this.role = role;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
