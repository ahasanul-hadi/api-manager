package com.bnda.webapi.publisher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApiEndPointService {
    private final ApiEndPointRepository apiEndPointRepository;

     public List<ApiEndPoint> findAll(){
        return apiEndPointRepository.findAll();
    }

    public Optional<ApiEndPoint> findById(Long id){
         return apiEndPointRepository.findById(id);
    }
}
