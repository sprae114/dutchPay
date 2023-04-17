package com.example.dutchpay.controller;


import com.example.dutchpay.config.TestSecurityConfig;
import com.example.dutchpay.domain.Friend;
import com.example.dutchpay.domain.UserAccount;
import com.example.dutchpay.dto.FriendSaveDto;
import com.example.dutchpay.dto.LoginPrincipal;
import com.example.dutchpay.service.DutchPayService;
import com.example.dutchpay.service.FriendService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@ExtendWith(SpringExtension.class)
class FriendControllerTest {
    private MockMvc mockMvc;
    @MockBean private FriendService friendService;

    @MockBean private DutchPayService dutchPayService;

    private UserAccount userAccount;

    public FriendControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @BeforeEach
    public void setup() {
        userAccount = new UserAccount(1111L, "김철수", "abc@naver.com");
    }

    @DisplayName("친구 목록 페이지 테스트 - 로그인전")
    @Test
    public void friendListBeforeLogin() throws Exception {
        mockMvc.perform(get("/friend"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/oauth2/authorization/kakao"));
    }


    @DisplayName("친구 목록 페이지 테스트")
    @Test
    @WithMockUser
    public void friendListTest() throws Exception {
        ArrayList<Friend> friends = new ArrayList<>();
        when(friendService.searchFriend(any(LoginPrincipal.class))).thenReturn(friends);

        mockMvc.perform(get("/friend"))
                .andExpect(status().isOk())
                .andExpect(view().name("friend"));
    }


    @DisplayName("친구 추가 테스트 - 로그인전")
    @Test
    public void DutchAddFriendBeforeLogin() throws Exception {
        mockMvc.perform(post("/friend")
                        .param("name", "Jane Doe")
                        .param("phoneNum", "010-1234-1234")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/oauth2/authorization/kakao"));
    }


    @DisplayName("친구 추가 테스트 - 성공")
    @Test
    @WithUserDetails(value = "1111", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void DutchAddFriend() throws Exception {
        willDoNothing().given(friendService).addFriend(any(Friend.class));

        mockMvc.perform(post("/friend")
                        .param("name", "Jane Doe")
                        .param("phoneNum", "010-1234-1234")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/friend"));

        verify(friendService, times(1)).addFriend(any());
    }


    @DisplayName("친구 추가 테스트 - 유효하지 않은 입력 값")
    @Test
    @WithUserDetails(value = "1111", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void DutchAddFriendWithInvalidInput() throws Exception {
        //given
        FriendSaveDto friendSaveDto = new FriendSaveDto("", "");

        // when
        mockMvc.perform(post("/friend")
                        .param("name", friendSaveDto.getName())
                        .param("phoneNum", friendSaveDto.getPhoneNum())
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("newFriend", "name", "phoneNum"))
                .andExpect(view().name("/friend"));

        // then
        verify(friendService, times(0)).addFriend(any());
    }


    @DisplayName("친구 삭제 테스트 - 성공")
    @Test
    @WithUserDetails(value = "1111", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void DutchDeleteFriend() throws Exception {
        willDoNothing().given(friendService).deleteFriend(anyLong());

        mockMvc.perform(get("/friend/{id}", 1L)
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/friend"));

        verify(friendService, times(1)).deleteFriend(anyLong());
    }


    @DisplayName("친구 삭제 테스트 - 인덱스 벗어나는 값")
    @Test
    @WithUserDetails(value = "1111", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void DutchDeleteNoFriend() throws Exception {
        // given
        Long nonExistentFriendId = 999L;
        doThrow(new IllegalArgumentException("해당 친구가 없습니다. id=" + nonExistentFriendId))
                .when(friendService).deleteFriend(nonExistentFriendId);

        mockMvc.perform(get("/friend/{id}", nonExistentFriendId))
                .andExpect(status().isBadRequest());

        verify(friendService, times(1)).deleteFriend(anyLong());
    }


    @DisplayName("친구 선택 전 테스트 - 로그인전")
    @Test
    public void dutchFriendSelectBeforeLogin() throws Exception {
        mockMvc.perform(get("/dutch/friend"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/oauth2/authorization/kakao"));
    }


    @DisplayName("친구 선택 전 테스트 - 성공")
    @Test
    @WithMockUser
    public void dutchFriendSelectTest() throws Exception {
        // given
        given(friendService.searchFriend(any(LoginPrincipal.class))).willReturn(new ArrayList<>());
        willDoNothing().given(friendService).addFriendSelectListBefore(any(List.class));

        // when
        mockMvc.perform(get("/dutch/friend"))
                .andExpect(status().isOk())
                .andExpect(view().name("friendSelect"));

        // then
        verify(friendService, times(1)).addFriendSelectListBefore(any(List.class));
    }

    @DisplayName("친구 선택 후 저장 테스트 - 성공")
    @Test
    @WithMockUser
    public void dutchFriendSelectSaveTest() throws Exception {
        // given
        List<Integer> selectedFriend = Arrays.asList(1, 2, 3);

        willDoNothing().given(friendService).addFriendSelectListAfter(selectedFriend);
        willDoNothing().given(dutchPayService).dutchinit();

        // when
        mockMvc.perform(post("/dutch/friend")
                        .param("selectedfreind", "1", "2", "3")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dutch/dutchPayList"));

        // then
        verify(friendService, times(1)).addFriendSelectListAfter(selectedFriend);
        verify(dutchPayService, times(1)).dutchinit();
    }

    @DisplayName("친구 선택 후 저장 테스트 - 친구를 선택하지 않을 때")
    @Test
    @WithMockUser
    public void dutchFriendSelectSaveValidationTest() throws Exception {
        // given
        List<Integer> selectedFriend = Arrays.asList(1, 2, 3);

        willDoNothing().given(friendService).addFriendSelectListAfter(selectedFriend);
        willDoNothing().given(dutchPayService).dutchinit();

        // when
        mockMvc.perform(post("/dutch/friend")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error"));

        // then
        verify(friendService, times(0)).addFriendSelectListAfter(any(List.class));
        verify(dutchPayService, times(0)).dutchinit();
    }
}