package com.example.articlesample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ArticleSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArticleSampleApplication.class, args);
    }

}
