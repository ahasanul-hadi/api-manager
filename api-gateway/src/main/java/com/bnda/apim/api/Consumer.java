package com.bnda.apim.api;

import lombok.Data;

import java.util.List;


@Data
public class Consumer {
    private long id;

    private String tokenId;

    private String consumerName;

    private boolean isActive;

}
