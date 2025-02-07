package com.projeto.services;

import com.projeto.entities.Profissional;
import com.projeto.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfissionalService {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    // ✅ Criar novo profissional
    public void cadastrarProfissional(Profissional profissional) {
        if (profissionalRepository.findByRegistro(profissional.getRegistro()).isPresent()) {
            throw new RuntimeException("Registro já cadastrado!");
        }
        profissionalRepository.save(profissional);
    }

    // ✅ Listar todos os profissionais
    public List<Profissional> listarProfissionais() {
        return profissionalRepository.findAll();
    }

    // ✅ Buscar profissional por ID
    public Profissional buscarProfissionalPorId(Long id) {
        return profissionalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado!"));
    }

    // ✅ Atualizar profissional
    public void atualizarProfissional(Long id, Profissional profissionalAtualizado) {
        Profissional profissionalExistente = buscarProfissionalPorId(id);
        
        profissionalExistente.setNome(profissionalAtualizado.getNome());
        profissionalExistente.setEspecialidade(profissionalAtualizado.getEspecialidade());
        profissionalExistente.setRegistro(profissionalAtualizado.getRegistro());
        profissionalExistente.setTelefone(profissionalAtualizado.getTelefone());

        profissionalRepository.save(profissionalExistente);
    }

    // ✅ Deletar profissional
    public void deletarProfissional(Long id) {
        profissionalRepository.deleteById(id);
    }
}
