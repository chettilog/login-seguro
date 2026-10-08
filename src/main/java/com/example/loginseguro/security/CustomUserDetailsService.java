package com.example.loginseguro.security;

import com.example.loginseguro.model.User;
import com.example.loginseguro.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Faz a ponte entre o Spring Security e o MongoDB:
 * no login, busca o usuário pelo e-mail e informa senha (hash) e perfis.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // E-mails são sempre salvos em minúsculas, então buscamos do mesmo jeito
        User user = userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        // Converte os perfis (ADMIN, GESTOR, USUARIO) para texto
        String[] roles = user.getRoles().stream()
                .map(Enum::name)
                .toArray(String[]::new);

        // Monta o objeto que o Spring Security entende.
        // .roles() adiciona o prefixo "ROLE_" automaticamente (ex.: ROLE_ADMIN)
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getSenhaHash())
                .roles(roles)
                .disabled(!user.isAtivo())
                .build();
    }
}