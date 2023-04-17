package com.example.dutchpay.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingServletRequestParameterException(MissingServletRequestParameterException ex, Model model) {

        log.error("Missing parameter: " + ex.getParameterName());
        model.addAttribute("error", "친구를 선택하지 않아 오류가 발생했습니다.");

        return "error/error";
    }
}

