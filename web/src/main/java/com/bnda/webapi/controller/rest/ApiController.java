package com.bnda.webapi.controller.rest;

import com.bnda.webapi.api.ApiEndPoint;
import com.bnda.webapi.consumer.Consumer;
import com.bnda.webapi.service.ApiEndPointService;
import com.bnda.webapi.service.ConsumerService;
import com.bnda.webapi.service.SubscriptionService;
import com.bnda.webapi.subscription.Subscription;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiController {

    private final ConsumerService consumerService;
    private final SubscriptionService subscriptionService;
    private final ApiEndPointService apiEndPointService;


    @GetMapping("/consumers")
    ResponseEntity getConsumers(){
        List<Consumer> consumers= consumerService.findAll();
        return new ResponseEntity(consumers, HttpStatus.OK);
    }

    @GetMapping("/consumers/{id}")
    ResponseEntity getConsumers(@PathVariable("id") Long id){
        Consumer consumer= consumerService.findById(id);
        return new ResponseEntity(consumer, HttpStatus.OK);
    }


    @GetMapping("/subscriptions")
    ResponseEntity getSubscriptions(){
        List<Subscription> list= subscriptionService.findAll();
        return new ResponseEntity(list,HttpStatus.OK);
    }

    @GetMapping("/subscriptions/{consumerID}/{endPointID}")
    ResponseEntity getSpecificSubscription(){
        Subscription sub= new Subscription();
        return new ResponseEntity(sub,HttpStatus.OK);
    }

    @GetMapping("/endpoints")
    ResponseEntity getApiEndPoints(){
        List<ApiEndPoint> endPoints= apiEndPointService.findAll();
        return new ResponseEntity(endPoints,HttpStatus.OK);
    }





}
