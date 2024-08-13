package com.example.dutchpay.exception;

import lombok.Getter;

@Getter
public class TypeMismatchException extends RuntimeException {

    private CodeError code;

    public TypeMismatchException(String message) {
        super(message);
    }

    public TypeMismatchException(CodeError codeError) {
        super(codeError.getMessage());
        this.code = codeError;
    }
}
