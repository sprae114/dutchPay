package com.example.dutchpay.dto;

import lombok.Data;

@Data
public class Dutchpay {
    public String name;
    public Long money = 0L;
    public Boolean isPaid = true;

    public Dutchpay() {
    }

    public Dutchpay(String name) {
        this.name = name;
    }
}
