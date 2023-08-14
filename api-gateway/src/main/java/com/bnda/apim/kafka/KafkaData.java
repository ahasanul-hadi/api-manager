package com.bnda.apim.kafka;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KafkaData {
    private String apiID;
    private String consumerID;
    private int status;
    private long timestamp;
    @JsonIgnore
    private String payload;
}
