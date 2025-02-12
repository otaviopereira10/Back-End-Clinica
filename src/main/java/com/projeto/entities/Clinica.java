package com.projeto.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "clinicas")
public class Clinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false, length = 255)
    private String endereco;

    @ManyToMany(mappedBy = "clinicas")
    private Set<Paciente> pacientes = new HashSet<>();

    @ManyToMany(mappedBy = "clinicas")
    private Set<Profissional> profissionais = new HashSet<>();

    public Clinica() {}

    public Clinica(String nome, String endereco) {
        this.nome = nome;
        this.endereco = endereco;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public Set<Paciente> getPacientes() { return pacientes; }
    public void setPacientes(Set<Paciente> pacientes) { this.pacientes = pacientes; }

    public Set<Profissional> getProfissionais() { return profissionais; }
    public void setProfissionais(Set<Profissional> profissionais) { this.profissionais = profissionais; }
}
