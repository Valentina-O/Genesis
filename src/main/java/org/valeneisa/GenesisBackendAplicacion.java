package org.valeneisa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.valeneisa")
@EntityScan(basePackages = "org.valeneisa")
public class GenesisBackendAplicacion {

    public static void main(String[] args) {
        SpringApplication.run(GenesisBackendAplicacion.class, args);
    }
}