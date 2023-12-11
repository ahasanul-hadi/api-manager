package com.bnda.microservices;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Service2 {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${server.port}")
    private int serverPort;

    public static void main(String args[]){
        SpringApplication.run(Service2.class, args);
    }

    @Bean
    CommandLineRunner init(){
        return args->{
            System.out.println("Hello "+appName+" Port:"+serverPort);
        };
    }
}
