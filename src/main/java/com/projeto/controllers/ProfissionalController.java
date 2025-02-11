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

@RestController
@RequestMapping("/api/profissionais")
public class ProfissionalController {

    @Autowired
    private ProfissionalService profissionalService;

    @PostMapping
    public ResponseEntity<?> cadastrarProfissional(@RequestBody Map<String, Object> payload) {
        try {
            Profissional profissional = new Profissional(
                (String) payload.get("nome"),
                (String) payload.get("especialidade"),
                (String) payload.get("registro"),
                (String) payload.get("telefone")
            );

            Set<Long> clinicaIds = ((List<Integer>) payload.get("clinicaIds")).stream()
                .map(Long::valueOf)
                .collect(java.util.stream.Collectors.toSet());

            profissionalService.cadastrarProfissional(profissional, clinicaIds);
            return ResponseEntity.status(HttpStatus.CREATED).body("Profissional cadastrado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: " + e.getMessage());
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
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarProfissional(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        try {
            Profissional profissional = new Profissional(
                (String) payload.get("nome"),
                (String) payload.get("especialidade"),
                (String) payload.get("registro"),
                (String) payload.get("telefone")
            );

            Set<Long> clinicaIds = ((List<Integer>) payload.get("clinicaIds")).stream()
                .map(Long::valueOf)
                .collect(java.util.stream.Collectors.toSet());

            profissionalService.atualizarProfissional(id, profissional, clinicaIds);
            return ResponseEntity.ok("Profissional atualizado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarProfissional(@PathVariable Long id) {
        try {
            profissionalService.deletarProfissional(id);
            return ResponseEntity.ok("Profissional removido com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
