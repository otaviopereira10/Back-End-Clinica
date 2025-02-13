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

    // ✅ Cadastrar Profissional com Clínicas Associadas
    @Transactional
    public Profissional cadastrarProfissional(Profissional profissional, Set<Long> clinicaIds) {
        if (profissionalRepository.findByRegistro(profissional.getRegistro()).isPresent()) {
            throw new RuntimeException("Erro: Registro já cadastrado!");
        }

        Set<Clinica> clinicas = clinicaRepository.findAllById(clinicaIds)
                .stream()
                .collect(Collectors.toSet());

        if (clinicas.size() != clinicaIds.size()) {
            throw new RuntimeException("Erro: Uma ou mais clínicas não foram encontradas.");
        }

        profissional.setClinicas(clinicas);
        return profissionalRepository.save(profissional);
    }

    // ✅ Listar Todos os Profissionais
    public List<Profissional> listarProfissionais() {
        return profissionalRepository.findAll();
    }

    // ✅ Buscar Profissional por ID com Clínicas
    public Profissional buscarProfissionalPorId(Long id) {
        return profissionalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Erro: Profissional não encontrado!"));
    }

    // ✅ Atualizar Profissional com Clínicas Associadas
    @Transactional
    public Profissional atualizarProfissional(Long id, Profissional profissionalAtualizado, Set<Long> clinicaIds) {
        Profissional profissionalExistente = buscarProfissionalPorId(id);

        profissionalExistente.setNome(profissionalAtualizado.getNome());
        profissionalExistente.setEspecialidade(profissionalAtualizado.getEspecialidade());
        profissionalExistente.setRegistro(profissionalAtualizado.getRegistro());
        profissionalExistente.setTelefone(profissionalAtualizado.getTelefone());

        // ✅ Atualiza as clínicas associadas
        Set<Clinica> clinicas = clinicaRepository.findAllById(clinicaIds)
                .stream()
                .collect(Collectors.toSet());

        if (clinicas.size() != clinicaIds.size()) {
            throw new RuntimeException("Erro: Uma ou mais clínicas não foram encontradas.");
        }

        profissionalExistente.setClinicas(clinicas);
        return profissionalRepository.save(profissionalExistente);
    }

    // ✅ Deletar Profissional e Remover Referências em Clínicas
    @Transactional
    public void deletarProfissional(Long id) {
        Profissional profissional = buscarProfissionalPorId(id);

        // Remove vínculos com clínicas antes de excluir
        profissional.getClinicas().forEach(clinica -> clinica.getProfissionais().remove(profissional));

        profissionalRepository.delete(profissional);
    }
}
