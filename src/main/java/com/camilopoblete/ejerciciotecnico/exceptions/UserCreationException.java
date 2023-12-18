package com.camilopoblete.ejerciciotecnico.exceptions;

public class UserCreationException extends RuntimeException{

    private final int errorCode;

    public UserCreationException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

}
