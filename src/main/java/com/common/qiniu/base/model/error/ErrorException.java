package com.common.qiniu.base.model.error;

public class ErrorException extends RuntimeException{
    private ErrorResponse errorException;

    public ErrorResponse getErrorException() {
        return errorException;
    }

    public void setErrorException(ErrorResponse errorException) {
        this.errorException = errorException;
    }

    public ErrorException(String message) {
        super(message);
    }
}
