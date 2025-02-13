package com.projeto.controllers;

import com.projeto.entities.Consulta;
import com.projeto.services.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: " + e.getMessage());
        }
    }

    // ✅ Cadastrar consulta
    @PostMapping("/{pacienteId}/{profissionalId}/{clinicaId}")
    public ResponseEntity<?> cadastrarConsulta(
            @PathVariable Long pacienteId,
            @PathVariable Long profissionalId,
            @PathVariable Long clinicaId,
            @RequestBody Consulta consulta) {
        try {
            Consulta novaConsulta = consultaService.cadastrarConsulta(pacienteId, profissionalId, clinicaId, consulta);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaConsulta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao cadastrar consulta: " + e.getMessage());
        }
    }

    // ✅ Atualizar consulta
    @PutMapping("/{id}/{pacienteId}/{profissionalId}/{clinicaId}")
    public ResponseEntity<?> atualizarConsulta(
            @PathVariable Long id,
            @PathVariable Long pacienteId,
            @PathVariable Long profissionalId,
            @PathVariable Long clinicaId,
            @RequestBody Consulta consultaAtualizada) {
        try {
            return ResponseEntity.ok(consultaService.atualizarConsulta(id, pacienteId, profissionalId, clinicaId, consultaAtualizada));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro ao atualizar consulta: " + e.getMessage());
        }
    }

    // ✅ Deletar consulta
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarConsulta(@PathVariable Long id) {
        consultaService.deletarConsulta(id);
        return ResponseEntity.ok("Consulta removida com sucesso!");
    }
}
