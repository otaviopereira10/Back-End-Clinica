package com.projeto.services;

import com.projeto.entities.Clinica;
import com.projeto.entities.Paciente;
import com.projeto.repository.ClinicaRepository;
import com.projeto.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        return pacienteRepository.findByIdWithClinicas(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado!"));
    }

    @Transactional
    public Paciente cadastrarPaciente(Paciente paciente, Set<Long> clinicaIds) {
        if (clinicaIds.isEmpty()) {
            throw new RuntimeException("Erro: O paciente deve estar associado a pelo menos uma clínica.");
        }

        // Busca as clínicas e verifica se todas existem
        Set<Clinica> clinicas = clinicaRepository.findAllById(clinicaIds)
                .stream()
                .collect(Collectors.toSet());

        if (clinicas.size() != clinicaIds.size()) {
            throw new RuntimeException("Erro: Uma ou mais clínicas não foram encontradas.");
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

        // Atualiza as clínicas associadas
        Set<Clinica> clinicas = clinicaRepository.findAllById(clinicaIds)
                .stream()
                .collect(Collectors.toSet());

        if (clinicas.size() != clinicaIds.size()) {
            throw new RuntimeException("Erro: Uma ou mais clínicas não foram encontradas.");
        }

        pacienteExistente.setClinicas(clinicas);
        return pacienteRepository.save(pacienteExistente);
    }

    @Transactional
    public void deletarPaciente(Long id) {
        Paciente paciente = buscarPacientePorId(id);
        paciente.getClinicas().forEach(clinica -> clinica.getPacientes().remove(paciente));
        pacienteRepository.delete(paciente);
    }
}
