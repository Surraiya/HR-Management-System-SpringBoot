package com.knits.enterprise.exceptions;

public class JobTitleException extends AppException{

    public JobTitleException(String message){
        super(message);
    }

    public JobTitleException(Exception e){
        super(e);
    }
}
