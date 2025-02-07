package com.projeto.services;

import com.projeto.entities.Login;
import com.projeto.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Login autenticarUsuario(String user, String senha) {
        System.out.println("🔍 Buscando usuário: " + user);
        
        Optional<Login> usuarioOpt = loginRepository.findByUser(user);

        if (usuarioOpt.isEmpty()) {
            System.out.println("❌ Usuário não encontrado!");
            throw new RuntimeException("Usuário ou senha inválidos!");
        }

        Login usuario = usuarioOpt.get();
        System.out.println("🔑 Usuário encontrado: " + usuario.getUser());
        System.out.println("📌 Senha digitada: " + senha);
        System.out.println("🛠️ Senha armazenada: " + usuario.getSenha());

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            System.out.println("❌ Senha incorreta!");
            throw new RuntimeException("Usuário ou senha inválidos!");
        }

        System.out.println("✅ Login bem-sucedido!");
        return usuario;
        
        
    }
    
}

