package com.bnda.apim.exception;

import lombok.Getter;

@Getter
public enum ErrorAttributesKey{
    TIME("timestamp"),
    STATUS("status"),
    ERROR("error"),
    PATH("path");

    private final String key;
    ErrorAttributesKey(String key) {
        this.key = key;
    }
}
