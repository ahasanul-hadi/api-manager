package com.bnda.webapi.publisher;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class EndPointDTO {
    private long id;

    private Long publisherID;
    private String path;
    private MethodType method;
    private String parameter;
}
