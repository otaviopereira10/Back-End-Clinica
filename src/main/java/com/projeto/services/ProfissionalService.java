package com.projeto.services;

import com.projeto.entities.Clinica;
import com.projeto.entities.Profissional;
import com.projeto.repository.ClinicaRepository;
import com.projeto.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ProfissionalService {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private ClinicaRepository clinicaRepository;

    public void cadastrarProfissional(Profissional profissional, Set<Long> clinicaIds) {
        if (profissionalRepository.findByRegistro(profissional.getRegistro()).isPresent()) {
            throw new RuntimeException("Registro já cadastrado!");
        }

        Set<Clinica> clinicas = clinicaRepository.findAllById(clinicaIds).stream().collect(java.util.stream.Collectors.toSet());
        profissional.setClinicas(clinicas);
        
        profissionalRepository.save(profissional);
    }

    public List<Profissional> listarProfissionais() {
        return profissionalRepository.findAll();
    }

    public Profissional buscarProfissionalPorId(Long id) {
        return profissionalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado!"));
    }

    public void atualizarProfissional(Long id, Profissional profissionalAtualizado, Set<Long> clinicaIds) {
        Profissional profissionalExistente = buscarProfissionalPorId(id);
        
        profissionalExistente.setNome(profissionalAtualizado.getNome());
        profissionalExistente.setEspecialidade(profissionalAtualizado.getEspecialidade());
        profissionalExistente.setRegistro(profissionalAtualizado.getRegistro());
        profissionalExistente.setTelefone(profissionalAtualizado.getTelefone());

        Set<Clinica> clinicas = clinicaRepository.findAllById(clinicaIds).stream().collect(java.util.stream.Collectors.toSet());
        profissionalExistente.setClinicas(clinicas);

        profissionalRepository.save(profissionalExistente);
    }

    public void deletarProfissional(Long id) {
        profissionalRepository.deleteById(id);
    }
}
