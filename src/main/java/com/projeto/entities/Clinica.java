package com.projeto.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @ManyToMany
    @JoinTable(name = "paciente_clinica",
            joinColumns = @JoinColumn(name = "clinica_id"),
            inverseJoinColumns = @JoinColumn(name = "paciente_id"))
    @JsonManagedReference // ✅ Permite listar os pacientes dentro da clínica
    private Set<Paciente> pacientes = new HashSet<>();

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public Set<Paciente> getPacientes() { return pacientes; }
    public void setPacientes(Set<Paciente> pacientes) { this.pacientes = pacientes; }
}
