package com.bnda.apim.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;

@Component
@Slf4j
public class GlobalErrorAttributes extends DefaultErrorAttributes  {


    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest  request, ErrorAttributeOptions options) {
        Throwable error = this.getError(request);
        Map<String, Object> map = super.getErrorAttributes(request, options);
        if(error instanceof GlobalCustomException exp){
            map.put(ErrorAttributesKey.ERROR.getKey(),exp.getMessage());
            map.put(ErrorAttributesKey.STATUS.getKey(), exp.getStatus().value());
        }
        else{
            MergedAnnotation<ResponseStatus> responseStatusAnnotation = MergedAnnotations.from(error.getClass(), MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).get(ResponseStatus.class);
            HttpStatus errorStatus = findHttpStatus(error, responseStatusAnnotation);
            map.put(ErrorAttributesKey.STATUS.getKey(),errorStatus.value());
        }
        return map;

    }

    private HttpStatus findHttpStatus(Throwable error, MergedAnnotation<ResponseStatus> responseStatusAnnotation) {
        if(error instanceof GlobalCustomException exp){
            return exp.status;
        }
        else if (error instanceof ResponseStatusException ex) {
            return HttpStatus.valueOf(ex.getStatusCode().value());
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

}


