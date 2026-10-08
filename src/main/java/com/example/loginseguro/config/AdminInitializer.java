package com.example.loginseguro.config;

import com.example.loginseguro.model.Role;
import com.example.loginseguro.model.User;
import com.example.loginseguro.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Executado automaticamente quando o sistema inicia.
 * Garante que exista um administrador, usando as variáveis ADMIN_EMAIL e ADMIN_SENHA.
 * Assim nenhuma credencial fica escrita no código.
 */
@Component
public class AdminInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email:}")
    private String adminEmail;

    @Value("${app.admin.senha:}")
    private String adminSenha;

    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (adminEmail == null || adminEmail.isBlank()) {
            log.info("ADMIN_EMAIL não definido: nenhum administrador inicial configurado.");
            return;
        }

        String email = adminEmail.trim().toLowerCase();

        userRepository.findByEmail(email).ifPresentOrElse(
                // Caso 1: a conta já existe -> promove a ADMIN
                user -> {
                    if (!user.getRoles().contains(Role.ADMIN)) {
                        user.setRoles(new HashSet<>(Set.of(Role.ADMIN)));
                        userRepository.save(user);
                        log.info("Usuário {} promovido a ADMIN.", email);
                    }
                },
                // Caso 2: a conta não existe -> cria um ADMIN novo
                () -> {
                    if (adminSenha == null || adminSenha.isBlank()) {
                        log.warn("ADMIN_SENHA não definida: não foi possível criar o administrador {}.", email);
                        return;
                    }
                    User admin = new User(
                            "Administrador",
                            email,
                            passwordEncoder.encode(adminSenha),
                            new HashSet<>(Set.of(Role.ADMIN))
                    );
                    userRepository.save(admin);
                    log.info("Administrador {} criado.", email);
                }
        );
    }
}