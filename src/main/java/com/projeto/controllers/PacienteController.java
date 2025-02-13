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
            // Validando campos obrigatórios
            if (!payload.containsKey("nome") || !payload.containsKey("idade") ||
                !payload.containsKey("telefone") || !payload.containsKey("endereco") ||
                !payload.containsKey("clinicaIds")) {
                return ResponseEntity.badRequest().body("Erro: Todos os campos são obrigatórios!");
            }

            // Convertendo valores corretamente
            String nome = (String) payload.get("nome");
            int idade = ((Number) payload.get("idade")).intValue();  // ✅ Conversão segura
            String telefone = (String) payload.get("telefone");
            String endereco = (String) payload.get("endereco");

            Paciente paciente = new Paciente(nome, idade, telefone, endereco);

            // Convertendo lista de IDs de clínicas corretamente
            List<?> clinicaIdsList = (List<?>) payload.get("clinicaIds");
            Set<Long> clinicaIds = clinicaIdsList.stream()
                .map(id -> ((Number) id).longValue()) // ✅ Garante conversão correta para Long
                .collect(Collectors.toSet());

            Paciente novoPaciente = pacienteService.cadastrarPaciente(paciente, clinicaIds);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoPaciente);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao cadastrar paciente: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarPaciente(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        try {
            if (!payload.containsKey("nome") || !payload.containsKey("idade") ||
                !payload.containsKey("telefone") || !payload.containsKey("endereco") ||
                !payload.containsKey("clinicaIds")) {
                return ResponseEntity.badRequest().body("Erro: Todos os campos são obrigatórios!");
            }

            String nome = (String) payload.get("nome");
            int idade = ((Number) payload.get("idade")).intValue();
            String telefone = (String) payload.get("telefone");
            String endereco = (String) payload.get("endereco");

            Paciente paciente = new Paciente(nome, idade, telefone, endereco);

            List<?> clinicaIdsList = (List<?>) payload.get("clinicaIds");
            Set<Long> clinicaIds = clinicaIdsList.stream()
                .map(ids -> ((Number) id).longValue())
                .collect(Collectors.toSet());

            Paciente pacienteAtualizado = pacienteService.atualizarPaciente(id, paciente, clinicaIds);
            return ResponseEntity.ok(pacienteAtualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro ao atualizar paciente: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarPaciente(@PathVariable Long id) {
        try {
            pacienteService.deletarPaciente(id);
            return ResponseEntity.ok("Paciente removido com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro ao deletar paciente: " + e.getMessage());
        }
    }
}
