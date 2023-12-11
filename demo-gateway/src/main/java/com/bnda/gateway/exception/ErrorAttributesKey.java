package com.bnda.gateway.exception;

import lombok.Getter;

@Getter
public enum ErrorAttributesKey{
    TIME("timestamp"),
    CODE("code"),
    MESSAGE("message"),
    PATH("path");

    private final String key;
    ErrorAttributesKey(String key) {
        this.key = key;
    }
}
