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

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @PostMapping
    public ResponseEntity<?> cadastrarPaciente(@RequestBody Map<String, Object> payload) {
        try {
            Paciente paciente = new Paciente(
                (String) payload.get("nome"),
                (int) payload.get("idade"),
                (String) payload.get("telefone"),
                (String) payload.get("endereco")
            );

            Set<Long> clinicaIds = ((List<Integer>) payload.get("clinicaIds")).stream()
                .map(Long::valueOf)
                .collect(java.util.stream.Collectors.toSet());

            Paciente novoPaciente = pacienteService.cadastrarPaciente(paciente, clinicaIds);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoPaciente);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Paciente>> listarPacientes() {
        return ResponseEntity.ok(pacienteService.listarPacientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPacientePorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(pacienteService.buscarPacientePorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarPaciente(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        try {
            Paciente paciente = new Paciente(
                (String) payload.get("nome"),
                (int) payload.get("idade"),
                (String) payload.get("telefone"),
                (String) payload.get("endereco")
            );

            Set<Long> clinicaIds = ((List<Integer>) payload.get("clinicaIds")).stream()
                .map(Long::valueOf)
                .collect(java.util.stream.Collectors.toSet());

            Paciente pacienteAtualizado = pacienteService.atualizarPaciente(id, paciente, clinicaIds);
            return ResponseEntity.ok(pacienteAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarPaciente(@PathVariable Long id) {
        try {
            pacienteService.deletarPaciente(id);
            return ResponseEntity.ok("Paciente removido com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
