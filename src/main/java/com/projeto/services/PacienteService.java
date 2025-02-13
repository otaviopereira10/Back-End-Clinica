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

    // ✅ Buscar paciente por ID com clínicas associadas
    public Paciente buscarPacientePorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Erro: Paciente não encontrado!"));
    }

    // ✅ Cadastrar paciente e associar clínicas
    @Transactional
    public Paciente cadastrarPaciente(Paciente paciente, Set<Long> clinicaIds) {
        if (clinicaIds == null || clinicaIds.isEmpty()) {
            throw new RuntimeException("Erro: O paciente deve estar associado a pelo menos uma clínica.");
        }

        Set<Clinica> clinicas = new HashSet<>(clinicaRepository.findAllById(clinicaIds));

        if (clinicas.isEmpty() || clinicas.size() != clinicaIds.size()) {
            throw new RuntimeException("Erro: Uma ou mais clínicas informadas não existem.");
        }

        paciente.setClinicas(clinicas);
        Paciente novoPaciente = pacienteRepository.save(paciente);

        // ✅ Atualiza a relação ManyToMany garantindo que a clínica também guarde o paciente
        for (Clinica clinica : clinicas) {
            clinica.getPacientes().add(novoPaciente);
            clinicaRepository.save(clinica);
        }

        return novoPaciente;
    }

    // ✅ Atualizar paciente e suas clínicas
    @Transactional
    public Paciente atualizarPaciente(Long id, Paciente pacienteAtualizado, Set<Long> clinicaIds) {
        Paciente pacienteExistente = buscarPacientePorId(id);

        pacienteExistente.setNome(pacienteAtualizado.getNome());
        pacienteExistente.setIdade(pacienteAtualizado.getIdade());
        pacienteExistente.setTelefone(pacienteAtualizado.getTelefone());
        pacienteExistente.setEndereco(pacienteAtualizado.getEndereco());

        Set<Clinica> clinicas = new HashSet<>(clinicaRepository.findAllById(clinicaIds));

        if (clinicas.isEmpty() || clinicas.size() != clinicaIds.size()) {
            throw new RuntimeException("Erro: Uma ou mais clínicas informadas não existem.");
        }

        pacienteExistente.setClinicas(clinicas);
        return pacienteRepository.save(pacienteExistente);
    }

    // ✅ Deletar paciente e remover vínculos
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
