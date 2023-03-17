package com.example.dutchpay;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.*;

public class MinimumTransferTest {


    @Test
    @DisplayName("4명의 사용자의 간단한 예시")
    void test() {
        // given
        Long[] balances = {0L, -2000L, 4000L, -2000L};
        String[] names = {"A", "B", "C", "D"};

        // when
        long startTime1 = System.currentTimeMillis();
        List<String> list1 = basicMinTransfers(balances.clone(), names);
        long finishTime1 = System.currentTimeMillis();

        printResult(startTime1, list1, finishTime1);

        long startTime2 = System.currentTimeMillis();
        List<String> list2 = sameMoneyAfterBigMoneyMinTransfers(balances.clone(), names);
        long finishTime2 = System.currentTimeMillis();
        printResult(startTime2, list2, finishTime2);

        long startTime4 = System.currentTimeMillis();
        List<String> list4 = sameMoneyAfterBigMoneyMinTransfers2(balances.clone(), names);
        long finishTime4 = System.currentTimeMillis();
        printResult(startTime4, list4, finishTime4);


        long startTime5 = System.currentTimeMillis();
        List<String> list5 = sameMoneyAfterBigMoneyMinTransfers3(balances.clone(), names);
        long finishTime5 = System.currentTimeMillis();
        printResult(startTime5, list5, finishTime5);

        long startTime3 = System.currentTimeMillis();
        int i = dfsMinTransfers(balances);
        long finishTime3 = System.currentTimeMillis();

        System.out.println("실행 시간 : " + (finishTime3 - startTime3) + "ms");
        System.out.println("이체 횟수 : " + i);

        // then
    }

    @Test
    @DisplayName("이체 횟수 반례 찾기")
    void test2() {
        // given
        Long[] balances = {20L, 12L, -5L, -4L, -4L, -3L, -2L, -2L, -8L, -4L};
        String[] names = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

        // when
        long startTime1 = System.currentTimeMillis();
        List<String> list1 = basicMinTransfers(balances.clone(), names);
        long finishTime1 = System.currentTimeMillis();

        printResult(startTime1, list1, finishTime1);

        long startTime2 = System.currentTimeMillis();
        List<String> list2 = sameMoneyAfterBigMoneyMinTransfers(balances.clone(), names);
        long finishTime2 = System.currentTimeMillis();

        printResult(startTime2, list2, finishTime2);

        long startTime4 = System.currentTimeMillis();
        List<String> list4 = sameMoneyAfterBigMoneyMinTransfers2(balances.clone(), names);
        long finishTime4 = System.currentTimeMillis();

        printResult(startTime4, list4, finishTime4);

        long startTime5 = System.currentTimeMillis();
        List<String> list5 = sameMoneyAfterBigMoneyMinTransfers3(balances.clone(), names);
        long finishTime5 = System.currentTimeMillis();

        printResult(startTime5, list5, finishTime5);

        long startTime3 = System.currentTimeMillis();
        int i = dfsMinTransfers(balances);
        long finishTime3 = System.currentTimeMillis();

        System.out.println("실행 시간 : " + (finishTime3 - startTime3) + "ms");
        System.out.println("이체 횟수 : " + i);

        // then
    }


    @Test
    @DisplayName("다수 사용자일때, 속도 비교하기")
    void test3() {
        // given
        Long[] balances = {-8L, -6L, -5L, -4L, -3L, -2L, -1L ,9L ,2L ,3L ,4L ,5L ,6L};
        String[] names = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M"};

        // when
        long startTime1 = System.currentTimeMillis();
        List<String> list1 = basicMinTransfers(balances.clone(), names);
        long finishTime1 = System.currentTimeMillis();

        printResult(startTime1, list1, finishTime1);

        long startTime2 = System.currentTimeMillis();
        List<String> list2 = sameMoneyAfterBigMoneyMinTransfers(balances.clone(), names);
        long finishTime2 = System.currentTimeMillis();

        printResult(startTime2, list2, finishTime2);

        long startTime4 = System.currentTimeMillis();
        List<String> list4 = sameMoneyAfterBigMoneyMinTransfers2(balances.clone(), names);
        long finishTime4 = System.currentTimeMillis();

        printResult(startTime4, list4, finishTime4);

        long startTime5 = System.currentTimeMillis();
        List<String> list5 = sameMoneyAfterBigMoneyMinTransfers3(balances.clone(), names);
        long finishTime5 = System.currentTimeMillis();

        printResult(startTime5, list5, finishTime5);

        long startTime3 = System.currentTimeMillis();
        int i = dfsMinTransfers(balances);
        long finishTime3 = System.currentTimeMillis();

        System.out.println("실행 시간 : " + (finishTime3 - startTime3) + "ms");
        System.out.println("이체 횟수 : " + i);
    }


    private void printResult(long startTime1, List<String> list1, long finishTime1) {
        System.out.println("실행 시간 : " + (finishTime1 - startTime1) + "ms");
        list1.forEach(System.out::println);
        System.out.println("이체 횟수 : " + list1.size());
        System.out.println("---------------");
    }

    /*
     * todo : 음수 잔액이 가장 작은 계좌와 양수 잔액으로 이체
     */

    private static List<String> basicMinTransfers(Long[] balances, String[] names) {
        int n = balances.length;
        List<String> res = new ArrayList<>();

        //음수 잔액이 가장 작은 계좌와 양수 잔액이 가장 큰 계좌를 찾습니다.
        //두 계좌의 잔액 중 작은 값을 이체합니다. 이렇게 하면 두 계좌 중 하나의 잔액이 0이 됩니다.
        int count = 0;

        while (true) {
            int minIndex = 0;
            int maxIndex = 0;

            for (int i = 1; i < n; i++) {
                if (balances[i] < balances[minIndex]) {
                    minIndex = i;
                }

                if (balances[i] > balances[maxIndex]) {
                    maxIndex = i;
                }
            }

            if (balances[minIndex] == 0 && balances[maxIndex] == 0) {
                break;
            }

            Long amount = Math.min(-balances[minIndex], balances[maxIndex]);

            res.add(names[minIndex] + " -> " + names[maxIndex] + " 보낼 금액 : " + amount + "원");
            balances[minIndex] += amount;
            balances[maxIndex] -= amount;
            count++;
        }

        return res;
    }

    /*
     * todo : 절대값이 같은 돈은 먼저 이체한후 남은 돈을 이체
     */

    public static List<String> sameMoneyAfterBigMoneyMinTransfers(Long[] balances, String[] names) {
        int n = balances.length;
        List<String> res = new ArrayList<>();

        //0을 제외한 절대값이 같은 경우, 각각 요소를 0으로 만들기
        for (int i = 0; i < n; i++) {
            if (balances[i] != 0) {
                for (int j = i + 1; j < n; j++) {
                    if ((Math.abs(balances[i]) == Math.abs(balances[j])) && (balances[i] > 0)){
                        res.add(names[j] + " -> " + names[i] + " 보낼 금액 : " + Math.abs(balances[i]) + "원");
                        balances[i] = 0L;
                        balances[j] = 0L;
                    }

                    if ((Math.abs(balances[i]) == Math.abs(balances[j])) && (balances[j] > 0)){
                        res.add(names[i] + " -> " + names[j] + " 보낼 금액 : " + Math.abs(balances[i]) + "원");
                        balances[i] = 0L;
                        balances[j] = 0L;
                    }
                }
            }
        }

        //음수 잔액이 가장 작은 계좌와 양수 잔액이 가장 큰 계좌를 찾습니다.
        //두 계좌의 잔액 중 작은 값을 이체합니다. 이렇게 하면 두 계좌 중 하나의 잔액이 0이 됩니다.
        while (true) {
            int minIndex = 0;
            int maxIndex = 0;

            for (int i = 1; i < n; i++) {
                if (balances[i] < balances[minIndex]) {
                    minIndex = i;
                }

                if (balances[i] > balances[maxIndex]) {
                    maxIndex = i;
                }
            }

            if (balances[minIndex] == 0 && balances[maxIndex] == 0) {
                break;
            }

            Long amount = Math.min(-balances[minIndex], balances[maxIndex]);

            res.add(names[minIndex] + " -> " + names[maxIndex] + " 보낼 금액 : " + amount + "원");
            balances[minIndex] += amount;
            balances[maxIndex] -= amount;
        }

        return res;
    }

    /*
     * todo : 절대값이 같은 돈은 먼저 이체한후 남은 돈을 이체하는데 같은 금액 우선순위 두기
     */

    public static List<String> sameMoneyAfterBigMoneyMinTransfers2(Long[] balances, String[] names) {
        int n = balances.length;
        List<String> res = new ArrayList<>();

        //0을 제외한 절대값이 같은 경우, 각각 요소를 0으로 만들기
        for (int i = 0; i < n; i++) {
            if (balances[i] != 0) {
                for (int j = i + 1; j < n; j++) {
                    if ((Math.abs(balances[i]) == Math.abs(balances[j])) && (balances[i] > 0)){
                        res.add(names[j] + " -> " + names[i] + " 보낼 금액 : " + Math.abs(balances[i]) + "원");
                        balances[i] = 0L;
                        balances[j] = 0L;
                    }

                    if ((Math.abs(balances[i]) == Math.abs(balances[j])) && (balances[j] > 0)){
                        res.add(names[i] + " -> " + names[j] + " 보낼 금액 : " + Math.abs(balances[i]) + "원");
                        balances[i] = 0L;
                        balances[j] = 0L;
                    }
                }
            }
        }

        //음수 잔액이 가장 작은 계좌와 양수 잔액이 가장 큰 계좌를 찾습니다.
        //두 계좌의 잔액 중 작은 값을 이체합니다. 이렇게 하면 두 계좌 중 하나의 잔액이 0이 됩니다.
        while (true) {
            int minIndex = 0;
            int maxIndex = 0;

            for (int i = 1; i < n; i++) {
                if (balances[i] > balances[maxIndex]) {
                    maxIndex = i;
                }
            }

            for (int j = 1; j < n; j++) {
                if (balances[j] + balances[maxIndex] == 0) {
                    minIndex = j;
                    break;
                }

                if (balances[j] < balances[minIndex]) {
                    minIndex = j;
                }
            }

            if (balances[minIndex] == 0 && balances[maxIndex] == 0) {
                break;
            }

            Long amount = Math.min(-balances[minIndex], balances[maxIndex]);

            res.add(names[minIndex] + " -> " + names[maxIndex] + " 보낼 금액 : " + amount + "원");
            balances[minIndex] += amount;
            balances[maxIndex] -= amount;
        }

        return res;
    }

    /*
     * todo : 절대값이 같은 돈은 먼저 이체한후 큰 금액부터 먼저 이체 같은 금액 우선순위 두기
     */

    public static List<String> sameMoneyAfterBigMoneyMinTransfers3(Long[] balances, String[] names) {
        int n = balances.length;
        List<String> res = new ArrayList<>();

        //0을 제외한 절대값이 같은 경우, 각각 요소를 0으로 만들기
        for (int i = 0; i < n; i++) {
            if (balances[i] != 0) {
                for (int j = i + 1; j < n; j++) {
                    if ((Math.abs(balances[i]) == Math.abs(balances[j])) && (balances[i] > 0)){
                        res.add(names[j] + " -> " + names[i] + " 보낼 금액 : " + Math.abs(balances[i]) + "원");
                        balances[i] = 0L;
                        balances[j] = 0L;
                    }

                    if ((Math.abs(balances[i]) == Math.abs(balances[j])) && (balances[j] > 0)){
                        res.add(names[i] + " -> " + names[j] + " 보낼 금액 : " + Math.abs(balances[i]) + "원");
                        balances[i] = 0L;
                        balances[j] = 0L;
                    }
                }
            }
        }


        while (true) {
            //가장 큰 금액 찾기
            int maxIndex = 0;
            Boolean temp = false;

            for (int i = 1; i < n; i++) {
                if (balances[i] > balances[maxIndex]) {
                    maxIndex = i;
                }
            }

            //큰 금액이 0이 될때까지 반복
            while (balances[maxIndex] != 0L){
                temp = true;
                int minIndex = 0;

                for (int j = 1; j < n; j++) {
                    if (balances[j] + balances[maxIndex] == 0) {
                        minIndex = j;
                        break;
                    }

                    if (balances[j] < balances[minIndex]) {
                        minIndex = j;
                    }
                }

                if (balances[minIndex] == 0 && balances[maxIndex] == 0) {
                    break;
                }

                Long amount = Math.min(-balances[minIndex], balances[maxIndex]);

                res.add(names[minIndex] + " -> " + names[maxIndex] + " 보낼 금액 : " + amount + "원");
                balances[minIndex] += amount;
                balances[maxIndex] -= amount;
            }

            //종료조건
            if (temp.equals(false)) {
                break;
            }
        }

        return res;
    }

    /*
     * todo : dfs로 풀어보기
     */

    // minTransfers 메소드는 transactions 배열을 입력으로 받습니다. 이 배열은 각 사람의 채무/채권 상태를 나타냄.
    public static int dfsMinTransfers(Long[] transactions) {
        Long[] debt = buildDebtArray(transactions);
        return dfs(transactions, 0);
    }

    private static int dfs(Long[] debt, int start) {
        //start 인덱스부터 시작하여 debt 배열의 첫 번째 0이 아닌 값을 찾습니다.
        while (start < debt.length && debt[start] == 0) start++;

        //만약 모든 값이 0이면, 모든 채무가 상쇄되었으므로 0을 반환합니다.
        if (start == debt.length) return 0;

        int min = Integer.MAX_VALUE;

        for (int i = start + 1; i < debt.length; i++) {

            if (debt[i] * debt[start] < 0) {//음수 양수 관계일때만

                //현재 요소가 해당 요소에게 채무/채권을 상쇄
                debt[i] += debt[start];

                //최소 이체 횟수를 계산하고 현재 단계의 최솟값과 비교하여 더 작은 값을 선택
                min = Math.min(min, dfs(debt, start + 1) + 1);

                //백트래킹을 위해 다시 돌려 놓기
                debt[i] -= debt[start];
            }
        }
        return min;
    }

    //이 배열은 transactions 배열에서 0이 아닌 값을 가진 요소만 포함
    private static Long[] buildDebtArray(Long[] transactions) {

        return Arrays.stream(transactions)
                .filter(x -> x != 0)
                .toArray(Long[]::new);
    }
}
