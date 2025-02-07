package com.projeto.services;

import com.projeto.entities.Paciente;
import com.projeto.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    // ✅ Criar Paciente
    public Paciente cadastrarPaciente(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    // ✅ Listar todos os pacientes
    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    // ✅ Buscar paciente por ID
    public Paciente buscarPacientePorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado!"));
    }

    // ✅ Atualizar paciente
    public Paciente atualizarPaciente(Long id, Paciente pacienteAtualizado) {
        Paciente pacienteExistente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado!"));

        pacienteExistente.setNome(pacienteAtualizado.getNome());
        pacienteExistente.setIdade(pacienteAtualizado.getIdade());
        pacienteExistente.setTelefone(pacienteAtualizado.getTelefone());
        pacienteExistente.setEndereco(pacienteAtualizado.getEndereco());

        return pacienteRepository.save(pacienteExistente);
    }

    // ✅ Deletar paciente
    public void deletarPaciente(Long id) {
        pacienteRepository.deleteById(id);
    }
}
