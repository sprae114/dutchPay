package com.example.dutchpay.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DutchResultService {
    public static List<Long> dutchpayTotal = new ArrayList<>();

    public void save() {
        // todo : 저장하기
    }

    //todo : 값을 더한후 나누기 계산하기
    public String calculate(){
        return "x";
    }

    // todo : 정산기능
    public void calculateDutchpay() {

    }
}
