package com.example.dutchpay.jpa;

import com.example.dutchpay.domain.DutchResult;
import com.example.dutchpay.domain.Friend;
import com.example.dutchpay.domain.UserAccount;
import com.example.dutchpay.repository.DutchResultRepository;
import com.example.dutchpay.repository.FriendRepository;
import com.example.dutchpay.repository.UserAccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("JPA 연결 테스트")
@DataJpaTest
public class JpaRepositoryTest {

    private final FriendRepository friendRepository;
    private final UserAccountRepository userAccountRepository;
    private final DutchResultRepository dutchResultRepository;

    public JpaRepositoryTest(@Autowired FriendRepository friendRepository,
                             @Autowired UserAccountRepository userAccountRepository,
                             @Autowired DutchResultRepository dutchResultRepository) {
        this.friendRepository = friendRepository;
        this.userAccountRepository = userAccountRepository;
        this.dutchResultRepository = dutchResultRepository;
    }

    @DisplayName("friend select 테스트")
    @Test
    void friendRepository_find() {
        // Given

        // When
        List<Friend> all = friendRepository.findAll();

        // Then
        assertThat(all)
                .isNotNull()
                .hasSize(1000);
    }

    @DisplayName("userAccount select 테스트")
    @Test
    void userAccountRepository_find() {
        // Given

        // When
        List<UserAccount> all = userAccountRepository.findAll();

        // Then
        assertThat(all)
                .isNotNull()
                .hasSize(101);
    }

    @DisplayName("dutchResult select 테스트")
    @Test
    void dutchResultRepository_find() {
        // Given

        // When
        List<DutchResult> all = dutchResultRepository.findAll();

        // Then
        assertThat(all)
                .isNotNull()
                .hasSize(506);
    }


    @DisplayName("friend insert 테스트")
    @Test
    void friendRepository_insert() {
        // Given
        long previousCount = friendRepository.count();
        UserAccount userAccount = userAccountRepository.findById(1L).get();

        // When
        friendRepository.save(Friend.of("Willamina Reville", "449-988-7379",  userAccount));

        // Then
        Assertions.assertThat(friendRepository.count()).isEqualTo(previousCount+1);
    }

    @DisplayName("userAccount insert 테스트")
    @Test
    void userAccountRepository_insert() {
        // Given
        long previousCount = userAccountRepository.count();

        // When
        userAccountRepository.save(UserAccount.of(101L, "john", "mbownass3@xing.com"));

        // Then
        Assertions.assertThat(userAccountRepository.count()).isEqualTo(previousCount+1);
    }

    @DisplayName("friend delete 테스트")
    @Test
    void friendRepository_delete() {
        // Given
        Friend friend = friendRepository.findById(1L).orElseThrow();
        long count = friendRepository.count();

        // When
        friendRepository.delete(friend);

        // Then
        assertThat(friendRepository.count()).isEqualTo(count - 1);
    }
}
