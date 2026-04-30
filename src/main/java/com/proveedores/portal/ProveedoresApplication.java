package com.proveedores.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.proveedores.portal")
@EntityScan(basePackages = "com.proveedores.portal.entity")
@EnableJpaRepositories(basePackages = "com.proveedores.portal.repository")
public class ProveedoresApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProveedoresApplication.class, args);
    }
}
