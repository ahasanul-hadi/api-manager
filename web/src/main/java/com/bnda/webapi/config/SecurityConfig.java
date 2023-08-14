package com.bnda.webapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception{

        http.authorizeHttpRequests( authorize -> authorize.requestMatchers("/**").permitAll().anyRequest().authenticated());

        http.formLogin(Customizer.withDefaults());

        return http.build();
    }
}
