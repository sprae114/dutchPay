package com.example.dutchpay.service;

import com.example.dutchpay.cotroller.DutchPayController;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.dutchpay.cotroller.DutchPayController.dutchpayMain;
import static com.example.dutchpay.service.InMemoryDutchPayService.dutchpayDb;

@Service
public class DutchResultService {
    public static List<Long> dutchpayTotal = new ArrayList<>();

    //todo : 값을 더한후 나누기 계산하기
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
        int n = balances.size();
        List<String> res = new ArrayList<>();

        //0을 제외한 절대값이 같은 경우, 각각 요소를 0으로 만들기
        for (int i = 0; i < n; i++) {
            if (balances.get(i) != 0) {
                for (int j = i + 1; j < n; j++) {
                    if ((Math.abs(balances.get(i)) == Math.abs(balances.get(j))) && (balances.get(i) > 0)) {
                        res.add(names.get(j) + " -> " + names.get(i) + "에게 보낼 금액은 : " + String.format("%,d", Math.abs(balances.get(i))) + "원 입니다.");
                        balances.set(i, 0L);
                        balances.set(j, 0L);
                    }

                    if ((Math.abs(balances.get(i)) == Math.abs(balances.get(j))) && (balances.get(j) > 0)) {
                        res.add(names.get(i) + " -> " + names.get(j) + "에게 보낼 금액은 : " + String.format("%,d", Math.abs(balances.get(i))) + "원 입니다.");
                        balances.set(i, 0L);
                        balances.set(j, 0L);
                    }
                }
            }
        }


        while (true) {
            //가장 큰 금액 찾기
            int maxIndex = 0;
            Boolean temp = false;

            for (int i = 1; i < n; i++) {
                if (balances.get(i) > balances.get(maxIndex)) {
                    maxIndex = i;
                }
            }

            //큰 금액이 0이 될때까지 반복
            while (balances.get(maxIndex) != 0L) {
                temp = true;
                int minIndex = 0;

                for (int j = 1; j < n; j++) {
                    if (balances.get(j) + balances.get(maxIndex) == 0) {
                        minIndex = j;
                        break;
                    }

                    if (balances.get(j) < balances.get(minIndex)) {
                        minIndex = j;
                    }
                }

                if (balances.get(minIndex) == 0 && balances.get(maxIndex) == 0) {
                    break;
                }

                Long amount = Math.min(-balances.get(minIndex), balances.get(maxIndex));

                res.add(names.get(minIndex) + " -> " + names.get(maxIndex) + "에게 보낼 금액은 : " + String.format("%,d", amount) + "원 입니다.");
                balances.set(minIndex, balances.get(minIndex) + amount);
                balances.set(maxIndex, balances.get(maxIndex) - amount);
            }

            //종료조건
            if (temp.equals(false)) {
                break;
            }
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
        dutchpayTotal = dutchpayDb.get(0);

        for (int i = 1; i < dutchpayDb.size(); i++) {
            for (int j = 0; j < dutchpayDb.get(i).size(); j++) {
                dutchpayTotal.set(j, dutchpayTotal.get(j) + dutchpayDb.get(i).get(j));
            }
        }
    }
}
