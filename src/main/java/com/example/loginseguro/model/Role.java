package com.example.loginseguro.model;

/**
 * Perfis de acesso do sistema.
 * Para adaptar a outro tema, basta renomear ou adicionar valores aqui.
 */
public enum Role {
    ADMIN,   // acesso total: gerencia usuários
    GESTOR,  // acesso intermediário
    USUARIO  // acesso básico: perfil padrão ao se cadastrar
}