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

    // ✅ Cadastrar Profissional com Clínicas Associadas
    @PostMapping
    public ResponseEntity<?> cadastrarProfissional(@RequestBody Map<String, Object> payload) {
        try {
            // Captura os dados do JSON corretamente
            String nome = (String) payload.get("nome");
            String especialidade = (String) payload.get("especialidade");
            String registro = (String) payload.get("registro");
            String telefone = (String) payload.get("telefone");

            Profissional profissional = new Profissional(nome, especialidade, registro, telefone);

            List<?> clinicaIdsList = (List<?>) payload.get("clinicaIds");
            Set<Long> clinicaIds = clinicaIdsList.stream()
                .map(id -> ((Number) id).longValue())
                .collect(Collectors.toSet());

            Profissional novoProfissional = profissionalService.cadastrarProfissional(profissional, clinicaIds);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoProfissional);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao cadastrar profissional: " + e.getMessage());
        }
    }

    // ✅ Listar Todos os Profissionais
    @GetMapping
    public ResponseEntity<List<Profissional>> listarProfissionais() {
        return ResponseEntity.ok(profissionalService.listarProfissionais());
    }

    // ✅ Buscar Profissional por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarProfissionalPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(profissionalService.buscarProfissionalPorId(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: " + e.getMessage());
        }
    }

    // ✅ Atualizar Profissional com Clínicas Associadas
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarProfissional(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        try {
            String nome = (String) payload.get("nome");
            String especialidade = (String) payload.get("especialidade");
            String registro = (String) payload.get("registro");
            String telefone = (String) payload.get("telefone");

            Profissional profissionalAtualizado = new Profissional(nome, especialidade, registro, telefone);

            List<?> clinicaIdsList = (List<?>) payload.get("clinicaIds");
            Set<Long> clinicaIds = clinicaIdsList.stream()
                .map(ids -> ((Number) id).longValue())
                .collect(Collectors.toSet());

            Profissional profissional = profissionalService.atualizarProfissional(id, profissionalAtualizado, clinicaIds);
            return ResponseEntity.ok(profissional);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro ao atualizar profissional: " + e.getMessage());
        }
    }

    // ✅ Deletar Profissional
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
