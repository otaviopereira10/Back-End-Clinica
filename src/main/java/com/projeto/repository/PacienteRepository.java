package com.projeto.repository;

import com.projeto.entities.Paciente;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    // ✅ Garante que ao buscar todos os pacientes, as clínicas também sejam carregadas
    @EntityGraph(attributePaths = "clinicas")
    List<Paciente> findAll();

    // ✅ Busca paciente por ID e carrega as clínicas associadas
    @Query("SELECT p FROM Paciente p LEFT JOIN FETCH p.clinicas WHERE p.id = :id")
    Optional<Paciente> findByIdWithClinicas(Long id);
}
