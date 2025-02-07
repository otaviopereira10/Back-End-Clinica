package com.projeto.services;

import com.projeto.entities.Cadastro_login;
import com.projeto.entities.Login;
import com.projeto.repository.CadastroRepository;
import com.projeto.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CadastroService {

    private final CadastroRepository cadastroRepository;
    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CadastroService(CadastroRepository cadastroRepository, LoginRepository loginRepository, PasswordEncoder passwordEncoder) {
        this.cadastroRepository = cadastroRepository;
        this.loginRepository = loginRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ Cadastrar usuário (Salva em ambas as tabelas)
    public void cadastrarUsuario(Cadastro_login usuario) {
        if (cadastroRepository.findByUser(usuario.getUser()).isPresent()) {
            throw new RuntimeException("Usuário já cadastrado!");
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha())); // Criptografar senha antes de salvar
        cadastroRepository.save(usuario);

        Login login = new Login(usuario.getUser(), usuario.getSenha());
        loginRepository.save(login);
    }

    // ✅ Listar todos os usuários
    public List<Cadastro_login> listarUsuarios() {
        return cadastroRepository.findAll();
    }

    // ✅ Buscar usuário por ID
    public Cadastro_login buscarUsuarioPorId(Long id) {
        return cadastroRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
    }

    // ✅ Atualizar usuário (mantém senha criptografada)
    public void atualizarUsuario(Long id, Cadastro_login usuarioAtualizado) {
        Cadastro_login usuarioExistente = cadastroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        usuarioExistente.setNome(usuarioAtualizado.getNome());
        usuarioExistente.setEmail(usuarioAtualizado.getEmail());
        usuarioExistente.setUser(usuarioAtualizado.getUser());
        usuarioExistente.setRegistro(usuarioAtualizado.getRegistro());

        // Atualizar a senha somente se uma nova for fornecida
        if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isEmpty()) {
            usuarioExistente.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
        }

        cadastroRepository.save(usuarioExistente);
    }

    // ✅ Deletar usuário
    public void deletarUsuario(Long id) {
        cadastroRepository.deleteById(id);
    }
}
