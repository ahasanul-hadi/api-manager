package com.bnda.microservices.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/order/api")
@RequiredArgsConstructor
public class RestControllers {

    @Value("${server.port}")
    private int serverPort;
    @GetMapping("/{data}")
    ResponseEntity<?> test(@PathVariable(name = "data") Optional<String> data) throws InterruptedException {

        Map<String, String> map= new HashMap<>();
        map.put("message","Order Service port:"+serverPort+"--> Confirmed Order "+ data.orElse("World"));
        log.info("called from port:"+serverPort+" with order:"+data.orElse("Nothing"));
        return ResponseEntity.ok(map);
    }


    @GetMapping("/sleep/{sleep}")
    ResponseEntity<?> sleep(@PathVariable(name = "sleep") double sleep) throws InterruptedException {

        Map<String, String> map= new HashMap<>();
        map.put("request-time", LocalDateTime.now().toString());
        Thread.sleep((long)(sleep*1000));
        map.put("response-time", LocalDateTime.now().toString());
        return ResponseEntity.ok(map);
    }

}
