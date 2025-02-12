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
import java.util.Optional;
import java.util.Set;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ClinicaRepository clinicaRepository;

    // ✅ Buscar todos os pacientes com clínicas associadas
    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    // ✅ Buscar um paciente por ID garantindo que suas clínicas sejam carregadas
    public Paciente buscarPacientePorId(Long id) {
        Optional<Paciente> paciente = pacienteRepository.findByIdWithClinicas(id);
        return paciente.orElseThrow(() -> new RuntimeException("Paciente não encontrado!"));
    }

    @Transactional
    public Paciente cadastrarPaciente(Paciente paciente, Set<Long> clinicaIds) {
        if (clinicaIds.isEmpty()) {
            throw new RuntimeException("Erro: O paciente deve estar associado a pelo menos uma clínica.");
        }

        Set<Clinica> clinicas = new HashSet<>();
        for (Long clinicaId : clinicaIds) {
            Clinica clinica = clinicaRepository.findById(clinicaId)
                    .orElseThrow(() -> new RuntimeException("Erro: Clínica com ID " + clinicaId + " não encontrada."));
            clinicas.add(clinica);
        }

        paciente.setClinicas(clinicas);
        return pacienteRepository.save(paciente);
    }

    @Transactional
    public Paciente atualizarPaciente(Long id, Paciente pacienteAtualizado, Set<Long> clinicaIds) {
        Paciente pacienteExistente = buscarPacientePorId(id);

        pacienteExistente.setNome(pacienteAtualizado.getNome());
        pacienteExistente.setIdade(pacienteAtualizado.getIdade());
        pacienteExistente.setTelefone(pacienteAtualizado.getTelefone());
        pacienteExistente.setEndereco(pacienteAtualizado.getEndereco());

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

        for (Clinica clinica : paciente.getClinicas()) {
            clinica.getPacientes().remove(paciente);
            clinicaRepository.save(clinica);
        }

        pacienteRepository.delete(paciente);
    }
}
