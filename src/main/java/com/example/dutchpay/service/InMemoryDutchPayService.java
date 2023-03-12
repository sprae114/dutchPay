package com.example.dutchpay.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InMemoryDutchPayService {
    public static ArrayList<List<Long>> dutchpayDb;  //[0, [0,-2000,4000,-2000]]과 같이 결과가 나오게

    public InMemoryDutchPayService() {
        dutchpayDb = new ArrayList<>();
    }
}
