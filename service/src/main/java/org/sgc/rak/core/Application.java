package org.sgc.rak.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Application entry point.
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.sgc.rak.repositories")
@EntityScan(basePackages = "org.sgc.rak.model")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
