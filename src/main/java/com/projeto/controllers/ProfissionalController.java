package com.projeto.controllers;

import com.projeto.entities.Profissional;
import com.projeto.services.ProfissionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profissionais")
public class ProfissionalController {

    @Autowired
    private ProfissionalService profissionalService;

    // ✅ Criar novo profissional
    @PostMapping
    public ResponseEntity<?> cadastrarProfissional(@RequestBody Profissional profissional) {
        try {
            profissionalService.cadastrarProfissional(profissional);
            return ResponseEntity.status(HttpStatus.CREATED).body("Profissional cadastrado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: " + e.getMessage());
        }
    }

    // ✅ Listar todos os profissionais
    @GetMapping
    public ResponseEntity<List<Profissional>> listarProfissionais() {
        return ResponseEntity.ok(profissionalService.listarProfissionais());
    }

    // ✅ Buscar profissional por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarProfissionalPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(profissionalService.buscarProfissionalPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ✅ Atualizar profissional
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarProfissional(@PathVariable Long id, @RequestBody Profissional profissional) {
        try {
            profissionalService.atualizarProfissional(id, profissional);
            return ResponseEntity.ok("Profissional atualizado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ✅ Deletar profissional
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
