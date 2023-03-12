package com.example.dutchpay.dto;

import lombok.Data;

@Data
public class DutchpayNoAlchol {
    public String name;
    public Long money = 0L;
    public Long alcholMoney = 0L;
    public Boolean isPaid = true;
    public Boolean isAlchol = false;

    public DutchpayNoAlchol() {
    }

    public DutchpayNoAlchol(String name) {
        this.name = name;
    }
}
