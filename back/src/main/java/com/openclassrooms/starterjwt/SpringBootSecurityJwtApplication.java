package com.openclassrooms.starterjwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication()
@EnableJpaAuditing
public class SpringBootSecurityJwtApplication {
	public static void main(String[] args) {
		// Chargement des variables d'environnement
		
		Dotenv dotenv = Dotenv.configure()
							.directory("/resources")
							.filename("env") // instead of '.env', use 'env'
							.load();
		dotenv.entries().forEach(entry ->
		System.setProperty(entry.getKey(), entry.getValue())
		);
		SpringApplication.run(SpringBootSecurityJwtApplication.class, args);
	}
}


