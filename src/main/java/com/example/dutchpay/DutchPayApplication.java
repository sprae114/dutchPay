package com.example.dutchpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class DutchPayApplication {
    public static void main(String[] args) {
        SpringApplication.run(DutchPayApplication.class, args);
    }
}
