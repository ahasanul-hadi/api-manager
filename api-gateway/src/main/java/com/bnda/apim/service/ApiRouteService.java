package com.bnda.apim.service;

import com.bnda.apim.api.ApiEndPoint;
import com.bnda.apim.api.Subscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class ApiRouteService {

    private final WebClient.Builder webClient;

    public Flux<ApiEndPoint> findApiRoutes(){
        Flux<ApiEndPoint> endPoints = webClient.build().get()
                .uri("http://localhost:8000/api-web/api/endpoints")
                .retrieve().bodyToFlux(ApiEndPoint.class);

        endPoints.log();

        return endPoints;
    }

    public Flux<Subscription> findAllSubscriptions(){
        Flux<Subscription> subsriptions = webClient.build().get()
                .uri("http://localhost:8000/api-web/api/subscriptions")
                .retrieve().bodyToFlux(Subscription.class);

        return subsriptions;
    }


}
