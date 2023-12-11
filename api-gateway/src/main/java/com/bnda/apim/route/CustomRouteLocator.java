package com.bnda.apim.route;

import com.bnda.apim.filter.AuthenticationFilter;
import com.bnda.apim.filter.LogFilter;
import com.bnda.apim.filter.RateLimiterFilter;
import com.bnda.apim.util.DataRepository;
import com.bnda.apim.util.GlobalConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import reactor.core.publisher.Flux;

import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
public class CustomRouteLocator implements RouteLocator {

    private final AuthenticationFilter authFilter;
    private final RateLimiterFilter rateLimiterFilter;
    private final LogFilter logFilter;
    private final RouteService routeService;
    private final RouteLocatorBuilder routeLocatorBuilder;

    @Override
    public Flux<Route> getRoutes() {


        RouteLocatorBuilder.Builder routesBuilder = routeLocatorBuilder.routes();

        return routeService.findApiRoutes()
                .map(apiRoute -> routesBuilder.route(String.valueOf(apiRoute.getId()),
                        p ->
                                p.path(apiRoute.getPath())
                                .filters(f ->
                                        f.addRequestHeader(GlobalConstants.HEADER_ENDPOINT_ID,apiRoute.getId()+"")
                                        .filter(authFilter)
                                        .filter(rateLimiterFilter)
                                        .cacheRequestBody(String.class)
                                        .filter(logFilter)
                                )
                                .uri(apiRoute.getPublisher().getBaseUri())

                ))
                .collectList()
                .flatMapMany(builders -> routesBuilder.build().getRoutes());

    }
}
