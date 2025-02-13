package com.projeto.controllers;

import com.projeto.entities.Profissional;
import com.projeto.services.ProfissionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/profissionais")
public class ProfissionalController {

    @Autowired
    private ProfissionalService profissionalService;

    @PostMapping
    public ResponseEntity<?> cadastrarProfissional(@RequestBody Map<String, Object> payload) {
        try {
            if (!payload.containsKey("nome") || !payload.containsKey("especialidade") ||
                !payload.containsKey("registro") || !payload.containsKey("telefone") ||
                !payload.containsKey("clinicaIds")) {
                return ResponseEntity.badRequest().body("Erro: Todos os campos são obrigatórios!");
            }

            String nome = (String) payload.get("nome");
            String especialidade = (String) payload.get("especialidade");
            String registro = (String) payload.get("registro");
            String telefone = (String) payload.get("telefone");

            Profissional profissional = new Profissional(nome, especialidade, registro, telefone);

            List<?> clinicaIdsList = (List<?>) payload.get("clinicaIds");
            Set<Long> clinicaIds = clinicaIdsList.stream()
                .map(id -> ((Number) id).longValue()) // ✅ Corrigido
                .collect(Collectors.toSet());

            profissionalService.cadastrarProfissional(profissional, clinicaIds);
            return ResponseEntity.status(HttpStatus.CREATED).body("Profissional cadastrado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao cadastrar profissional: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Profissional>> listarProfissionais() {
        return ResponseEntity.ok(profissionalService.listarProfissionais());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarProfissionalPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(profissionalService.buscarProfissionalPorId(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarProfissional(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        try {
            if (!payload.containsKey("nome") || !payload.containsKey("especialidade") ||
                !payload.containsKey("registro") || !payload.containsKey("telefone") ||
                !payload.containsKey("clinicaIds")) {
                return ResponseEntity.badRequest().body("Erro: Todos os campos são obrigatórios!");
            }

            String nome = (String) payload.get("nome");
            String especialidade = (String) payload.get("especialidade");
            String registro = (String) payload.get("registro");
            String telefone = (String) payload.get("telefone");

            Profissional profissional = new Profissional(nome, especialidade, registro, telefone);

            List<?> clinicaIdsList = (List<?>) payload.get("clinicaIds");
            Set<Long> clinicaIds = clinicaIdsList.stream()
                .map(ids -> ((Number) id).longValue())
                .collect(Collectors.toSet());

            profissionalService.atualizarProfissional(id, profissional, clinicaIds);
            return ResponseEntity.ok("Profissional atualizado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro ao atualizar profissional: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarProfissional(@PathVariable Long id) {
        try {
            profissionalService.deletarProfissional(id);
            return ResponseEntity.ok("Profissional removido com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro ao deletar profissional: " + e.getMessage());
        }
    }
}
