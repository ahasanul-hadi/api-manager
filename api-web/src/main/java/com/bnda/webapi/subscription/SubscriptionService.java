package com.bnda.webapi.subscription;

import com.bnda.webapi.consumer.ConsumerService;
import com.bnda.webapi.exception.ApplicationException;
import com.bnda.webapi.publisher.ApiEndPointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubscriptionService {

    @Value("${api.manager.gateway.url}")
    private String gatewayUrl;

    private final SubscriptionRepository subscriptionRepository;
    private final ConsumerService consumerService;
    private final ApiEndPointService endPointService;
    private final WebClient.Builder webClient;


    public List<Subscription> findAll(){
        return subscriptionRepository.findAll();
    }

    public void subscribe(SubscriptionDTO subscriptionDTO) throws ApplicationException {
        Subscription subscription= new Subscription();
        subscription.setApiEndPoint(endPointService.findById(subscriptionDTO.getEndpointID()).orElseThrow());
        subscription.setConsumer(consumerService.findById(subscriptionDTO.getConsumerID()));
        subscription.setRequestPerSecLimit(subscriptionDTO.getRequestPerSecLimit());

        subscriptionRepository.save(subscription);
    }

    public void flushGateway(){
        try{
            String response=  webClient.build().get()
                    .uri(gatewayUrl+"/api/route/refresh")
                    .retrieve().bodyToFlux(String.class).blockFirst();
            log.info("response:"+response);
        }catch (Exception e){
            log.error(e.toString());
        }

    }

    public void deleteById(Long subID) {
        subscriptionRepository.deleteById(subID);
    }
}
