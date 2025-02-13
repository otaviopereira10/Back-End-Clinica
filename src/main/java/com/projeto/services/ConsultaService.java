package com.projeto.services;

import com.projeto.entities.Clinica;
import com.projeto.entities.Consulta;
import com.projeto.entities.Paciente;
import com.projeto.entities.Profissional;
import com.projeto.repository.ClinicaRepository;
import com.projeto.repository.ConsultaRepository;
import com.projeto.repository.PacienteRepository;
import com.projeto.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private ClinicaRepository clinicaRepository;

    // ✅ Listar todas as consultas com seus relacionamentos carregados
    public List<Consulta> listarConsultas() {
        return consultaRepository.findAllWithRelations();
    }

    // ✅ Buscar uma consulta pelo ID
    public Consulta buscarConsultaPorId(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Erro: Consulta não encontrada!"));
    }

    // ✅ Cadastrar uma consulta associada a Paciente, Profissional e Clínica
    @Transactional
    public Consulta cadastrarConsulta(Long pacienteId, Long profissionalId, Long clinicaId, Consulta consulta) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Erro: Paciente não encontrado!"));

        Profissional profissional = profissionalRepository.findById(profissionalId)
                .orElseThrow(() -> new RuntimeException("Erro: Profissional não encontrado!"));

        Clinica clinica = clinicaRepository.findById(clinicaId)
                .orElseThrow(() -> new RuntimeException("Erro: Clínica não encontrada!"));

        consulta.setPaciente(paciente);
        consulta.setProfissional(profissional);
        consulta.setClinica(clinica);
        return consultaRepository.save(consulta);
    }

    // ✅ Atualizar uma consulta
    @Transactional
    public Consulta atualizarConsulta(Long id, Long pacienteId, Long profissionalId, Long clinicaId, Consulta consultaAtualizada) {
        Consulta consultaExistente = buscarConsultaPorId(id);

        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Erro: Paciente não encontrado!"));

        Profissional profissional = profissionalRepository.findById(profissionalId)
                .orElseThrow(() -> new RuntimeException("Erro: Profissional não encontrado!"));

        Clinica clinica = clinicaRepository.findById(clinicaId)
                .orElseThrow(() -> new RuntimeException("Erro: Clínica não encontrada!"));

        consultaExistente.setPaciente(paciente);
        consultaExistente.setProfissional(profissional);
        consultaExistente.setClinica(clinica);
        consultaExistente.setData(consultaAtualizada.getData());
        consultaExistente.setHora(consultaAtualizada.getHora());

        return consultaRepository.save(consultaExistente);
    }

    // ✅ Deletar consulta
    @Transactional
    public void deletarConsulta(Long id) {
        consultaRepository.deleteById(id);
    }
}
