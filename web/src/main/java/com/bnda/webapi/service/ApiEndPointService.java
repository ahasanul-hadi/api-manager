package com.bnda.webapi.service;

import com.bnda.webapi.api.ApiEndPoint;
import com.bnda.webapi.api.ApiPublisher;
import com.bnda.webapi.api.MethodType;
import com.bnda.webapi.repository.ApiEndPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiEndPointService {
    private final ApiEndPointRepository apiEndPointRepository;

     public List<ApiEndPoint> findAll(){
        return apiEndPointRepository.findAll();
    }
}
