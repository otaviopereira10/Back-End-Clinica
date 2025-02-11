package com.projeto.entities;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "profissionais")
public class Profissional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 100)
    private String especialidade;

    @Column(nullable = false, unique = true, length = 50)
    private String registro;

    @Column(nullable = false, length = 20)
    private String telefone;

    @ManyToMany
    @JoinTable(
        name = "profissional_clinica",
        joinColumns = @JoinColumn(name = "profissional_id"),
        inverseJoinColumns = @JoinColumn(name = "clinica_id")
    )
    private Set<Clinica> clinicas;

    public Profissional() {}

    public Profissional(String nome, String especialidade, String registro, String telefone) {
        this.nome = nome;
        this.especialidade = especialidade;
        this.registro = registro;
        this.telefone = telefone;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }

    public String getRegistro() { return registro; }
    public void setRegistro(String registro) { this.registro = registro; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public Set<Clinica> getClinicas() { return clinicas; }
    public void setClinicas(Set<Clinica> clinicas) { this.clinicas = clinicas; }
}
