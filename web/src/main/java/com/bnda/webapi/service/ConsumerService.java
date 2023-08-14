package com.bnda.webapi.service;

import com.bnda.webapi.consumer.Consumer;
import com.bnda.webapi.exception.ApplicationException;
import com.bnda.webapi.repository.ConsumerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsumerService {
    private final ConsumerRepository consumerRepository;

    public Consumer findById(long id){
        return consumerRepository.findById(id).orElseThrow(()-> new ApplicationException("Consumer Not found with id:"+id, HttpStatus.NOT_FOUND));
    }
    public List findAll(){
        return consumerRepository.findAll();
    }
}
