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

    // ✅ CADASTRAR PACIENTE
    @PostMapping
    public ResponseEntity<?> cadastrarPaciente(@RequestBody Map<String, Object> payload) {
        try {
            // 🔍 Validação dos campos obrigatórios
            if (!payload.containsKey("nome") || !payload.containsKey("idade") ||
                !payload.containsKey("telefone") || !payload.containsKey("endereco") ||
                !payload.containsKey("clinicaIds")) {
                return ResponseEntity.badRequest().body("Erro: Todos os campos são obrigatórios!");
            }

            Paciente paciente = new Paciente(
                (String) payload.get("nome"),
                (int) payload.get("idade"),
                (String) payload.get("telefone"),
                (String) payload.get("endereco")
            );

            // 🔍 Convertendo lista de clinicas para Set<Long>
            List<?> clinicaIdsList = (List<?>) payload.get("clinicaIds");
            if (clinicaIdsList == null || clinicaIdsList.isEmpty()) {
                return ResponseEntity.badRequest().body("Erro: O paciente deve estar associado a pelo menos uma clínica.");
            }
            Set<Long> clinicaIds = clinicaIdsList.stream()
                .map(id -> Long.valueOf(id.toString()))
                .collect(Collectors.toSet());

            Paciente novoPaciente = pacienteService.cadastrarPaciente(paciente, clinicaIds);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoPaciente);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao cadastrar paciente: " + e.getMessage());
        }
    }

    // ✅ LISTAR TODOS OS PACIENTES (Incluindo Clínicas)
    @GetMapping
    public ResponseEntity<List<Paciente>> listarPacientes() {
        try {
            return ResponseEntity.ok(pacienteService.listarPacientesComClinicas());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // ✅ BUSCAR PACIENTE POR ID (Incluindo Clínicas)
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPacientePorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(pacienteService.buscarPacientePorIdComClinicas(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: " + e.getMessage());
        }
    }

    // ✅ ATUALIZAR PACIENTE
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarPaciente(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        try {
            // 🔍 Validação dos campos obrigatórios
            if (!payload.containsKey("nome") || !payload.containsKey("idade") ||
                !payload.containsKey("telefone") || !payload.containsKey("endereco") ||
                !payload.containsKey("clinicaIds")) {
                return ResponseEntity.badRequest().body("Erro: Todos os campos são obrigatórios!");
            }

            Paciente paciente = new Paciente(
                (String) payload.get("nome"),
                (int) payload.get("idade"),
                (String) payload.get("telefone"),
                (String) payload.get("endereco")
            );

            // 🔍 Convertendo lista de clinicas para Set<Long>
            List<?> clinicaIdsList = (List<?>) payload.get("clinicaIds");
            if (clinicaIdsList == null || clinicaIdsList.isEmpty()) {
                return ResponseEntity.badRequest().body("Erro: O paciente deve estar associado a pelo menos uma clínica.");
            }
            Set<Long> clinicaIds = clinicaIdsList.stream()
                .map(cId -> Long.valueOf(cId.toString()))
                .collect(Collectors.toSet());

            Paciente pacienteAtualizado = pacienteService.atualizarPaciente(id, paciente, clinicaIds);
            return ResponseEntity.ok(pacienteAtualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro ao atualizar paciente: " + e.getMessage());
        }
    }

    // ✅ DELETAR PACIENTE
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
