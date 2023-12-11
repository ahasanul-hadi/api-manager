package com.bnda.apim.config;

import com.bnda.apim.util.DataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AppConfig {

    @Bean
    public DataRepository initData(){
        return new DataRepository();
    }

    @Bean
    WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }


}
