package com.bnda.microservices.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AuthServer {
    @Value("${spring.application.name}")
    private String appName;
    public static void main(String args[]){
         SpringApplication.run(AuthServer.class, args);
    }

    @Bean
    public CommandLineRunner init(){
        return args->{
            System.out.println("Hello "+appName);
        };
    }
}


