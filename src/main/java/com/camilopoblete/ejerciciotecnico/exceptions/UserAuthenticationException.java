package com.camilopoblete.ejerciciotecnico.exceptions;

public class UserAuthenticationException extends RuntimeException{
    private final int errorCode;

    public UserAuthenticationException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }


}
