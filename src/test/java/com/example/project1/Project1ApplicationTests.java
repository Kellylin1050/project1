package com.example.project1;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootTest
@ComponentScan({"main.controllers", "main.repositories"})
@EnableJpaRepositories(basePackages = "com.example.project1.Dao")
class Project1ApplicationTests {

    @Test
    void contextLoads() {
    }

}
