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

    @Transactional
    public Paciente cadastrarPaciente(Paciente paciente, Set<Long> clinicaIds) {
        if (clinicaIds.isEmpty()) {
            throw new RuntimeException("Erro: O paciente deve estar associado a pelo menos uma clínica.");
        }

        // Busca todas as clínicas no banco
        Set<Clinica> clinicas = clinicaRepository.findAllById(clinicaIds).stream().collect(Collectors.toSet());

        if (clinicas.isEmpty() || clinicas.size() != clinicaIds.size()) {
            throw new RuntimeException("Erro: Uma ou mais clínicas informadas não existem.");
        }

        // Salva primeiro o paciente sem clínicas
        Paciente novoPaciente = pacienteRepository.save(paciente);

        // Associa clínicas ao paciente e salva novamente
        novoPaciente.setClinicas(clinicas);
        return pacienteRepository.save(novoPaciente);
    }

    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    public Paciente buscarPacientePorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado!"));
    }

    @Transactional
    public Paciente atualizarPaciente(Long id, Paciente pacienteAtualizado, Set<Long> clinicaIds) {
        Paciente pacienteExistente = buscarPacientePorId(id);

        pacienteExistente.setNome(pacienteAtualizado.getNome());
        pacienteExistente.setIdade(pacienteAtualizado.getIdade());
        pacienteExistente.setTelefone(pacienteAtualizado.getTelefone());
        pacienteExistente.setEndereco(pacienteAtualizado.getEndereco());

        // Verifica se as clínicas existem antes de atualizar
        Set<Clinica> clinicas = clinicaRepository.findAllById(clinicaIds).stream().collect(Collectors.toSet());

        if (clinicas.isEmpty() || clinicas.size() != clinicaIds.size()) {
            throw new RuntimeException("Erro: Uma ou mais clínicas informadas não existem.");
        }

        pacienteExistente.setClinicas(clinicas);

        return pacienteRepository.save(pacienteExistente);
    }

    @Transactional
    public void deletarPaciente(Long id) {
        Paciente paciente = buscarPacientePorId(id);

        // Remove a relação ManyToMany antes de excluir o paciente
        for (Clinica clinica : paciente.getClinicas()) {
            clinica.getPacientes().remove(paciente);
            clinicaRepository.save(clinica);
        }

        pacienteRepository.delete(paciente);
    }
}
