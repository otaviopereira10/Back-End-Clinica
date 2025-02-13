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

    // ✅ Buscar todos os pacientes
    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    // ✅ Buscar paciente por ID
    public Paciente buscarPacientePorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado!"));
    }

    // ✅ Cadastrar paciente com clínicas associadas
    @Transactional
    public Paciente cadastrarPaciente(Paciente paciente, Set<Long> clinicaIds) {
        Set<Clinica> clinicas = clinicaRepository.findAllById(clinicaIds)
                .stream().collect(Collectors.toSet());

        if (clinicas.isEmpty() || clinicas.size() != clinicaIds.size()) {
            throw new RuntimeException("Erro: Uma ou mais clínicas não foram encontradas.");
        }

        paciente.setClinicas(clinicas);
        return pacienteRepository.save(paciente);
    }

    // ✅ Atualizar paciente e suas clínicas
    @Transactional
    public Paciente atualizarPaciente(Long id, Paciente pacienteAtualizado, Set<Long> clinicaIds) {
        Paciente pacienteExistente = buscarPacientePorId(id);

        pacienteExistente.setNome(pacienteAtualizado.getNome());
        pacienteExistente.setIdade(pacienteAtualizado.getIdade());
        pacienteExistente.setTelefone(pacienteAtualizado.getTelefone());
        pacienteExistente.setEndereco(pacienteAtualizado.getEndereco());

        Set<Clinica> clinicas = clinicaRepository.findAllById(clinicaIds)
                .stream().collect(Collectors.toSet());

        if (clinicas.isEmpty() || clinicas.size() != clinicaIds.size()) {
            throw new RuntimeException("Erro: Uma ou mais clínicas não foram encontradas.");
        }

        pacienteExistente.setClinicas(clinicas);
        return pacienteRepository.save(pacienteExistente);
    }

    // ✅ Deletar paciente e remover vínculo com clínicas
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
