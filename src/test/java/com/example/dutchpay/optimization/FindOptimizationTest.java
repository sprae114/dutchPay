package com.example.dutchpay.optimization;

import com.example.dutchpay.repository.FriendRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("find 최적화 테스트")
@DataJpaTest
public class FindOptimizationTest {

    private final FriendRepository friendRepository;
    private Long selectAccountId = 235L;

    public FindOptimizationTest(@Autowired FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }


    @DisplayName("friend findAll 테스트 - Basic")
    @Test
    void findOptimizationTest() {
        // Given
        long startTime = System.currentTimeMillis();

        // When
        friendRepository.findAllByUserAccountId(selectAccountId);

        long endTime = System.currentTimeMillis();

        // Then
        long elapsedTime = endTime - startTime;
        System.out.println("최적화 시간은 : " + elapsedTime);
    }


    @DisplayName("friend findAll 테스트 - Query")
    @Test
    void findOptimizationTest2() {
        // Given
        long startTime = System.currentTimeMillis();

        // When
        friendRepository.findAllByAccountIdQueryOptimization(selectAccountId);
        long endTime = System.currentTimeMillis();

        // Then
        long elapsedTime = endTime - startTime;
        System.out.println("최적화 시간은 : " + elapsedTime);
    }

    @DisplayName("friend findAll 테스트 - FetchJoin")
    @Test
    void findOptimizationTest3() {
        // Given
        long startTime = System.currentTimeMillis();

        // When
        friendRepository.findAllByAccountIdWithFetchJoin(selectAccountId);
        long endTime = System.currentTimeMillis();

        // Then
        long elapsedTime = endTime - startTime;
        System.out.println("최적화 시간은 : " + elapsedTime);

    }
}
