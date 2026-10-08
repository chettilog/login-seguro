package com.example.loginseguro.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Dados recebidos do formulário de cadastro.
 * Contém apenas os campos que o usuário pode preencher
 * (o perfil é definido pelo sistema, nunca pelo formulário).
 */
public class CadastroForm {

    @NotBlank(message = "Informe seu nome")
    @Size(min = 3, max = 80, message = "O nome deve ter entre 3 e 80 caracteres")
    private String nome;

    @NotBlank(message = "Informe seu e-mail")
    @Email(message = "Informe um e-mail válido")
    private String email;

    @NotBlank(message = "Informe uma senha")
    @Size(min = 8, max = 64, message = "A senha deve ter entre 8 e 64 caracteres")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
            message = "A senha deve conter letras e números")
    private String senha;

    @NotBlank(message = "Confirme sua senha")
    private String confirmarSenha;

    // ===== Getters e Setters =====

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getConfirmarSenha() { return confirmarSenha; }
    public void setConfirmarSenha(String confirmarSenha) { this.confirmarSenha = confirmarSenha; }
}