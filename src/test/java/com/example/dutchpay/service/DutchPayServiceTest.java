package com.example.dutchpay.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static com.example.dutchpay.service.DutchPayService.dutchpayTotal;
import static com.example.dutchpay.service.DutchPayService.minTransfers;
import static com.example.dutchpay.service.InMemoryDutchPayService.dutchpayDb;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class DutchPayServiceTest {

    @InjectMocks DutchPayService dutchPayService;

    @BeforeEach
    void beforeEach(){
        dutchpayDb = new ArrayList<>();
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

        assertThat(dutchpayTotal).isEqualTo(List.of(0L, 38000L, -10000L, -10000L, -10000L));
    }

    @DisplayName("리스트가 없는 상태에서 금액 합치기")
    @Test
    void calculateDutchPayNoMoneyTest(){
        dutchpayDb.clear();
        dutchPayService.calculateDutchPayMoney();

        assertThat(dutchpayTotal).isEqualTo(List.of());
    }

    @DisplayName("최소 이체 구현- 성공")
    @Test
    void minTransfersTest() {
        //given
        List<Long> list = List.of(0L, 38000L, -10000L, -10000L, -10000L);
        List<String> names = new ArrayList<>(List.of("A", "B", "C", "D", "E"));

        //when
        List<String> result = minTransfers(list, names);

        //then
        assertThat(result)
                .isEqualTo(List.of("C -> B에게 보낼 금액은 : 10,000원 입니다."
                        , "D -> B에게 보낼 금액은 : 10,000원 입니다."
                        ,  "E -> B에게 보낼 금액은 : 10,000원 입니다."));
    }

    @DisplayName("데이터가 없는 상태에서 최소구현 이체")
    @Test
    void noDataMinTransfersTest() {
        //given
        List<Long> list = List.of(0L, 0L, 0L, 0L, 0L);
        List<String> names = new ArrayList<>(List.of("A", "B", "C", "D", "E"));

        //when
        List<String> result = minTransfers(list, names);

        //then
        assertThat(result).isEqualTo(List.of());
    }
}