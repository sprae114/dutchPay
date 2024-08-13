package com.example.dutchpay.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CodeError {
    NOT_FOUND_USER("404", "Not Found"),
    NOT_FOUND_FRIEND("404", "Not Found"),
    TYPE_MISMATCH("400", "Type Mismatch");

    private String code;
    private String message;
}
