package com.bnda.apim.api;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Subscription {
    private long id;

    private Consumer consumer;

    private ApiEndPoint apiEndPoint;

    private int requestPerMinLimit;

    private int quotaLimit;


}
