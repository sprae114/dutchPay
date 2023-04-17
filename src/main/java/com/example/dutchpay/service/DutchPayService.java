package com.example.dutchpay.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.dutchpay.controller.DutchPayController.dutchpayMain;
import static com.example.dutchpay.service.InMemoryDutchPayService.dutchpayDb;
@Transactional
@Service
public class DutchPayService {
    public static List<Long> dutchpayTotal = new ArrayList<>();

    public String calculateDutchPayMoney(){
        sumDutchPayMoney();

        Long number = dutchpayMain.stream().reduce(0L, Long::sum);
        return String.format("%,d", number);
    }

    public void dutchinit() {
        dutchpayDb.clear();  //각각 금액 저장
        dutchpayTotal.clear();  //계산할 총금액
        dutchpayMain.clear();  //목록
    }

    //최소이체로 이체하기
    public static List<String> minTransfers(List<Long> balances, List<String> names) {

        if (balances == null || names == null) {
            return new ArrayList<>();
        }

        if (balances.isEmpty() || names.isEmpty()) {
            return new ArrayList<>();
        }

        int n = balances.size();

        List<String> res = new ArrayList<>();

        // 0을 제외한 절대값이 같은 경우, 각각 요소를 0으로 만들기
        List<Long> tempBalances = new ArrayList<>(balances);
        for (int i = 0; i < n; i++) {
            if (tempBalances.get(i) != 0L) {
                for (int j = i + 1; j < n; j++) {
                    if (Math.abs(tempBalances.get(i)) == Math.abs(tempBalances.get(j))) {
                        if (tempBalances.get(i) > 0) {
                            res.add(names.get(j) + " -> " + names.get(i) + "에게 보낼 금액은 : " + String.format("%,d", Math.abs(tempBalances.get(i))) + "원 입니다.");
                            tempBalances.set(i, 0L);
                            tempBalances.set(j, 0L);
                        } else if (tempBalances.get(j) > 0) {
                            res.add(names.get(i) + " -> " + names.get(j) + "에게 보낼 금액은 : " + String.format("%,d", Math.abs(tempBalances.get(i))) + "원 입니다.");
                            tempBalances.set(i, 0L);
                            tempBalances.set(j, 0L);
                        }
                    }
                }
            }
        }

        // 송금 거래 처리
        int count = n;
        while (count > 0) {
            int maxIndex = 0;
            boolean found = false;

            // 가장 큰 금액 찾기
            for (int i = 1; i < n; i++) {
                if (tempBalances.get(i) > tempBalances.get(maxIndex)) {
                    maxIndex = i;
                }
            }

            // 큰 금액이 0이 될 때까지 반복
            while (tempBalances.stream().filter(balance -> balance != 0).count() != 1) {
                found = true;
                int minIndex = 0;

                // 최소 금액을 가지는 참가자 선택
                for (int j = 1; j < n; j++) {
                    if (tempBalances.get(j) + tempBalances.get(maxIndex) == 0) {
                        minIndex = j;
                        break;
                    }

                    if (tempBalances.get(j) < tempBalances.get(minIndex)) {
                        minIndex = j;
                    }
                }

                if (tempBalances.get(minIndex) == 0 && tempBalances.get(maxIndex) == 0) {
                    break;
                }

                // 송금 처리
                Long amount = Math.min(-tempBalances.get(minIndex), tempBalances.get(maxIndex));

                res.add(names.get(minIndex) + " -> " + names.get(maxIndex) + "에게 보낼 금액은 : " + String.format("%,d", amount) + "원 입니다.");
                tempBalances.set(minIndex, tempBalances.get(minIndex) + amount);
                tempBalances.set(maxIndex, tempBalances.get(maxIndex) - amount);
            }

            if (!found) {
                break;
            }

            count--;
        }

        return res;
    }


    public String printDutchPay(String totalMoney, List<String> dutchPayResult){
        StringBuilder sb = new StringBuilder();
        sb.append("총 계산 금액은 " + totalMoney + "원 입니다.<br>");

        for (String dutchPay : dutchPayResult) {
            sb.append(dutchPay + "<br>");
        }

        return sb.toString();
    }

    private void sumDutchPayMoney() {

        //0일때 제외
        if (dutchpayDb.size() == 0) {
            return;
        }

        dutchpayTotal = dutchpayDb.get(0);

        for (int i = 1; i < dutchpayDb.size(); i++) {
            for (int j = 0; j < dutchpayDb.get(i).size(); j++) {
                dutchpayTotal.set(j, dutchpayTotal.get(j) + dutchpayDb.get(i).get(j));
            }
        }
    }
}
