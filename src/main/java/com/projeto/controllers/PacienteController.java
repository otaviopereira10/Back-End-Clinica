package com.projeto.controllers;

import com.projeto.entities.Paciente;
import com.projeto.services.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public ResponseEntity<List<Paciente>> listarPacientes() {
        return ResponseEntity.ok(pacienteService.listarPacientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPacientePorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(pacienteService.buscarPacientePorId(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> cadastrarPaciente(@RequestBody Map<String, Object> payload) {
        try {
            Paciente paciente = new Paciente(
                (String) payload.get("nome"),
                ((Number) payload.get("idade")).intValue(),
                (String) payload.get("telefone"),
                (String) payload.get("endereco")
            );

            Set<Long> clinicaIds = ((List<?>) payload.get("clinicaIds")).stream()
                .map(id -> ((Number) id).longValue())
                .collect(Collectors.toSet());

            return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.cadastrarPaciente(paciente, clinicaIds));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao cadastrar paciente: " + e.getMessage());
        }
    }
}
