package com.bnda.apim.util;

import com.bnda.apim.api.ApiEndPoint;
import com.bnda.apim.api.Subscription;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Data
@Slf4j
public class DataRepository {
    private List<ApiEndPoint> allEndPoints;
    private List<Subscription> allSubscription;

    public Optional<Subscription> getSubscriptionByConsumerAndApi(String consumerToken, long endpointID){
        allSubscription.forEach(e->log.info("apiID:"+e.getApiEndPoint().getId()+" consumer:"+e.getConsumer().getTokenId()));
        return allSubscription.stream().filter(e->e.getConsumer().getTokenId().equals(consumerToken) && e.getApiEndPoint().getId()==endpointID).findFirst();
    }

    public Optional<Subscription> getSubscriptionById(long id){
        return allSubscription.stream().filter(e->e.getId()==id).findFirst();
    }


}
