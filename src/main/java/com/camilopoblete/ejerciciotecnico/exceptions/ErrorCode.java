package com.camilopoblete.ejerciciotecnico.exceptions;

public enum ErrorCode
{

        USER_CREATION_FAILURE(1000,"Error al momento de intentar crear el usuario"),
        INVALID_PASSWORD_FORMAT(1001, "La contraseña debe tener una Mayúscula,dos números y tener un largo entre 8 y 12"),
        USER_ALREADY_EXISTS(1002, "Usuario ya existe en el sistema"),
        AUTHENTICATION_FAILED(2001, "Autenticacion fallida");

        private final int code;
        private final String message;

        // Constructor
        ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

        public int getCode() {
        return code;
    }

        public String getMessage() {
        return message;
    }


}
