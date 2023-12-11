package com.bnda.microservices.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/payment/api")
@RequiredArgsConstructor
public class RestControllers {

    @Value("${server.port}")
    private int serverPort;


    @GetMapping("/amount/{data}")
    ResponseEntity<?> test(@PathVariable(name = "data", required = false) String data){
        Map<String, String> map= new HashMap<>();
        map.put("message","Payment amount:"+ data+" Confirmed!");

        log.info("called from port:"+serverPort+" with data:"+data);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
