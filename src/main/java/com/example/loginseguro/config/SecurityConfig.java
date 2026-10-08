package com.example.loginseguro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração central de segurança:
 * - como as senhas são criptografadas (BCrypt)
 * - quais rotas cada perfil pode acessar
 * - como funcionam login e logout
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * BCrypt gera um hash diferente a cada senha (usa "salt" aleatório).
     * O número 12 define o custo: quanto maior, mais difícil de quebrar por força bruta.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ===== Regras de acesso por rota =====
                .authorizeHttpRequests(auth -> auth
                        // Páginas públicas (qualquer pessoa acessa)
                        .requestMatchers("/", "/login", "/cadastro",
                                "/css/**", "/js/**", "/images/**", "/themes/**", "/error").permitAll()
                        // Somente ADMIN
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // ADMIN e GESTOR
                        .requestMatchers("/gestor/**").hasAnyRole("ADMIN", "GESTOR")
                        // Qualquer outra rota exige estar logado (qualquer perfil)
                        .anyRequest().authenticated()
                )

                // ===== Login =====
                .formLogin(form -> form
                        .loginPage("/login")               // nossa página de login
                        .usernameParameter("email")        // nome do campo de e-mail no formulário
                        .passwordParameter("senha")        // nome do campo de senha no formulário
                        .defaultSuccessUrl("/painel", true) // para onde vai após logar
                        .failureUrl("/login?erro")          // se errar a senha
                        .permitAll()
                )

                // ===== Logout =====
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)            // encerra a sessão
                        .deleteCookies("JSESSIONID", "SESSION") // apaga o cookie da sessão
                        .permitAll()
                )

                // ===== Acesso negado (logado, mas sem o perfil necessário) =====
                .exceptionHandling(ex -> ex.accessDeniedPage("/acesso-negado"));

        // A proteção CSRF fica ativada por padrão (proteção contra formulários falsos)
        return http.build();
    }
}