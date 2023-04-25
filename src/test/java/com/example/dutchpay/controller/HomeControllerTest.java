package com.example.dutchpay.controller;

import com.example.dutchpay.config.TestSecurityConfig;
import com.example.dutchpay.domain.type.SearchType;
import com.example.dutchpay.service.DutchResultService;
import com.example.dutchpay.service.PaginationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@ExtendWith(SpringExtension.class)
class HomeControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private DutchResultService dutchResultService;

    @MockBean private PaginationService paginationService;


    @DisplayName("home 페이지 테스트")
    @Test
    public void homeTest() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @DisplayName("main 페이지 테스트 - 로그인전")
    @Test
    public void mainPageBeforeLogin() throws Exception {
        mockMvc.perform(get("/main"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/oauth2/authorization/kakao"));
    }

    @DisplayName("main 페이지 테스트 - 로그인후")
    @Test
    @WithMockUser
    public void mainPageBeforeAfter() throws Exception {
        mockMvc.perform(get("/main"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/dutch"));
    }

    @DisplayName("이전 계산 결과 페이지 테스트 - 로그인전")
    @Test
    void previousCalculations2() throws Exception {
        mockMvc.perform(get("/previousCalculations"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/oauth2/authorization/kakao"));
    }

    @DisplayName("이전 계산 결과 페이지 테스트 - 로그인후 전체 검색")
    @Test
    @WithUserDetails(value = "1111", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void previousCalculations() throws Exception {
        when(dutchResultService.searchDutchResult(any(Long.class), eq(null), eq(null), any(Pageable.class))).thenReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0, 1, 2, 3, 4));

        mockMvc.perform(get("/previousCalculations"))
                .andExpect(status().isOk())
                .andExpect(view().name("previousCalculations"))
                .andExpect(model().attributeExists("dutchResultList"))
                .andExpect(model().attributeExists("paginationBarNumbers"));
    }

    @DisplayName("이전 계산 결과 페이지 테스트 - 이름 검색어 호출")
    @Test
    @WithUserDetails(value = "1111", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void previousCalculationsSearchNameTest() throws Exception {
        SearchType searchType = SearchType.NAMES;
        String searchValue = "김";
        when(dutchResultService.searchDutchResult(any(Long.class), eq(searchType), eq(searchValue), any(Pageable.class))).thenReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0, 1, 2, 3, 4));

        mockMvc.perform(get("/previousCalculations")
                .queryParam("searchType", searchType.name())
                .queryParam("searchValue", searchValue))
                .andExpect(status().isOk())
                .andExpect(view().name("previousCalculations"))
                .andExpect(model().attributeExists("dutchResultList"))
                .andExpect(model().attributeExists("paginationBarNumbers"));
    }

    @DisplayName("이전 계산 결과 페이지 테스트 - 날짜 검색어 호출")
    @Test
    @WithUserDetails(value = "1111", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void previousCalculationsSearchDateTest() throws Exception {
        SearchType searchType = SearchType.CREATED_AT_STRING;
        String searchValue = "04";
        when(dutchResultService.searchDutchResult(any(Long.class), eq(searchType), eq(searchValue), any(Pageable.class))).thenReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0, 1, 2, 3, 4));

        mockMvc.perform(get("/previousCalculations")
                        .queryParam("searchType", searchType.name())
                        .queryParam("searchValue", searchValue))
                .andExpect(status().isOk())
                .andExpect(view().name("previousCalculations"))
                .andExpect(model().attributeExists("dutchResultList"))
                .andExpect(model().attributeExists("paginationBarNumbers"));
    }
}
