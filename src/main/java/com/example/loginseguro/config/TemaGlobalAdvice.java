package com.example.loginseguro.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Disponibiliza o nome do tema ativo para TODAS as páginas.
 * O layout usa esse valor para carregar o CSS do tema certo.
 * Para trocar o visual do sistema, basta mudar "app.tema" no application.properties.
 */
@ControllerAdvice
public class TemaGlobalAdvice {

    @Value("${app.tema:padrao}")
    private String tema;

    @ModelAttribute("tema")
    public String tema() {
        return tema;
    }
}