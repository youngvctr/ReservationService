package com.project.reservation.exception;

import lombok.*;

@Getter
public class ErrorResponse {

    private int status;
    private String message;
    private String code;

    public ErrorResponse(final ErrorCode errorCode){
        this.status = errorCode.getStatus();
        this.message = errorCode.getMessage();
        this.code = errorCode.getCode();
    }

    public static ErrorResponse of(final ErrorCode errorCode){
        return new ErrorResponse(errorCode);
    }
}
