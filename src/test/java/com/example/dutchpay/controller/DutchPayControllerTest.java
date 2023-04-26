package com.example.dutchpay.controller;


import com.example.dutchpay.config.TestSecurityConfig;
import com.example.dutchpay.dto.FreindsDutchpayDto;
import com.example.dutchpay.dto.FreindsDutchpayNoAlcholDto;
import com.example.dutchpay.repository.DutchResultRepository;
import com.example.dutchpay.service.DutchPayService;
import com.example.dutchpay.service.InMemoryDutchPayService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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

import static org.assertj.core.api.Assertions.assertThat;
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
class DutchPayControllerTest {
    private MockMvc mockMvc;

    @InjectMocks private DutchPayController dutchPayController;
    @MockBean private DutchPayService dutchPayService;

    @MockBean private DutchResultRepository dutchResultRepository;


    public DutchPayControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }


    @DisplayName("더치페이 홈페이지 테스트 - 로그인전")
    @Test
    public void dutchHomeBeforeLogin() throws Exception {
        mockMvc.perform(get("/dutch"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/oauth2/authorization/kakao"));
    }


    @DisplayName("더치페이 홈페이지 테스트 - 성공")
    @Test
    @WithUserDetails(value = "1111", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void dutchHomeBeforeTest() throws Exception {
        mockMvc.perform(get("/dutch"))
                .andExpect(status().isOk())
                .andExpect(view().name("dutchHome"));
    }


    @DisplayName("더치페이 리스트 조회 테스트 - 성공")
    @Test
    @WithMockUser
    public void dutchResultTest() throws Exception {
        // given
        DutchPayController.dutchpayMain = new ArrayList<>();

        // when
        mockMvc.perform(get("/dutch/dutchPayList"))
                .andExpect(status().isOk())
                .andExpect(view().name("cal/totalcalculrate"))
                .andExpect(model().attribute("dutchList", DutchPayController.dutchpayMain));
    }


    @DisplayName("더치페이 리스트 삭제 테스트 - 성공")
    @Test
    @WithMockUser
    public void dutchResultDeleteTest() throws Exception {
        // given
        int idToRemove = 0;
        List<Long> longs = List.of(10000L, 20000L, 30000L);
        ArrayList<List<Long>> dutchpayDb = new ArrayList<>();
        dutchpayDb.add(longs);

        List<Long> dutchpayMain = new ArrayList<>(Arrays.asList(1L, 2L, 3L));

        InMemoryDutchPayService.dutchpayDb = dutchpayDb;
        DutchPayController.dutchpayMain = dutchpayMain;

        // when
        mockMvc.perform(post("/dutch/dutchPayList/{id}", idToRemove)
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dutch/dutchPayList"));

        // then
        assertThat(InMemoryDutchPayService.dutchpayDb).doesNotContain(longs);
        assertThat(DutchPayController.dutchpayMain).doesNotContain(1L);
    }

    @DisplayName("더치페이 리스트 초기화 - 성공")
    @Test
    @WithMockUser
    public void dutchClearTest_success() throws Exception {
        // given
        willDoNothing().given(dutchPayService).dutchinit();

        // when
        mockMvc.perform(get("/dutch/dutchPayListClear"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dutch/dutchPayList"));

        // then
        verify(dutchPayService, times(1)).dutchinit();
    }


    @DisplayName("술값 포함 X 더치페이 계산 조회 - 성공")
    @Test
    @WithMockUser
    public void dutchResultNoAlcoholTest() throws Exception {
        // when
        mockMvc.perform(get("/dutch/dutchPayList/noAlcohol"))
                .andExpect(status().isOk())
                .andExpect(view().name("cal/calculraterNoAlcoholDetail"))
                .andExpect(model().attributeExists("form"));
    }


    @DisplayName("술값 포함 X 더치페이 계산 - 성공")
    @Test
    @WithMockUser
    public void dutchResultNoAlcoholSubmit() throws Exception{
        //given
        FreindsDutchpayNoAlcholDto form = new FreindsDutchpayNoAlcholDto();

        //when
        mockMvc.perform(post("/dutch/dutchPayList/noAlcohol")
                        .with(csrf())
                        .flashAttr("form", form))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dutch/dutchPayList"));
    }


    @DisplayName("술값 포함 O 더치페이 계산 조회 - 성공")
    @Test
    @WithMockUser
    public void dutchResultAlcoholTest() throws Exception {
        // when
        mockMvc.perform(get("/dutch/dutchPayList/alcohol"))
                .andExpect(status().isOk())
                .andExpect(view().name("cal/calculaterDetail"))
                .andExpect(model().attributeExists("form"));
    }


    @DisplayName("술값 포함 O 더치페이 계산 - 성공")
    @Test
    @WithMockUser
    public void dutchResultAlcoholSubmit() throws Exception{
        //given
        FreindsDutchpayDto form = new FreindsDutchpayDto();

        //when
        mockMvc.perform(post("/dutch/dutchPayList/alcohol")
                        .with(csrf())
                        .flashAttr("form", form))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dutch/dutchPayList"));
    }


    @DisplayName("더치페이 결과 조회 - 성공")
    @Test
    @WithMockUser
    public void dutchResultDetailTest() throws Exception {
        //given
        String resultMessage = "Result String";
        given(dutchPayService.calculateDutchPayMoney()).willReturn("10000");
        given(dutchPayService.printDutchPay(any(String.class), anyList())).willReturn(resultMessage);

        // When
        mockMvc.perform(get("/dutch/dutchResult"))
                .andExpect(status().isOk())
                .andExpect(view().name("cal/calculraterResult"))
                .andExpect(model().attributeExists("result"))
                .andExpect(model().attribute("result", resultMessage));

        // Then
        verify(dutchPayService, times(1)).calculateDutchPayMoney();
        verify(dutchPayService, times(1)).printDutchPay(any(String.class), anyList());
    }


    @DisplayName("더치페이 결과 저장 - 성공")
    @Test
    @WithUserDetails(value = "1111", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testRecordDutchResult() throws Exception {
        String recordDutch = "Sample recordDutch";

        mockMvc.perform(post("/dutch/recordDutchResult")
                        .param("recordDutch", recordDutch)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dutch"));
    }
}