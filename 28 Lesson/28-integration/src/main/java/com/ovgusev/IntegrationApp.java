package com.ovgusev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.http.config.EnableIntegrationGraphController;

@SpringBootApplication
@EnableIntegrationGraphController
public class IntegrationApp {
    public static void main(String[] args) {
        SpringApplication.run(IntegrationApp.class);
    }
}
