package com.bnda.apim.filter;

import com.bnda.apim.util.GlobalConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomKeyResolver implements KeyResolver {

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {

        String keyResolverID= exchange.getRequest().getHeaders().getFirst(GlobalConstants.HEADER_SUBSCRIPTION_ID);
        log.info("keyResolverID:"+keyResolverID);
        assert keyResolverID != null;
        return Mono.just(keyResolverID);
    }
}
