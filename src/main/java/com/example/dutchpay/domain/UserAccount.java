package com.example.dutchpay.domain;

import lombok.Data;

@Data
public class UserAccount {

    public Long id;
    public String name;

    public String email;
    public String password;
}
