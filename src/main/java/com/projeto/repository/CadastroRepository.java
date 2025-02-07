package com.projeto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.projeto.entities.Cadastro_login;
import java.util.Optional;

@Repository
public interface CadastroRepository extends JpaRepository<Cadastro_login, Long> {
    Optional<Cadastro_login> findByUser(String user);
}
