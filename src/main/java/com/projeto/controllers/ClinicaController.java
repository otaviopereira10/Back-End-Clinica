package com.projeto.controllers;

import com.projeto.entities.Clinica;
import com.projeto.services.ClinicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/clinicas")
public class ClinicaController {

    @Autowired
    private ClinicaService clinicaService;

    @PostMapping
    public ResponseEntity<Clinica> criarClinica(@RequestBody Clinica clinica) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clinicaService.criarClinica(clinica));
    }

    @GetMapping
    public ResponseEntity<List<Clinica>> listarClinicas() {
        return ResponseEntity.ok(clinicaService.listarClinicas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Clinica> buscarClinicaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clinicaService.buscarClinicaPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Clinica> atualizarClinica(@PathVariable Long id, @RequestBody Clinica clinica) {
        return ResponseEntity.ok(clinicaService.atualizarClinica(id, clinica));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarClinica(@PathVariable Long id) {
        clinicaService.deletarClinica(id);
        return ResponseEntity.ok("Clínica removida com sucesso!");
    }
}
