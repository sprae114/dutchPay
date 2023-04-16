package com.example.dutchpay.controller;

import com.example.dutchpay.config.TestSecurityConfig;
import com.example.dutchpay.cotroller.HomeController;
import com.example.dutchpay.domain.DutchResult;
import com.example.dutchpay.service.DutchResultService;
import com.example.dutchpay.service.UserAccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@ExtendWith(SpringExtension.class)
class HomeControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private DutchResultService dutchResultService;


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

    @DisplayName("이전 계산 결과 페이지 테스트 - 로그인후")
    @Test
    @WithMockUser
    void previousCalculations() throws Exception {
        List<DutchResult> dutchResultList = Arrays.asList(new DutchResult(), new DutchResult());
        when(dutchResultService.getDutchResult(any())).thenReturn(dutchResultList);

        mockMvc.perform(get("/previousCalculations"))
                .andExpect(status().isOk())
                .andExpect(view().name("previousCalculations"))
                .andExpect(model().attributeExists("dutchResultList"))
                .andExpect(model().attribute("dutchResultList", dutchResultList));
    }
}
