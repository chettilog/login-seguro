package com.example.loginseguro.controller;

import com.example.loginseguro.dto.CadastroForm;
import com.example.loginseguro.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Telas de login e cadastro.
 * O processamento do login (POST /login) e do logout é feito pelo Spring Security,
 * conforme configurado na SecurityConfig.
 */
@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /** Exibe a tela de login. */
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    /** Exibe o formulário de cadastro vazio. */
    @GetMapping("/cadastro")
    public String cadastroForm(Model model) {
        model.addAttribute("form", new CadastroForm());
        return "auth/cadastro";
    }

    /** Recebe o formulário de cadastro. */
    @PostMapping("/cadastro")
    public String cadastrar(@Valid @ModelAttribute("form") CadastroForm form,
                            BindingResult result,
                            Model model,
                            RedirectAttributes redirect) {

        // 1) Erros de validação (campos vazios, e-mail inválido, senha fraca...)
        if (result.hasErrors()) {
            return "auth/cadastro";
        }

        // 2) Regras de negócio (senhas diferentes, e-mail já cadastrado)
        try {
            userService.cadastrar(form);
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            return "auth/cadastro";
        }

        // 3) Sucesso: volta para o login com uma mensagem
        redirect.addFlashAttribute("sucesso", "Cadastro realizado! Faça login para continuar.");
        return "redirect:/login";
    }
}