package com.bnda.apim.filter;

import com.bnda.apim.api.Subscription;
import com.bnda.apim.exception.GlobalCustomException;
import com.bnda.apim.route.RouteService;
import com.bnda.apim.util.DataRepository;
import com.bnda.apim.util.GlobalConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GatewayFilter, Ordered {

    private final RouteService apiRouteService;

    private final DataRepository dataRepository;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authorizationToken= exchange.getRequest().getHeaders().getFirst(GlobalConstants.HEADER_AUTHORIZATION);

        if(authorizationToken == null || !authorizationToken.startsWith("Bearer")){
            throw new GlobalCustomException( HttpStatus.UNAUTHORIZED, "Access denied! Please provide Bearer Token in Authorization Header", exchange.getRequest().getURI().getPath());
        }

        final String consumerToken= authorizationToken.substring(7); // Remove Bearer
        final long endpointID= Integer.parseInt(Objects.requireNonNull(exchange.getRequest().getHeaders().getFirst(GlobalConstants.HEADER_ENDPOINT_ID)));

        log.info("consumerToken:"+consumerToken+" endpointID:"+endpointID);

        Optional<Subscription> subscriptionOptional= dataRepository.getSubscriptionByConsumerAndApi(consumerToken,endpointID);

        if(subscriptionOptional.isEmpty()) {
            log.warn("No Subscription found!");
            throw new GlobalCustomException(HttpStatus.UNAUTHORIZED, "Provide valid Bearer Token in Authorization Header", exchange.getRequest().getPath().toString());
        }
        else{
            log.info("Subscription found: "+subscriptionOptional.get().toString());
            String custom= exchange.getRequest().getHeaders().getFirst(GlobalConstants.HEADER_CUSTOM);
            if(custom==null)
                custom="test";
            ServerHttpRequest request = exchange.getRequest()
                    .mutate()
                    .header(GlobalConstants.HEADER_SUBSCRIPTION_ID, subscriptionOptional.get().getId()+"")
                    .header(GlobalConstants.HEADER_CONSUMER_ID, subscriptionOptional.get().getConsumer().getId()+"")
                    .header(GlobalConstants.HEADER_AUTHORIZATION, custom)
                    .build();

            exchange = exchange.mutate().request(request).build();
        }

        return chain.filter(exchange);

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
