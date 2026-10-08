package com.example.loginseguro.service;

import com.example.loginseguro.dto.CadastroForm;
import com.example.loginseguro.model.Role;
import com.example.loginseguro.model.User;
import com.example.loginseguro.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Regras de negócio relacionadas a usuários.
 * Os controllers chamam esta classe; ela é quem fala com o banco.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Cadastra um novo usuário com o perfil padrão USUARIO.
     * Lança IllegalArgumentException com uma mensagem amigável se algo estiver errado.
     */
    public User cadastrar(CadastroForm form) {
        // Padroniza o e-mail: sem espaços e em minúsculas
        String email = form.getEmail().trim().toLowerCase();

        if (!form.getSenha().equals(form.getConfirmarSenha())) {
            throw new IllegalArgumentException("As senhas não conferem");
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Este e-mail já está cadastrado");
        }

        // A senha é transformada em hash antes de ir para o banco
        String senhaHash = passwordEncoder.encode(form.getSenha());

        User user = new User(
                form.getNome().trim(),
                email,
                senhaHash,
                new HashSet<>(Set.of(Role.USUARIO))
        );

        return userRepository.save(user);
    }

    /** Lista todos os usuários (usado na área do ADMIN). */
    public List<User> listarTodos() {
        return userRepository.findAll();
    }

    /**
     * Troca o perfil de um usuário (usado na área do ADMIN).
     * O administrador não pode alterar o próprio perfil, para o sistema
     * nunca ficar sem ADMIN por engano.
     */
    public void alterarPerfil(String userId, Role novoPerfil, String emailDoAdmin) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (user.getEmail().equalsIgnoreCase(emailDoAdmin)) {
            throw new IllegalArgumentException("Você não pode alterar o seu próprio perfil");
        }

        user.setRoles(new HashSet<>(Set.of(novoPerfil)));
        userRepository.save(user);
    }
}