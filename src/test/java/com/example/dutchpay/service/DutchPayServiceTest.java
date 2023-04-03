package com.example.dutchpay.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static com.example.dutchpay.service.DutchPayService.dutchpayTotal;
import static com.example.dutchpay.service.DutchPayService.minTransfers;
import static com.example.dutchpay.service.InMemoryDutchPayService.dutchpayDb;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class DutchPayServiceTest {

    final DutchPayService dutchPayService;


    @Autowired
    public DutchPayServiceTest(DutchPayService dutchPayService) {
        this.dutchPayService = dutchPayService;
    }


    @BeforeEach
    void beforeEach(){
        //금액 넣기
        List<Long> list1 = new ArrayList<>(List.of(8000L, -2000L, -2000L, -2000L, -2000L));
        List<Long> list2 = new ArrayList<>(List.of(-8000L, 40000L, -8000L, -8000L, -8000L));
        dutchpayDb.add(list1);
        dutchpayDb.add(list2);
    }

    @AfterEach
    void afterEach(){
        dutchPayService.dutchinit();
    }


    @DisplayName("금액 합치기 - 성공")
    @Test
    void calculateDutchPayMoneyTest() {
        dutchPayService.calculateDutchPayMoney();

        Assertions.assertThat(dutchpayTotal).isEqualTo(List.of(0L, 38000L, -10000L, -10000L, -10000L));
    }

    @DisplayName("최소 이체 구현- 성공")
    @Test
    void minTransfersTest() {
        List<Long> list = List.of(0L, 38000L, -10000L, -10000L, -10000L);
        List<String> names = new ArrayList<>(List.of("A", "B", "C", "D", "E"));

        List<String> result = minTransfers(list, names);

        Assertions.assertThat(result)
                .isEqualTo(List.of("C -> B에게 보낼 금액은 : 10,000원 입니다."
                        , "D -> B에게 보낼 금액은 : 10,000원 입니다."
                        ,  "E -> B에게 보낼 금액은 : 10,000원 입니다."));
    }
}