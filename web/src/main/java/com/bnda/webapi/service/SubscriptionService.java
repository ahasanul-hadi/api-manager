package com.bnda.webapi.service;

import com.bnda.webapi.repository.SubscriptionRepository;
import com.bnda.webapi.subscription.Subscription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public List<Subscription> findAll(){
        return subscriptionRepository.findAll();
    }
}
