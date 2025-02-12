package com.projeto.repository;

import com.projeto.entities.Paciente;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    @EntityGraph(attributePaths = "clinicas")
    List<Paciente> findAllWithClinicas();

    @EntityGraph(attributePaths = "clinicas")
    Optional<Paciente> findByIdWithClinicas(Long id);
}
