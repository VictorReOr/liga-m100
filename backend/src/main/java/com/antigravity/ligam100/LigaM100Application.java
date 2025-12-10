package com.antigravity.ligam100;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LigaM100Application {

    public static void main(String[] args) {
        SpringApplication.run(LigaM100Application.class, args);
    }

}
