package com.example.dutchpay.service;

import com.example.dutchpay.domain.Friend;
import com.example.dutchpay.domain.UserAccount;
import com.example.dutchpay.dto.FriendSelectSaveDto;
import com.example.dutchpay.exception.UserNotFoundException;
import com.example.dutchpay.repository.FriendRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static com.example.dutchpay.service.FriendService.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class FriendServiceTest {
    @InjectMocks
    FriendService friendService;

    @Mock
    FriendRepository friendRepository;

    private UserAccount userAccount;

    @BeforeEach
    void beforeEach() {
        userAccount = new UserAccount(1L, "김철수", "1234@naver.com");
    }

    @DisplayName("친구 추가 - 성공")
    @Test
    void addFriendTest() {
        // Given
        Friend friend = new Friend("test1", "010-1234-1234", userAccount);

        // When
        friendService.addFriend(friend);

        // Then
        verify(friendRepository, times(1)).save(friend);
    }

    @DisplayName("친구 추가 null 객체를 전달할때 - 실패")
    @Test
    void addFriendTest_NullFriend() {
        // Given
        Friend friend = null;

        // When
        assertThrows(UserNotFoundException.class, () -> friendService.addFriend(friend));

        // Then
        verify(friendRepository, times(0)).save(any(Friend.class));
    }

    @DisplayName("친구 삭제 - 성공")
    @Test
    void deleteFriendTest() {
        // Given
        Long friendId = 1L;

        // When
        friendService.deleteFriend(friendId);

        // Then
        verify(friendRepository, times(1)).deleteById(friendId);
    }

    @DisplayName("친구 선택 불러오기 - 성공")
    @Test
    void addFriendSelectListBeforeTest() {
        // Given
        List<Friend> friendList = new ArrayList<>();
        friendList.add(new Friend("test1", "010-1234-1234", userAccount));
        friendList.add(new Friend("test2", "010-1234-1234", userAccount));

        // When
        friendService.addFriendSelectListBefore(friendList);

        // Then
        assertThat(friendSelectListBefore).hasSize(2);
    }

    @DisplayName("존재하지 않는 친구 아이디로 deleteFriend 호출 - 실패")
    @Test
    void deleteFriendTest_NonexistentId() {
        // Given
        Long friendId = 999L;
        doThrow(new RuntimeException("친구 존재하지 않습니다.")).when(friendRepository).deleteById(friendId);

        // When
        assertThrows(RuntimeException.class, () -> friendService.deleteFriend(friendId));

        // Then
        verify(friendRepository, times(1)).deleteById(friendId);
    }

    @DisplayName("친구 선택 후 리스트 - 성공")
    @Test
    void addFriendSelectListAfterTest() {
        // Given
        friendSelectListBefore.add(new FriendSelectSaveDto(1L, "test1", "010-1234-1234"));
        friendSelectListBefore.add(new FriendSelectSaveDto(2L, "test2", "010-1234-1234"));
        List<Integer> selectedFriends = new ArrayList<>();
        selectedFriends.add(0);

        // When
        friendService.addFriendSelectListAfter(selectedFriends);

        // Then
        assertThat(friendSelectListAfter).hasSize(1);
        assertThat(friendSelectListAfter.get(0).getName()).isEqualTo("test1");
        assertThat(friendSelectListAfter.get(0).getPhoneNum()).isEqualTo("010-1234-1234");
    }

    @DisplayName("범위를 벗어난 인덱스로 addFriendSelectListAfter를 호출할 때 - 실패")
    @Test
    void addFriendSelectListAfterTest_IndexOutOfBounds() {
        // Given
        friendSelectListBefore.add(new FriendSelectSaveDto(1L, "test1", "010-1234-1234"));
        friendSelectListBefore.add(new FriendSelectSaveDto(2L, "test2", "010-1234-1234"));
        List<Integer> selectedFriends = new ArrayList<>();
        selectedFriends.add(3); // Index out of bounds

        // When & Then
        assertThrows(IndexOutOfBoundsException.class, () -> friendService.addFriendSelectListAfter(selectedFriends));
    }
}
