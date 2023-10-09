package com.project.reservation.exception;

public class ServiceException extends RuntimeException{
    private ErrorCode errorCode;
    public ServiceException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    public ErrorCode getErrorCode(){
        return errorCode;
    }
}
