package com.projeto.repository;

import com.projeto.entities.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph;
import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    
    // 🔥 Garante que as clínicas sejam carregadas junto com os pacientes
    @Query("SELECT DISTINCT p FROM Paciente p LEFT JOIN FETCH p.clinicas")
    List<Paciente> findAllWithClinicas();

    // 🔥 Método para buscar um paciente por ID e trazer suas clínicas
    @Query("SELECT p FROM Paciente p LEFT JOIN FETCH p.clinicas WHERE p.id = :id")
    Optional<Paciente> findByIdWithClinicas(Long id);
}
