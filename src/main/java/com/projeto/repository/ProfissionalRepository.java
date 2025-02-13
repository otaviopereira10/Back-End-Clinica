package com.projeto.repository;

import com.projeto.entities.Profissional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {

    // ✅ Busca profissional pelo registro (corrigido)
    Optional<Profissional> findByRegistro(String registro);

    // ✅ Garante que ao buscar todos os profissionais, as clínicas também sejam carregadas
    @EntityGraph(attributePaths = "clinicas") // 🔥 Garante que as clínicas sejam carregadas no GET
    List<Profissional> findAll();

    // ✅ Busca profissional por ID e carrega as clínicas associadas
    @Query("SELECT p FROM Profissional p LEFT JOIN FETCH p.clinicas WHERE p.id = :id")
    Optional<Profissional> findByIdWithClinicas(Long id);
}
