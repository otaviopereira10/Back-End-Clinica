package com.projeto.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "clinicas")
public class Clinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 255)
    private String endereco;

    // 🔹 Evita referência cíclica
    @JsonBackReference
    @ManyToMany(mappedBy = "clinicas")
    private Set<Paciente> pacientes = new HashSet<>();

    @JsonBackReference
    @ManyToMany(mappedBy = "clinicas")
    private Set<Profissional> profissionais = new HashSet<>();

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
