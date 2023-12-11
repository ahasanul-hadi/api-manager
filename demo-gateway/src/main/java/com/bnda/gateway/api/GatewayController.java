package com.bnda.gateway.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GatewayController {

    @GetMapping("/fallback")
    public ResponseEntity<?> test(){
        return new ResponseEntity<>("Backend service is not responding. this is custom message", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping("/fallback2")
    public ResponseEntity<?> test2(){
        return new ResponseEntity<>("Default fallback payment response.", HttpStatus.SERVICE_UNAVAILABLE);
    }
}
