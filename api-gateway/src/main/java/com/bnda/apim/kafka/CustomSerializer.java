package com.bnda.apim.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomSerializer implements Serializer<KafkaData> {

    private final ObjectMapper objectMapper;

    @Override
    public byte[] serialize(String topic, KafkaData kafkaData) {
        try{
            return objectMapper.writeValueAsBytes(kafkaData);
        }catch (Exception e){}

        return null;
    }
}
