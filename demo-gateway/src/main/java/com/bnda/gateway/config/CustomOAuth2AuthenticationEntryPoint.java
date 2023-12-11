package com.bnda.gateway.config;


import com.bnda.gateway.exception.GlobalCustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
public class CustomOAuth2AuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        log.error(e.getLocalizedMessage());

        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String errorMessage = "Insufficient authentication details";


        if (e instanceof OAuth2AuthenticationException) {
            OAuth2Error error = ((OAuth2AuthenticationException) e).getError();

            if (StringUtils.hasText(error.getDescription())) {
                errorMessage = error.getDescription();
                errorMessage= errorMessage;
            }

            if (error instanceof BearerTokenError bearerTokenError) {

                if (StringUtils.hasText(bearerTokenError.getScope())) {
                    errorMessage= bearerTokenError.getScope();
                }

                status = ((BearerTokenError) error).getHttpStatus();
            }
        }

        return Mono.error(new GlobalCustomException(status,errorMessage,exchange.getRequest().getPath().toString()));
    }
}
