package com.projeto.controllers;

import com.projeto.entities.Cadastro_login;
import com.projeto.entities.Login;
import com.projeto.services.CadastroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class CadastroController {

    @Autowired
    private CadastroService cadastroService;

    // ✅ Cadastrar novo usuário
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Cadastro_login usuario) {
        try {
            cadastroService.cadastrarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: " + e.getMessage());
        }
    }

    // ✅ Buscar todos os usuários
    @GetMapping("/usuarios")
    public ResponseEntity<List<Cadastro_login>> listarUsuarios() {
        return ResponseEntity.ok(cadastroService.listarUsuarios());
    }

    // ✅ Buscar usuário por ID
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<?> buscarUsuarioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cadastroService.buscarUsuarioPorId(id));
    }

    // ✅ Atualizar usuário
    @PutMapping("/usuarios/{id}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable Long id, @RequestBody Cadastro_login usuarioAtualizado) {
        cadastroService.atualizarUsuario(id, usuarioAtualizado);
        return ResponseEntity.ok("Usuário atualizado com sucesso!");
    }

    // ✅ Excluir usuário
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<?> deletarUsuario(@PathVariable Long id) {
        cadastroService.deletarUsuario(id);
        return ResponseEntity.ok("Usuário deletado com sucesso!");
    }
}
