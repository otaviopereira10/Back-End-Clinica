package com.projeto.services;

import com.projeto.entities.Consulta;
import com.projeto.entities.Paciente;
import com.projeto.entities.Profissional;
import com.projeto.repository.ConsultaRepository;
import com.projeto.repository.PacienteRepository;
import com.projeto.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final ProfissionalRepository profissionalRepository;

    @Autowired
    public ConsultaService(ConsultaRepository consultaRepository, PacienteRepository pacienteRepository, ProfissionalRepository profissionalRepository) {
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;
        this.profissionalRepository = profissionalRepository;
    }

    // ✅ Criar uma nova consulta
    public void criarConsulta(Long pacienteId, Long profissionalId, Consulta consulta) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado!"));
        Profissional profissional = profissionalRepository.findById(profissionalId)
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado!"));

        consulta.setPaciente(paciente);
        consulta.setProfissional(profissional);
        consultaRepository.save(consulta);
    }

    // ✅ Listar todas as consultas
    public List<Consulta> listarConsultas() {
        return consultaRepository.findAll();
    }

    // ✅ Buscar consulta por ID
    public Consulta buscarConsultaPorId(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada!"));
    }

    // ✅ Atualizar consulta (CORRIGIDO PARA SALVAR ALTERAÇÕES NO BANCO)
    public void atualizarConsulta(Long id, Long pacienteId, Long profissionalId, Consulta novaConsulta) {
        Consulta consultaExistente = buscarConsultaPorId(id);

        // Atualiza data e hora
        if (novaConsulta.getData() != null) {
            consultaExistente.setData(novaConsulta.getData());
        }
        if (novaConsulta.getHora() != null) {
            consultaExistente.setHora(novaConsulta.getHora());
        }

        // Atualiza paciente se fornecido
        if (pacienteId != null) {
            Paciente paciente = pacienteRepository.findById(pacienteId)
                    .orElseThrow(() -> new RuntimeException("Paciente não encontrado!"));
            consultaExistente.setPaciente(paciente);
        }

        // Atualiza profissional se fornecido
        if (profissionalId != null) {
            Profissional profissional = profissionalRepository.findById(profissionalId)
                    .orElseThrow(() -> new RuntimeException("Profissional não encontrado!"));
            consultaExistente.setProfissional(profissional);
        }

        // Salva no banco de dados
        consultaRepository.save(consultaExistente);
    }

    // ✅ Excluir consulta
    public void excluirConsulta(Long id) {
        if (!consultaRepository.existsById(id)) {
            throw new RuntimeException("Consulta não encontrada!");
        }
        consultaRepository.deleteById(id);
    }
}
