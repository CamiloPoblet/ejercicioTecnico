package com.camilopoblete.ejerciciotecnico.exceptions;

public class AlreadyExistsException extends RuntimeException{

    private final String errorCode;

    public AlreadyExistsException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }


}
