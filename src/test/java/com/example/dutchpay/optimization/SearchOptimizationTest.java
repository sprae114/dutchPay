package com.example.dutchpay.optimization;

import com.example.dutchpay.domain.DutchResult;
import com.example.dutchpay.repository.DutchResultRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;


@DisplayName("검색 최적화 테스트")
@DataJpaTest
class SearchOptimizationTest {

    private DutchResultRepository dutchResultRepository;

    private Long userId = 648L;

    @Autowired
    public SearchOptimizationTest(DutchResultRepository dutchResultRepository) {
        this.dutchResultRepository = dutchResultRepository;
    }

    @DisplayName("전체 검색")
    @Test
    void test1() {
        // Given
        PageRequest pageable = PageRequest.of(0, 10);

        // When
        long startTime = System.currentTimeMillis();
        dutchResultRepository.findAllByUserAccountId(userId, pageable);
        long endTime = System.currentTimeMillis();

        // Then
        long elapsedTime = endTime - startTime;
        System.out.println("최적화 시간은 : " + elapsedTime);
    }


    @DisplayName("날짜 검색")
    @Test
    void test2() {
        // Given
        String searchKeyword = "2022";
        PageRequest pageable = PageRequest.of(0, 10);

        // When
        long startTime = System.currentTimeMillis();
        dutchResultRepository.findAllByUserAccountIdAndCreatedAtStringContaining(userId, searchKeyword, pageable);
        long endTime = System.currentTimeMillis();

        // Then
        long elapsedTime = endTime - startTime;
        System.out.println("최적화 시간은 : " + elapsedTime);
    }


    @DisplayName("이름 검색")
    @Test
    void test3() {
        // Given
        String searchKeyword = "Fancy";
        PageRequest pageable = PageRequest.of(0, 10);

        // When
        long startTime = System.currentTimeMillis();
        dutchResultRepository.findAllByUserAccountIdAndNamesContaining(userId, searchKeyword, pageable);
        long endTime = System.currentTimeMillis();

        // Then
        long elapsedTime = endTime - startTime;
        System.out.println("최적화 시간은 : " + elapsedTime);
    }
}