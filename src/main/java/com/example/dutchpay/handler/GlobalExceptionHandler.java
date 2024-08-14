package com.example.dutchpay.handler;

import com.example.dutchpay.exception.ErrorDetails;
import com.example.dutchpay.exception.TypeMismatchException;
import com.example.dutchpay.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.UnknownServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(UserNotFoundException e) {
        ErrorDetails errorDetails = new ErrorDetails(e.getMessage(), LocalDateTime.now().toString());
        log.error("UserNotFoundException: {}", errorDetails);

        return "error/4xx";
    }

    @ExceptionHandler(TypeMismatchException.class)
    public String handleTypeMismatchException(TypeMismatchException e, Model model) {
        ErrorDetails errorDetails = new ErrorDetails(e.getMessage(), LocalDateTime.now().toString());
        log.error("TypeMismatchException: {}", errorDetails);

        return "error/4xx";
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        ErrorDetails errorDetails = new ErrorDetails(e.getMessage(), LocalDateTime.now().toString());
        log.error("MissingServletRequestParameterException: {}", errorDetails);

        return "error/4xx";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorDetails errorDetails = new ErrorDetails(e.getMessage(), LocalDateTime.now().toString());
        log.error("IllegalArgumentException: {}", errorDetails);

        return "error/5xx";
    }
}

