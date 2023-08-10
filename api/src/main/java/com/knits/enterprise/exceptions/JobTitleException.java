package com.knits.enterprise.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class JobTitleException extends AppException{

    public JobTitleException(String message){
        super(message);
    }

    public JobTitleException(Exception e){
        super(e);
    }
}
