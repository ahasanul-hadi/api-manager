package com.bnda.apim.route;

import com.bnda.apim.api.ApiEndPoint;
import com.bnda.apim.api.Subscription;
import com.bnda.apim.util.DataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;


@Service
@Slf4j
@RequiredArgsConstructor
public class RouteService {

    @Value("${api.web.url}")
    private String apiWebUrl;

    private final WebClient.Builder webClient;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final DataRepository dataRepository;

    public Flux<ApiEndPoint> findApiRoutes(){

        return webClient.build().get()
                .uri(apiWebUrl+"/api/endpoints")
                .retrieve().bodyToFlux(ApiEndPoint.class);
    }

    public Flux<Subscription> findAllSubscriptions(){

        return webClient.build().get()
                .uri(apiWebUrl+"/api/subscriptions")
                .retrieve().bodyToFlux(Subscription.class);
    }

    public void refreshRoutes() {

        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));

        findApiRoutes().collectList().subscribe(e->{
            log.info("api routes:"+e.toString());
            dataRepository.setAllEndPoints(e);
        });

        findAllSubscriptions().collectList().subscribe(e->{
            log.info("subscriptions:"+e.toString());
            dataRepository.setAllSubscription(e);
        });
    }


}
