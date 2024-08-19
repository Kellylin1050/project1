package com.example.project1;

import com.example.project1.Dao.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = "com.example.project1")
//@EnableJpaRepositories(basePackages = "com.example.project1.Dao")
@EntityScan(basePackages = "com.example.project1.Entity")
@EnableAspectJAutoProxy(proxyTargetClass = true)
//@EnableJpaRepositories(repositoryFactoryBeanClass= UserRepository.class)
public class Project1Application {

    public static void main(String[] args) {
        SpringApplication.run(Project1Application.class, args);
    }

}
