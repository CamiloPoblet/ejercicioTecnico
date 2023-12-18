package com.camilopoblete.ejerciciotecnico.exceptions;

import com.camilopoblete.ejerciciotecnico.exceptions.AlreadyExistsException;
import com.camilopoblete.ejerciciotecnico.exceptions.UserCreationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,final HttpServletResponse response) {
        try {
            System.out.println(ex.getMessage());
            System.out.println(ex.getBindingResult());
            List<FieldError> list = ex.getFieldErrors();
            String errorFields="field with errors:";
            for (FieldError fieldError: list) {
                errorFields+=fieldError.getField()+",";
            }
            errorFields=errorFields.substring(0,errorFields.length()-1);
            response.getWriter().write(createErrorJSONString(1000, errorFields));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<String> handleAlreadyExistsException(AlreadyExistsException e){
        return new ResponseEntity<>("el usuario ya existe en sistema", HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(UserCreationException.class)
    public void handleUserCreationException(final Exception e, final HttpServletResponse response){

        UserCreationException uce=((UserCreationException) e);
        log.debug("handleUserCreationException: codigo (%n) mensaje (%s) ",uce.getErrorCode(),uce.getMessage());
        try {
            response.getWriter().write(createErrorJSONString(uce.getErrorCode(), uce.getMessage()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @ResponseBody
    @ExceptionHandler(UserAuthenticationException.class)
    public void handleUserAuthenticationException(final Exception e, final HttpServletResponse response){

        UserAuthenticationException uce=((UserAuthenticationException) e);
        log.debug("handleUserUserAuthenticationException: codigo (%n) mensaje (%s) ",uce.getErrorCode(),uce.getMessage());
        try {
            response.getWriter().write(createErrorJSONString(uce.getErrorCode(), uce.getMessage()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

   public String createErrorJSONString(int codigo,String detail)
   {
       return "{ \"error\": [{ \"timestamp\": \""+new Date().toString()+"\", \"codigo\": "+codigo+", \"detail\":\""+detail+"\" }] }";
   }

}
