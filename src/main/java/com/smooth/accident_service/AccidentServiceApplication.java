package com.smooth.accident_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AccidentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccidentServiceApplication.class, args);
    }

}
