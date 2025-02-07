package com.projeto.controllers;

import com.projeto.entities.Consulta;
import com.projeto.services.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    // ✅ Criar nova consulta
    @PostMapping("/{pacienteId}/{profissionalId}")
    public ResponseEntity<?> criarConsulta(
        @PathVariable Long pacienteId,
        @PathVariable Long profissionalId,
        @RequestBody Consulta consulta
    ) {
        try {
            consultaService.criarConsulta(pacienteId, profissionalId, consulta);
            return ResponseEntity.status(HttpStatus.CREATED).body("Consulta agendada com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: " + e.getMessage());
        }
    }

    // ✅ Listar todas as consultas
    @GetMapping
    public ResponseEntity<List<Consulta>> listarConsultas() {
        return ResponseEntity.ok(consultaService.listarConsultas());
    }

    // ✅ Buscar consulta por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarConsultaPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(consultaService.buscarConsultaPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: " + e.getMessage());
        }
    }

    // ✅ Atualizar consulta (CORRIGIDO PARA USAR pacienteId e profissionalId)
    @PutMapping("/{id}/{pacienteId}/{profissionalId}")
    public ResponseEntity<?> atualizarConsulta(
        @PathVariable Long id,
        @PathVariable Long pacienteId,
        @PathVariable Long profissionalId,
        @RequestBody Consulta novaConsulta
    ) {
        try {
            consultaService.atualizarConsulta(id, pacienteId, profissionalId, novaConsulta);
            return ResponseEntity.ok("Consulta atualizada com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: " + e.getMessage());
        }
    }

    // ✅ Excluir consulta
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirConsulta(@PathVariable Long id) {
        try {
            consultaService.excluirConsulta(id);
            return ResponseEntity.ok("Consulta excluída com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: " + e.getMessage());
        }
    }
}
