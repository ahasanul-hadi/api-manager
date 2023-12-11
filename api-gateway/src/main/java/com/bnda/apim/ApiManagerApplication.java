package com.bnda.apim;


import com.bnda.apim.route.RouteService;
import com.bnda.apim.util.DataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class ApiManagerApplication {

	private final RouteService routeService;
	private final DataRepository dataRepository;

	@Value("${spring.application.name}")
	private String appName;

	public static void main(String[] args) {
		SpringApplication.run(ApiManagerApplication.class, args);

	}

	@Bean
	CommandLineRunner init(){
		return args->{
			log.info("appName:"+appName);

			routeService.findApiRoutes().collectList().subscribe(e->{
				log.info("api routes:"+e.toString());
				dataRepository.setAllEndPoints(e);
			});

			routeService.findAllSubscriptions().collectList().subscribe(e->{
				log.info("subscriptions:"+e.toString());
				dataRepository.setAllSubscription(e);
			});
		};
	}


}
