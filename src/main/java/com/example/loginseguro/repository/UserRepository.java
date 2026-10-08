package com.example.loginseguro.repository;

import com.example.loginseguro.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Acesso à coleção "users" no MongoDB.
 * O Spring Data gera a implementação automaticamente a partir do nome dos métodos.
 */
public interface UserRepository extends MongoRepository<User, String> {

    // Busca o usuário pelo e-mail (usado no login)
    Optional<User> findByEmail(String email);

    // Verifica se o e-mail já está cadastrado (usado no cadastro)
    boolean existsByEmail(String email);
}