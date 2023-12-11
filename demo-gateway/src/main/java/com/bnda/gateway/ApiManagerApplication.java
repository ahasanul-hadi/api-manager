package com.bnda.gateway;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class ApiManagerApplication {

	@Value("${spring.application.name}")
	private String appName;

	public static void main(String[] args) {
		//ClassLoader.getSystemResourceAsStream("/application.properties");
		SpringApplication.run(ApiManagerApplication.class, args);

	}

	@Bean
	WebClient.Builder webClientBuilder() {
		return WebClient.builder();
	}

	@Bean
	CommandLineRunner init(){
		return args->{
			log.info("appName:"+appName);
		};
	}


}
