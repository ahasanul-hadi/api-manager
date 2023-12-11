package com.bnda.apim.route;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class RefreshController {

    private final RouteService routeService;

    @GetMapping("/api/route/refresh")
    public Mono<ResponseEntity<?>> refreshRoute() {
        routeService.refreshRoutes();
        return Mono.just(new ResponseEntity<>("OK", HttpStatus.OK));
    }
}
