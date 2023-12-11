package com.bnda.apim.kafka;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Data
@Slf4j
public class ProducerConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaServers;


    @Bean
    public KafkaAdmin admin()
    {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(KafkaConstants.KAFKA_TOPIC_NAME)
                .partitions(KafkaConstants.KAFKA_PARTITION_SIZE)
                .replicas(KafkaConstants.KAFKA_REPLICATION_FACTOR)
                .build();
    }


    @Bean
    public KafkaTemplate<String, KafkaData> kafkaTemplate() {
        KafkaTemplate<String, KafkaData> kafkaTemplate =  new KafkaTemplate<String, KafkaData>(producerFactory());

        kafkaTemplate.setProducerListener(new ProducerListener<String, KafkaData>() {
            @Override
            public void onSuccess(ProducerRecord<String, KafkaData> producerRecord, RecordMetadata recordMetadata) {

                log.info("ACK from ProducerListener message: {} partition:{} offset:  {}",
                        producerRecord.value(), recordMetadata.partition(), recordMetadata.offset());
            }

            public void onFailure(Throwable ex) {
                log.warn("Unable to deliver message  {}", ex.getMessage());
            }

        });

        return kafkaTemplate;
    }


    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServers);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 1);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonSerializer.class);
        // See https://kafka.apache.org/documentation/#producerconfigs for more properties
        return props;
    }


    @Bean
    public ProducerFactory<String, KafkaData> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }


}
