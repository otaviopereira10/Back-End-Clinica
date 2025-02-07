package com.projeto.controllers;

import com.projeto.entities.Paciente;
import com.projeto.services.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    // ✅ Criar Paciente
    @PostMapping
    public ResponseEntity<Paciente> cadastrarPaciente(@RequestBody Paciente paciente) {
        Paciente novoPaciente = pacienteService.cadastrarPaciente(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPaciente);
    }

    // ✅ Listar Pacientes
    @GetMapping
    public ResponseEntity<List<Paciente>> listarPacientes() {
        List<Paciente> pacientes = pacienteService.listarPacientes();
        return ResponseEntity.ok(pacientes);
    }

    // ✅ Buscar Paciente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> buscarPacientePorId(@PathVariable Long id) {
        Paciente paciente = pacienteService.buscarPacientePorId(id);
        return ResponseEntity.ok(paciente);
    }

    // ✅ Atualizar Paciente
    @PutMapping("/{id}")
    public ResponseEntity<Paciente> atualizarPaciente(@PathVariable Long id, @RequestBody Paciente pacienteAtualizado) {
        Paciente paciente = pacienteService.atualizarPaciente(id, pacienteAtualizado);
        return ResponseEntity.ok(paciente);
    }

    // ✅ Deletar Paciente
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarPaciente(@PathVariable Long id) {
        pacienteService.deletarPaciente(id);
        return ResponseEntity.ok("Paciente removido com sucesso!");
    }
}
