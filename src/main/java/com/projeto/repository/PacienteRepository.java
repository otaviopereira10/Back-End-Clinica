package com.projeto.repository;

import com.projeto.entities.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.EntityGraph;
import java.util.List;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
	
    // 🔥 Garante que as clínicas são carregadas junto com os pacientes no GET
    @EntityGraph(attributePaths = "clinicas")
    List<Paciente> findAll();
}

