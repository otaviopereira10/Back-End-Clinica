package com.projeto.controllers;

import com.projeto.entities.Login;
import com.projeto.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> autenticarUsuario(@RequestBody Login usuario) {
        try {
            loginService.autenticarUsuario(usuario.getUser(), usuario.getSenha());
            return ResponseEntity.ok("Login bem-sucedido!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha inválidos!");
        }
    }
}
