package com.projeto.services;

import com.projeto.entities.Clinica;
import com.projeto.entities.Profissional;
import com.projeto.repository.ClinicaRepository;
import com.projeto.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProfissionalService {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private ClinicaRepository clinicaRepository;

    // ✅ Listar todos os profissionais com suas clínicas associadas
    public List<Profissional> listarProfissionais() {
        return profissionalRepository.findAll();
    }

    // ✅ Buscar um profissional por ID com clínicas associadas
    public Profissional buscarProfissionalPorId(Long id) {
        return profissionalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Erro: Profissional não encontrado!"));
    }

    // ✅ Cadastrar profissional e associar clínicas
    @Transactional
    public Profissional cadastrarProfissional(Profissional profissional, Set<Long> clinicaIds) {
        if (clinicaIds == null || clinicaIds.isEmpty()) {
            throw new RuntimeException("Erro: O profissional deve estar associado a pelo menos uma clínica.");
        }

        Set<Clinica> clinicas = new HashSet<>(clinicaRepository.findAllById(clinicaIds));

        if (clinicas.isEmpty() || clinicas.size() != clinicaIds.size()) {
            throw new RuntimeException("Erro: Uma ou mais clínicas informadas não existem.");
        }

        profissional.setClinicas(clinicas);
        Profissional novoProfissional = profissionalRepository.save(profissional);

        // ✅ Atualiza a relação ManyToMany garantindo que a clínica também guarde o profissional
        for (Clinica clinica : clinicas) {
            clinica.getProfissionais().add(novoProfissional);
            clinicaRepository.save(clinica);
        }

        return novoProfissional;
    }

    // ✅ Atualizar profissional e suas clínicas
    @Transactional
    public Profissional atualizarProfissional(Long id, Profissional profissionalAtualizado, Set<Long> clinicaIds) {
        Profissional profissionalExistente = buscarProfissionalPorId(id);

        profissionalExistente.setNome(profissionalAtualizado.getNome());
        profissionalExistente.setEspecialidade(profissionalAtualizado.getEspecialidade());
        profissionalExistente.setRegistro(profissionalAtualizado.getRegistro());
        profissionalExistente.setTelefone(profissionalAtualizado.getTelefone());

        Set<Clinica> clinicas = new HashSet<>(clinicaRepository.findAllById(clinicaIds));

        if (clinicas.isEmpty() || clinicas.size() != clinicaIds.size()) {
            throw new RuntimeException("Erro: Uma ou mais clínicas informadas não existem.");
        }

        profissionalExistente.setClinicas(clinicas);
        return profissionalRepository.save(profissionalExistente);
    }

    // ✅ Deletar profissional e remover vínculos
    @Transactional
    public void deletarProfissional(Long id) {
        Profissional profissional = buscarProfissionalPorId(id);

        for (Clinica clinica : profissional.getClinicas()) {
            clinica.getProfissionais().remove(profissional);
            clinicaRepository.save(clinica);
        }

        profissionalRepository.delete(profissional);
    }
}
