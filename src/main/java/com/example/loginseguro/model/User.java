package com.example.loginseguro.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * Representa um usuário salvo na coleção "users" do MongoDB.
 * Classe genérica: pode receber novos campos conforme o tema do projeto.
 */
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String nome;

    // E-mail é o login do usuário; o índice impede e-mails repetidos no banco
    @Indexed(unique = true)
    private String email;

    // Nunca guardamos a senha pura, apenas o hash gerado pelo BCrypt
    private String senhaHash;

    // Perfis de acesso (ADMIN, GESTOR, USUARIO)
    private Set<Role> roles = new HashSet<>();

    // Permite desativar um usuário sem apagá-lo
    private boolean ativo = true;

    private Instant criadoEm = Instant.now();

    // Construtor vazio exigido pelo Spring Data
    public User() {
    }

    public User(String nome, String email, String senhaHash, Set<Role> roles) {
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.roles = roles;
    }

    // ===== Getters e Setters =====

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public Instant getCriadoEm() { return criadoEm; }
    public void setCriadoEm(Instant criadoEm) { this.criadoEm = criadoEm; }
}