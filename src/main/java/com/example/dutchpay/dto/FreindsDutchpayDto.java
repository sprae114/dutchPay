package com.example.dutchpay.dto;

import com.example.dutchpay.cotroller.DutchPayController;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import static com.example.dutchpay.service.InMemoryDutchPayService.dutchpayDb;
import static java.util.stream.Collectors.toList;

@ToString
public class FreindsDutchpayDto {

    private final List<Dutchpay> dutchpays;

    public FreindsDutchpayDto() {
        this.dutchpays = new ArrayList<>();
    }

    public List<Dutchpay> getDutchpays() {
        return dutchpays;
    }

    public void addDutchpay(Dutchpay dutchpay) {
        this.dutchpays.add(dutchpay);
    }

    public void calculate() {
        //참석 안한 사람을 제외한 인원수 세는 방법
        long count = dutchpays.stream().filter(Dutchpay::getIsPaid).count();

        //총 금액 구하기
        long totalMoney = dutchpays.stream().mapToLong(Dutchpay::getMoney).sum();
        DutchPayController.dutchpayMain.add(totalMoney);

        //각각의 금액 구하기
        long amountDue = Math.round((totalMoney / count) / 100.0) * 100;


        //각각의 금액을 ArrayList에 넣기
        List<Long> result = dutchpays.stream()
                .mapToLong(Dutchpay::getMoney)
                .boxed()
                .collect(toList());

        for (int i = 0; i < result.size(); i++) {
            if (dutchpays.get(i).getIsPaid()){
                result.set(i, result.get(i) - amountDue);
            }
        }

        dutchpayDb.add(result);
    }
}
