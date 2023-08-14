package com.bnda.apim.api;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class ApiEndPoint {

    private long id;

    private String path;

    private String method;

    private String parameter;

    public ApiPublisher publisher;

}
