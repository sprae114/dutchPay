package com.example.dutchpay.controller;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;


public class SettlementAlgorithmTest {

    @Test
    void test() {
        // given
        List<Long> dutchpayTotal = Arrays.asList(0L, -2000L, 4000L, -2000L);
        BigMoney(dutchpayTotal);
        // when

        // then
    }

    static int BigMoney(List<Long> dutchpayTotal) {
        int totalCase = 0;

        dutchpayTotal.sort(Long::compareTo);

        int i = searchBigMoney(dutchpayTotal, totalCase);

        return i;
    }

    static String SamePaidAfterBigMoney(List<Long> dutchpayTotal) {
        return "null";
    }

    static String dividedMoneyAfterBigMoney(List<Long> dutchpayTotal) {
        return "null";
    }


    private static int searchBigMoney(List<Long> dutchpayTotal, int totalCase) {

        int left = 0;
        int right = dutchpayTotal.size() - 1;

        while (left < right) {
            long leftMoney = dutchpayTotal.get(left);
            long rightMoney = dutchpayTotal.get(right);

            if (leftMoney + rightMoney == 0) {
                dutchpayTotal.set(left, 0L);
                dutchpayTotal.set(right, 0L);
                left++;
                right--;
                totalCase++;

            } else if (leftMoney + rightMoney > 0) {
                dutchpayTotal.set(left, leftMoney + rightMoney);
                dutchpayTotal.set(right, 0L);
                left++;
                totalCase++;
            }

            else {
                dutchpayTotal.set(left, 0L);
                dutchpayTotal.set(right, leftMoney + rightMoney);
                right--;
                totalCase++;
            }
        }

        return totalCase;
    }

    private static List<Long> searchSamePaid(List<Long> dutchpayTotal) {
        for (int i = 0; i < dutchpayTotal.size() - 1; i++) {
            for (int j = i + 1; j < dutchpayTotal.size(); j++) {
                if (dutchpayTotal.get(i) + dutchpayTotal.get(j) == 0) {
                    dutchpayTotal.set(i, 0L);
                    dutchpayTotal.set(j-1, 0L);
                }
            }
        }

        return dutchpayTotal;
    }

    private static List<Long> searchDividedMoney(List<Long> dutchpayTotal) {
        //내림차순 정렬
        dutchpayTotal.sort(Long::compareTo);

        //양수만 추출
        List<Long> positiveList = dutchpayTotal.subList(0, dutchpayTotal.size() - 1);


        return dutchpayTotal;
    }

}
