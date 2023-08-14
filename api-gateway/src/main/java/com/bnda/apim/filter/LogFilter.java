package com.bnda.apim.filter;

import com.bnda.apim.kafka.KafkaConstants;
import com.bnda.apim.kafka.KafkaData;
import com.bnda.apim.util.GlobalConstants;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;


@Component
@RequiredArgsConstructor
@Slf4j
public class LogFilter implements GatewayFilter, Ordered {

    private final KafkaTemplate<String, KafkaData> kafkaTemplate;
    private final ModifyResponseBodyGatewayFilterFactory modifyResponseBodyGatewayFilterFactory;

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String reqID= exchange.getRequest().getId();
        String uri= exchange.getRequest().getPath().toString();
        String host= exchange.getRequest().getRemoteAddress().getHostName();
        String method= exchange.getRequest().getMethod().name();

        String requestBody = exchange.getAttribute(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR);

       // ObjectMapper mapper = new ObjectMapper();
        //JsonNode tree = mapper.readTree(cachedBodyAttribute);
        //JsonNode node = tree.get("test");
        //String test = node.textValue();

        //XForwardedRemoteAddressResolver resolver = XForwardedRemoteAddressResolver.maxTrustedIndex(1);
        //InetSocketAddress inetSocketAddress = resolver.resolve(exchange);
        //String ip= inetSocketAddress.getAddress().getHostAddress();

        //log.info("req:"+reqID+" uri:"+uri+" requestBody:"+requestBody);


        //return chain.filter(exchange);

        long timestamp= System.currentTimeMillis();
        String apiID= exchange.getRequest().getHeaders().getFirst(GlobalConstants.HEADER_ENDPOINT_ID);
        String consumerID= exchange.getRequest().getHeaders().getFirst(GlobalConstants.HEADER_CONSUMER_ID);


        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    log.info("reqID: {}  ip: {} uri: {} status: {} requestBody:{}  apiID:{}",reqID,host,uri,exchange.getResponse().getStatusCode(),requestBody, apiID);

                    try {
                        KafkaData data = KafkaData.builder()
                                .apiID(apiID)
                                .consumerID(consumerID)
                                .status(exchange.getResponse().getStatusCode().value())
                                .timestamp(timestamp)
                                .payload("")
                                .build();


                        CompletableFuture<SendResult<String, KafkaData>> future = kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC_NAME, reqID, data);
                        kafkaTemplate.flush();
                        future.whenComplete((result, ex) -> {
                            if (ex == null) {

                                log.error("Sent apiid=[" + data.getApiID() +
                                        "] with offset=[" + result.getRecordMetadata().offset() + "] partion:"+result.getRecordMetadata().partition());
                            } else {
                                log.error("Unable to send apiid=[" +
                                        data.getApiID() + "] due to : " + ex.getMessage());
                            }
                        });

                    }catch (Exception e){
                        log.error("error sending kafka:"+e.toString());
                    }

                }));


    }

    @Override
    public int getOrder() {
        return 100;
    }
}
