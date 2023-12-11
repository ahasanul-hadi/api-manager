package com.bnda.apim.filter;

import com.bnda.apim.exception.GlobalCustomException;
import com.bnda.apim.util.DataRepository;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class RateLimiterFilter implements GatewayFilter, Ordered {
    private final RateLimiter<?> rateLimiter;
    private final KeyResolver keyResolver;


    public RateLimiterFilter(final KeyResolver keyResolver, final CustomRateLimiter customRateLimiter) {
        this.rateLimiter = customRateLimiter;
        this.keyResolver = keyResolver;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

            Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);

            return keyResolver.resolve(exchange).flatMap(key -> {
                log.info("subscription Key:"+key);
                assert route != null;
                Mono<RateLimiter.Response> result = rateLimiter.isAllowed(route.getId(), key);
                return result.flatMap(response -> {

                    response.getHeaders().forEach((k, v) -> exchange.getResponse().getHeaders().add(k, v));

                    if (response.isAllowed()) {
                        return chain.filter(exchange);
                    }
                    throw new GlobalCustomException(HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests", exchange.getRequest().getPath().toString());
                });
            });

    }


    @Override
    public int getOrder() {
        return 1;
    }
}
