package com.fitai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Point d'entrée principal de l'API FitAI.
 *
 * @author Walid
 * @version 1.0.0
 */
@SpringBootApplication
public class FitAiApplication {

    /**
     * Méthode main qui démarre le contexte Spring Boot.
     *
     * @param args arguments de ligne de commande passés à l'application.
     */
    public static void main(String[] args) {
        SpringApplication.run(FitAiApplication.class, args);
    }
}
