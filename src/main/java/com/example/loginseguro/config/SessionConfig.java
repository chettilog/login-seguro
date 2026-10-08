package com.example.loginseguro.config;

import org.mongodb.spring.session.config.annotation.web.http.EnableMongoHttpSession;
import org.springframework.context.annotation.Configuration;

/**
 * Armazena as sessões HTTP (usuário logado) na coleção "sessions" do MongoDB Atlas,
 * em vez de guardar na memória do servidor.
 *
 * Vantagens:
 * - o usuário continua logado mesmo se a aplicação reiniciar;
 * - permite rodar várias instâncias do sistema compartilhando as mesmas sessões.
 *
 * A sessão expira após 30 minutos (1800 segundos) sem uso.
 */
@Configuration
@EnableMongoHttpSession(collectionName = "sessions", maxInactiveIntervalInSeconds = 1800)
public class SessionConfig {
}