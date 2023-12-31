package com.bnda.gateway.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

record ExceptionRule(Class<?> exceptionClass, HttpStatus status){}

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes  {

    private final List<ExceptionRule> exceptionsRules = List.of(
            new ExceptionRule(GlobalCustomException.class, HttpStatus.UNAUTHORIZED)
    );


    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest  request, ErrorAttributeOptions options) {
        Throwable error = getError(request);
        final String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        if(error instanceof GlobalCustomException){
            GlobalCustomException exp= (GlobalCustomException)error;
            return Map.of(ErrorAttributesKey.CODE.getKey(), exp.getStatus().value(), ErrorAttributesKey.MESSAGE.getKey(), exp.getMessage(),  ErrorAttributesKey.TIME.getKey(), timestamp, ErrorAttributesKey.PATH.getKey(), exp.getPath());
        }
        else if(error instanceof ResponseStatusException){
            ResponseStatusException exp= (ResponseStatusException)error;
            return Map.of(ErrorAttributesKey.CODE.getKey(), exp.getStatusCode().value(), ErrorAttributesKey.MESSAGE.getKey(), exp.getMessage(),  ErrorAttributesKey.TIME.getKey(), timestamp, ErrorAttributesKey.PATH.getKey(), request.path());
        }

        return Map.of(ErrorAttributesKey.CODE.getKey(), determineHttpStatus(error).value(),  ErrorAttributesKey.MESSAGE.getKey(), error.getMessage(), ErrorAttributesKey.TIME.getKey(), timestamp, ErrorAttributesKey.PATH.getKey(), request.exchange().getRequest().getPath().toString());

    }


    private HttpStatus determineHttpStatus(Throwable error) {
        return  MergedAnnotations.from(error.getClass(), MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).get(ResponseStatus.class).getValue(ErrorAttributesKey.CODE.getKey(), HttpStatus.class).orElse(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}


