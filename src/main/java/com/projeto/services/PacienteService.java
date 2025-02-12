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

    // ✅ CADASTRAR PACIENTE (Agora garante que a relação Paciente-Clínica seja mantida)
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

        Paciente novoPaciente = pacienteRepository.save(paciente);
        novoPaciente.setClinicas(clinicas);
        return pacienteRepository.save(novoPaciente);
    }

    // ✅ LISTAR PACIENTES COM AS CLÍNICAS (Corrigido para retornar a lista corretamente)
    public List<Paciente> listarPacientesComClinicas() {
        return pacienteRepository.findAll(); // Já usa @EntityGraph no Repository para carregar as clínicas
    }

    // ✅ BUSCAR PACIENTE POR ID COM CLÍNICAS
    public Paciente buscarPacientePorIdComClinicas(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado!"));
    }

    // ✅ ATUALIZAR PACIENTE (Mantém a relação com as clínicas)
    @Transactional
    public Paciente atualizarPaciente(Long id, Paciente pacienteAtualizado, Set<Long> clinicaIds) {
        Paciente pacienteExistente = buscarPacientePorIdComClinicas(id);

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

    // ✅ DELETAR PACIENTE (Remove a relação com as clínicas antes de excluir)
    @Transactional
    public void deletarPaciente(Long id) {
        Paciente paciente = buscarPacientePorIdComClinicas(id);

        for (Clinica clinica : paciente.getClinicas()) {
            clinica.getPacientes().remove(paciente);
            clinicaRepository.save(clinica);
        }

        pacienteRepository.delete(paciente);
    }
}
