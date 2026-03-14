package com.churninsight.api.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public class ModelServiceException extends RuntimeException{

    private final HttpStatus httpStatus;

    public ModelServiceException(HttpStatus httpStatus, String message ) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
