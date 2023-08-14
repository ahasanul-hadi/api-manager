package com.bnda.apim.config;

import com.bnda.apim.service.ApiRouteService;
import com.bnda.apim.util.DataRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AppConfig {

    private final ApiRouteService apiRouteService;


    @Bean
    DataRepository loadData(){
        DataRepository data= new DataRepository();

        apiRouteService.findApiRoutes().collectList().subscribe(e->{
            log.info("apis:"+e.toString());
            data.setAllEndPoints(e);
        });

        apiRouteService.findAllSubscriptions().collectList().subscribe(e->{
            log.info("subscriptions:"+e.toString());
            data.setAllSubscription(e);
        });
        return data;
    }




}
