package com.example.loginseguro.controller;

import com.example.loginseguro.model.Role;
import com.example.loginseguro.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Área administrativa.
 * Todas as rotas /admin/** são restritas ao perfil ADMIN pela SecurityConfig.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    /** Lista todos os usuários. */
    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", userService.listarTodos());
        model.addAttribute("perfis", Role.values());
        return "admin/usuarios";
    }

    /** Altera o perfil de um usuário. */
    @PostMapping("/usuarios/{id}/perfil")
    public String alterarPerfil(@PathVariable("id") String id,
                                @RequestParam("perfil") Role perfil,
                                Authentication auth,
                                RedirectAttributes redirect) {
        try {
            userService.alterarPerfil(id, perfil, auth.getName());
            redirect.addFlashAttribute("sucesso", "Perfil atualizado com sucesso.");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }
}