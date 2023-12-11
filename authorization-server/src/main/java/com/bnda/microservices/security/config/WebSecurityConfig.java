package com.bnda.microservices.security.config;

import com.bnda.microservices.security.user.CustomUserDetailsService;
import com.bnda.microservices.security.user.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@Data
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserRepository userRepository;

    @Bean
    @Order(2)
    SecurityFilterChain appFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((authorize) -> authorize.requestMatchers("/register","/web/login", "/api/save").permitAll().anyRequest().authenticated());

        http.formLogin(Customizer.withDefaults());

        return http.build();



    }

    @Bean
    AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider= new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetails());
        provider.setPasswordEncoder(passEncoder());
        return provider;
    }

    @Bean
    PasswordEncoder passEncoder(){
        return  NoOpPasswordEncoder.getInstance();
    }

    @Bean
    UserDetailsService userDetails(){
        return new CustomUserDetailsService(getUserRepository());
    }


}
