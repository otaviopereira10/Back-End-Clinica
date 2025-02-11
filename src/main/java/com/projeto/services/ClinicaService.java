package com.projeto.services;

import com.projeto.entities.Clinica;
import com.projeto.repository.ClinicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClinicaService {

    @Autowired
    private ClinicaRepository clinicaRepository;

    public Clinica criarClinica(Clinica clinica) {
        return clinicaRepository.save(clinica);
    }

    public List<Clinica> listarClinicas() {
        return clinicaRepository.findAll();
    }

    public Clinica buscarClinicaPorId(Long id) {
        return clinicaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clínica não encontrada!"));
    }

    public Clinica atualizarClinica(Long id, Clinica clinicaAtualizada) {
        Clinica clinica = buscarClinicaPorId(id);
        clinica.setNome(clinicaAtualizada.getNome());
        clinica.setEndereco(clinicaAtualizada.getEndereco());
        return clinicaRepository.save(clinica);
    }

    public void deletarClinica(Long id) {
        clinicaRepository.deleteById(id);
    }
}
