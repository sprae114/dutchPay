package com.example.dutchpay.config;

import com.example.dutchpay.dto.LoginPrincipal;
import com.example.dutchpay.dto.UserAccountDto;
import com.example.dutchpay.service.UserAccountService;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@Import(SecurityConfig.class)
public class TestSecurityConfig {

    @MockBean private UserAccountService userAccountService;

    @BeforeTestMethod
    public void securitySetUp() {
        given(userAccountService.searchUser(any(Long.class)))
                .willReturn(Optional.of(createUserAccountDto()));
        given(userAccountService.saveUser(any(Long.class), anyString(), anyString()))
                .willReturn(createUserAccountDto());
        given(userAccountService.saveUser(any(LoginPrincipal.class))).willReturn(createUserAccountDto());
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetailsService userDetailsService = Mockito.mock(UserDetailsService.class);
        UserDetails user = User.builder()
                .username("1111")
                .password("password")
                .authorities("ROLE_USER")
                .build();
        Mockito.when(userDetailsService.loadUserByUsername("1111")).thenReturn(user);
        return userDetailsService;
    }


    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                1111L,
                "김철수",
                "abc@naver.com"
        );
    }
}

