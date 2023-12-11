package com.bnda.apim.route;


import com.bnda.apim.filter.AuthenticationFilter;
import com.bnda.apim.filter.LogFilter;
import com.bnda.apim.filter.RateLimiterFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class RouteConfiguration {

    @Bean
    public RouteLocator routeLocator(AuthenticationFilter authFilter, RateLimiterFilter rateLimiterFilter, LogFilter logFilter,
                                     RouteService apiRouteService, RouteLocatorBuilder routeLocatorBuilder) {

        return new CustomRouteLocator(authFilter, rateLimiterFilter, logFilter, apiRouteService, routeLocatorBuilder);
    }

}
