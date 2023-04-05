package com.example.dutchpay.controller;

import com.example.dutchpay.domain.DutchResult;
import com.example.dutchpay.service.DutchResultService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DutchResultService dutchResultService;

    @Test
    void previousCalculations() throws Exception {
        List<DutchResult> dutchResultList = Arrays.asList(new DutchResult(), new DutchResult());
        when(dutchResultService.getDutchResult(any())).thenReturn(dutchResultList);

        mockMvc.perform(get("/previousCalculations").with(user("test_user"))
                .param("userId", "test_user"))
                .andExpect(status().isOk())
                .andExpect(view().name("previousCalculations"))
                .andExpect(model().attributeExists("dutchResultList"))
                .andExpect(model().attribute("dutchResultList", dutchResultList));

        System.out.println("dutchResultList: " + dutchResultList);
    }

}
