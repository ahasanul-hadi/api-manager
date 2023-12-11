package com.bnda.webapi.publisher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PublisherService {
    private final PublisherRepository publisherRepository;
    public final ApiEndPointRepository endPointRepository;

    public Publisher save(Publisher publisher){
        return publisherRepository.save(publisher);
    }

    public List<Publisher> findAll(){
        return publisherRepository.findAll();
    }

    public Optional<Publisher> findById(Long id){
        return publisherRepository.findById(id);
    }

    public ApiEndPoint saveEndPoint(EndPointDTO endPointDTO){
        Publisher publisher= findById(endPointDTO.getPublisherID()).orElseThrow();
        ApiEndPoint endPoint= new ApiEndPoint();
        endPoint.setPublisher(publisher);
        endPoint.setMethod(endPointDTO.getMethod());
        endPoint.setPath(endPointDTO.getPath());
        endPoint.setParameter(endPointDTO.getParameter());

        return endPointRepository.save(endPoint);
    }

    public void deleteById(Long id){
        publisherRepository.deleteById(id);
    }

    public void deleteEndPoint(Long endPointID){
        endPointRepository.deleteById(endPointID);
    }

}
