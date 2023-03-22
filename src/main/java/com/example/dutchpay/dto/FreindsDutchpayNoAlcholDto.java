package com.example.dutchpay.dto;

import com.example.dutchpay.cotroller.DutchPayController;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import static com.example.dutchpay.service.InMemoryDutchPayService.dutchpayDb;
import static java.lang.Math.max;
import static java.util.stream.Collectors.toList;

@ToString
public class FreindsDutchpayNoAlcholDto {
    private final List<DutchpayNoAlchol> dutchpaysNoAlchol;
    Long sumMoney;

    public FreindsDutchpayNoAlcholDto() {
        dutchpaysNoAlchol = new ArrayList<>();
        sumMoney = 0L;
    }

    public List<DutchpayNoAlchol> getDutchpaysAlchol() {
        return dutchpaysNoAlchol;
    }

    public void addDutchpayAlchol(DutchpayNoAlchol dutchpayNoAlchol) {
        this.dutchpaysNoAlchol.add(dutchpayNoAlchol);
    }

    public void calculate() {
        List<Long> sumSnackMoney = getSnackMoney();
        List<Long> sumAlcholMoney = getAlcholMoney();

        //더치페이 금액 구하기
        for (int i = 0; i < sumSnackMoney.size(); i++) {
            sumSnackMoney.set(i, sumSnackMoney.get(i) + sumAlcholMoney.get(i));
        }

        //총 금액 넣기
        DutchPayController.dutchpayMain.add(sumMoney);
        dutchpayDb.add(sumSnackMoney);
    }

    private List<Long> getSnackMoney() {

        //참석 안한 사람을 제외한 인원수 세는 방법
        long count = dutchpaysNoAlchol.stream().filter(DutchpayNoAlchol::getIsPaid).count();

        //총 금액 구하기
        long totalMoney = dutchpaysNoAlchol.stream().mapToLong(DutchpayNoAlchol::getMoney).sum();
        sumMoney += totalMoney;

        //각각의 금액 구하기
        long amountDue = Math.round((totalMoney / max(1, count)) / 100.0) * 100;

        //각각의 금액을 ArrayList에 넣기
        List<Long> result = dutchpaysNoAlchol.stream()
                .mapToLong(DutchpayNoAlchol::getMoney)
                .boxed()
                .collect(toList());

        for (int i = 0; i < result.size(); i++) {
            if (dutchpaysNoAlchol.get(i).getIsPaid()){
                result.set(i, result.get(i) - amountDue);
            }
        }

        return result;
    }

    private List<Long> getAlcholMoney() {

        //체크한 사람 제외해서 인원수 세는 방법
        long count = dutchpaysNoAlchol.stream().filter(dutchpay -> !dutchpay.isAlchol).count();

        //총 금액 구하기
        long totalMoney = dutchpaysNoAlchol.stream().mapToLong(DutchpayNoAlchol::getAlcholMoney).sum();
        sumMoney += totalMoney;

        //각각의 금액 구하기
        long amountDue = Math.round((totalMoney / max(1, count)) / 100.0) * 100;

        //각각의 금액을 ArrayList에 넣기
        List<Long> result = dutchpaysNoAlchol.stream()
                .mapToLong(DutchpayNoAlchol::getAlcholMoney)
                .boxed()
                .collect(toList());

        for (int i = 0; i < result.size(); i++) {
            if (!dutchpaysNoAlchol.get(i).getIsAlchol()){
                result.set(i, result.get(i) - amountDue);
            }
        }

        return result;
    }
}
