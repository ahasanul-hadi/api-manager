package com.bnda.apim.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class GlobalCustomException extends RuntimeException{
    HttpStatus status;
    private String message;
    private String path;

    public GlobalCustomException(HttpStatus status, String message, String path) {
        super(message);
        this.message = message;
        this.status = status;
        this.path = path;
    }
    public GlobalCustomException() {
    }
}
