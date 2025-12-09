package com.sidy.sms_service.config;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DotenvInitializer {
    private final Environment env;

    public DotenvInitializer(Environment env) {
        this.env = env;
    }

    @PostConstruct
    public void loadEnv() {
        try {
            // --- 1) Lire le profil ACTIF ---
            String[] profiles = env.getActiveProfiles();
            String activeProfile = profiles.length > 0 ? profiles[0] : "default";

            String envFile = switch (activeProfile) {
                case "dev" -> ".env.dev";
                case "prod" -> ".env.prod";
                case "test" -> ".env.test";
                default -> ".env";
            };

            log.info("→ Loading env file: {}", envFile);

            // --- 2) Charger le bon fichier .env ---
            Dotenv dotenv = Dotenv.configure()
                    .filename(envFile)
                    .ignoreIfMissing()
                    .load();

            // --- 3) Injecter dans System Properties ---
            dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

            log.info("✓ {} loaded successfully", envFile);

        } catch (Exception e) {
            log.error("⚠️ Failed to load environment file: {}", e.getMessage());
        }
    }
}
