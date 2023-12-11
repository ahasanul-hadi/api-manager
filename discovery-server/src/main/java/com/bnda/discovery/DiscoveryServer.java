package com.bnda.discovery;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServer {
    public static void main(String args[]){
        SpringApplication.run(DiscoveryServer.class, args);
    }

    @Bean
    CommandLineRunner init(){
        return args -> {
            System.out.println("Hello Discovery Server....");
        };
    }
}
