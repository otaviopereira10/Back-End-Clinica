package com.projeto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ Ativa CORS corretamente
            .csrf(csrf -> csrf.disable()) // ✅ Desativa CSRF
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/register", "/api/auth/login").permitAll() // ✅ Permite login e cadastro
                .requestMatchers("/api/auth/usuarios/**").permitAll() // ✅ Permite acesso à listagem de usuários
                .requestMatchers("/api/auth/**").permitAll() // ✅ Permite todas as rotas de autenticação
                .requestMatchers("/api/profissionais/**").permitAll() // ✅ Permite CRUD de profissionais
                .requestMatchers("/api/consultas/**").permitAll() // ✅ Permite CRUD de consultas
                .requestMatchers("/api/pacientes/**").permitAll() // ✅ Permite CRUD de pacientes
                .requestMatchers("/api/clinicas/**").permitAll() // ✅ Permite CRUD de clínicas
                .anyRequest().authenticated() // 🔒 Bloqueia o restante
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ Configuração CORS correta
    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
            "http://127.0.0.1:5500",
            "http://localhost:5500",
            "https://67a687dbbcc6fc006be4af8e--clinicafisio.netlify.app",
            "https://clinicafisio.netlify.app" // ✅ Permite chamadas do Netlify (URL final sem build preview)
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // ✅ Permite todos os métodos HTTP
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
