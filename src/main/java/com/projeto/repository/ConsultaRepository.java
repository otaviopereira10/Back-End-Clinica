package com.projeto.repository;

import com.projeto.entities.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    // ✅ Retorna todas as consultas com dados do Paciente, Profissional e Clínica
    @Query("SELECT c FROM Consulta c JOIN FETCH c.paciente JOIN FETCH c.profissional JOIN FETCH c.clinica")
    List<Consulta> findAllWithRelations();
}
