package com.example.dutchpay.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {

    private CodeError code;
    private String info;

    public UserNotFoundException(CodeError codeError) {
        super(codeError.getMessage()); // 부모 클래스의 생성자를 호출
        this.code = codeError;
    }

    public UserNotFoundException(CodeError codeError, String info) {
        super(codeError.getMessage()); // 부모 클래스의 생성자를 호출
        this.code = codeError;
        this.info = info;
    }
}
