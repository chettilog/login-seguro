package com.example.loginseguro.controller;

import com.example.loginseguro.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Páginas gerais do sistema.
 * Quem pode acessar cada rota é definido na SecurityConfig, não aqui.
 */
@Controller
public class PageController {

    private final UserRepository userRepository;

    public PageController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /** Página inicial (pública). */
    @GetMapping("/")
    public String inicio() {
        return "index";
    }

    /** Painel do usuário logado (qualquer perfil). */
    @GetMapping("/painel")
    public String painel(Authentication auth, Model model) {
        // auth.getName() devolve o e-mail de quem está logado
        userRepository.findByEmail(auth.getName())
                .ifPresent(usuario -> model.addAttribute("usuario", usuario));
        return "painel";
    }

    /** Área de gestão (GESTOR e ADMIN). */
    @GetMapping("/gestor")
    public String gestor() {
        return "gestor";
    }

    /** Exibida quando alguém logado tenta acessar uma área sem permissão. */
    @GetMapping("/acesso-negado")
    public String acessoNegado() {
        return "acesso-negado";
    }
}