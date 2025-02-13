package com.projeto.services;

import com.projeto.entities.Clinica;
import com.projeto.entities.Profissional;
import com.projeto.repository.ClinicaRepository;
import com.projeto.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProfissionalService {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private ClinicaRepository clinicaRepository;

    // ✅ Listar todos os profissionais com as clínicas associadas
    public List<Profissional> listarProfissionais() {
        return profissionalRepository.findAll();
    }

    // ✅ Buscar um profissional por ID
    public Profissional buscarProfissionalPorId(Long id) {
        return profissionalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Erro: Profissional não encontrado!"));
    }

    // ✅ Cadastrar profissional com clínicas associadas
    @Transactional
    public Profissional cadastrarProfissional(Profissional profissional, Set<Long> clinicaIds) {
        if (profissionalRepository.findByRegistro(profissional.getRegistro()).isPresent()) {
            throw new RuntimeException("Erro: Registro já cadastrado!");
        }

        Set<Clinica> clinicas = clinicaRepository.findAllById(clinicaIds)
                .stream().collect(Collectors.toSet());

        if (clinicas.isEmpty() || clinicas.size() != clinicaIds.size()) {
            throw new RuntimeException("Erro: Uma ou mais clínicas não foram encontradas.");
        }

        profissional.setClinicas(clinicas);

        // ✅ Também adiciona o profissional na lista da clínica
        for (Clinica clinica : clinicas) {
            clinica.getProfissionais().add(profissional);
            clinicaRepository.save(clinica);
        }

        return profissionalRepository.save(profissional);
    }

    // ✅ Atualizar profissional e suas clínicas
    @Transactional
    public Profissional atualizarProfissional(Long id, Profissional profissionalAtualizado, Set<Long> clinicaIds) {
        Profissional profissionalExistente = buscarProfissionalPorId(id);

        profissionalExistente.setNome(profissionalAtualizado.getNome());
        profissionalExistente.setEspecialidade(profissionalAtualizado.getEspecialidade());
        profissionalExistente.setRegistro(profissionalAtualizado.getRegistro());
        profissionalExistente.setTelefone(profissionalAtualizado.getTelefone());

        Set<Clinica> clinicas = clinicaRepository.findAllById(clinicaIds)
                .stream().collect(Collectors.toSet());

        if (clinicas.size() != clinicaIds.size()) {
            throw new RuntimeException("Erro: Uma ou mais clínicas não foram encontradas.");
        }

        profissionalExistente.setClinicas(clinicas);

        return profissionalRepository.save(profissionalExistente);
    }

    // ✅ Deletar profissional e remover vínculo com clínicas
    @Transactional
    public void deletarProfissional(Long id) {
        Profissional profissional = buscarProfissionalPorId(id);

        // ✅ Remove vínculo com clínicas antes de excluir
        for (Clinica clinica : profissional.getClinicas()) {
            clinica.getProfissionais().remove(profissional);
            clinicaRepository.save(clinica);
        }

        profissionalRepository.delete(profissional);
    }
}
