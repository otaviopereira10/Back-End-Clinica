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

@Service
public class ProfissionalService {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private ClinicaRepository clinicaRepository;

    // ✅ Listar todos os profissionais
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
        if (clinicaIds == null || clinicaIds.isEmpty()) {
            throw new RuntimeException("Erro: O profissional deve estar associado a pelo menos uma clínica.");
        }

        // ✅ Busca as clínicas e verifica se todas existem
        Set<Clinica> clinicas = new HashSet<>(clinicaRepository.findAllById(clinicaIds));

        if (clinicas.isEmpty() || clinicas.size() != clinicaIds.size()) {
            throw new RuntimeException("Erro: Uma ou mais clínicas informadas não existem.");
        }

        // ✅ Associa clínicas ao profissional
        profissional.setClinicas(clinicas);
        Profissional novoProfissional = profissionalRepository.save(profissional);

        // ✅ Atualiza as clínicas para garantir persistência da relação ManyToMany
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

        // ✅ Atualiza as clínicas associadas
        Set<Clinica> clinicas = new HashSet<>(clinicaRepository.findAllById(clinicaIds));

        if (clinicas.isEmpty() || clinicas.size() != clinicaIds.size()) {
            throw new RuntimeException("Erro: Uma ou mais clínicas informadas não existem.");
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
