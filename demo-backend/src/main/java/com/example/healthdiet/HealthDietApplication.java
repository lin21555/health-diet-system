package com.example.healthdiet;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.healthdiet.mapper")
public class HealthDietApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthDietApplication.class, args);
    }
}
