package com.projeto.services;

import com.projeto.entities.Clinica;
import com.projeto.entities.Paciente;
import com.projeto.repository.ClinicaRepository;
import com.projeto.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ClinicaRepository clinicaRepository;

    @Transactional
    public Paciente cadastrarPaciente(Paciente paciente, Set<Long> clinicaIds) {
        if (clinicaIds.isEmpty()) {
            throw new RuntimeException("Erro: O paciente deve estar associado a pelo menos uma clínica.");
        }

        // ✅ Carrega as clínicas existentes
        Set<Clinica> clinicas = new HashSet<>();
        for (Long clinicaId : clinicaIds) {
            Clinica clinica = clinicaRepository.findById(clinicaId)
                    .orElseThrow(() -> new RuntimeException("Erro: Clínica com ID " + clinicaId + " não encontrada."));
            clinicas.add(clinica);
        }

        // ✅ Salva o paciente e associa as clínicas
        paciente.setClinicas(clinicas);
        return pacienteRepository.save(paciente);
    }

    @Transactional(readOnly = true)
    public List<Paciente> listarPacientes() {
        List<Paciente> pacientes = pacienteRepository.findAll();

        // ✅ Força o carregamento das clínicas associadas (Lazy Loading)
        for (Paciente p : pacientes) {
            p.getClinicas().size(); // Hibernate carrega a lista de clínicas
        }

        return pacientes;
    }

    @Transactional(readOnly = true)
    public Paciente buscarPacientePorId(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado!"));

        // ✅ Força o carregamento das clínicas associadas
        paciente.getClinicas().size();
        
        return paciente;
    }

    @Transactional
    public Paciente atualizarPaciente(Long id, Paciente pacienteAtualizado, Set<Long> clinicaIds) {
        Paciente pacienteExistente = buscarPacientePorId(id);

        pacienteExistente.setNome(pacienteAtualizado.getNome());
        pacienteExistente.setIdade(pacienteAtualizado.getIdade());
        pacienteExistente.setTelefone(pacienteAtualizado.getTelefone());
        pacienteExistente.setEndereco(pacienteAtualizado.getEndereco());

        // ✅ Atualiza as clínicas associadas
        Set<Clinica> clinicas = new HashSet<>();
        for (Long clinicaId : clinicaIds) {
            Clinica clinica = clinicaRepository.findById(clinicaId)
                    .orElseThrow(() -> new RuntimeException("Erro: Clínica com ID " + clinicaId + " não encontrada."));
            clinicas.add(clinica);
        }

        pacienteExistente.setClinicas(clinicas);

        return pacienteRepository.save(pacienteExistente);
    }

    @Transactional
    public void deletarPaciente(Long id) {
        Paciente paciente = buscarPacientePorId(id);

        // ✅ Remove a relação ManyToMany antes da exclusão
        for (Clinica clinica : paciente.getClinicas()) {
            clinica.getPacientes().remove(paciente);
            clinicaRepository.save(clinica);
        }

        pacienteRepository.delete(paciente);
    }
}
