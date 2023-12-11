package com.bnda.webapi.consumer;

import com.bnda.webapi.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsumerService {
    private final ConsumerRepository consumerRepository;

    public Consumer findById(long id) throws ApplicationException {
        return consumerRepository.findById(id).orElseThrow(()-> new ApplicationException("Consumer Not found with id:"+id, HttpStatus.NOT_FOUND));
    }
    public List findAll(){
        return consumerRepository.findAll();
    }

    public void save(Consumer consumer) {
        consumerRepository.save(consumer);
    }

    public void deleteById(Long id) {
        consumerRepository.deleteById(id);
    }
}
