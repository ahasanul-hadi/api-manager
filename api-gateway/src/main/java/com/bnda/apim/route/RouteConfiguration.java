package com.bnda.apim.route;


import com.bnda.apim.filter.AuthenticationFilter;
import com.bnda.apim.filter.LogFilter;
import com.bnda.apim.filter.RateLimiterFilter;
import com.bnda.apim.service.ApiRouteService;
import com.bnda.apim.util.DataRepository;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfiguration {

   /*@Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("id", p -> p
                        .readBody(String.class, i->true)
                        .and()
                        .path("/boithok/test/api/**")
                        .filters(f -> f.addRequestHeader("Hello", "World").filter(new LogFilter()))
                        .uri("https://bnda.gov.bd")
                )
                .build();
    }*/


    @Bean
    public RouteLocator routeLocator(AuthenticationFilter authFilter, RateLimiterFilter rateLimiterFilter, LogFilter logFilter, DataRepository dataRepository, ApiRouteService apiRouteService,
                                     RouteLocatorBuilder routeLocatorBuilder) {

        return new CustomRouteLocator(authFilter, rateLimiterFilter, logFilter, dataRepository, apiRouteService, routeLocatorBuilder);
    }

}
