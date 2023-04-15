package com.example.dutchpay.config;

import com.example.dutchpay.dto.KakaoOAuth2Response;
import com.example.dutchpay.dto.LoginPrincipal;
import com.example.dutchpay.service.UserAccountService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .mvcMatchers(
                                HttpMethod.GET,
                                "/"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(auth -> auth
                        .defaultSuccessUrl("/main", true)
                )
                .logout(auth -> auth
                        .logoutSuccessUrl("/")
                )
                .build();
    }


    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(
            UserAccountService userAccountService
    ) {
        final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();


        return userRequest -> {
            OAuth2User oAuth2User = delegate.loadUser(userRequest);

            KakaoOAuth2Response kakaoResponse = KakaoOAuth2Response.from(oAuth2User.getAttributes());
            Long providerId = kakaoResponse.getId();
            String nickname = String.valueOf(kakaoResponse.getProperties().get("nickname"));


            return userAccountService.searchUser(providerId)
                    .map(LoginPrincipal::from)
                    .orElseGet(() ->
                            LoginPrincipal.from(
                                    userAccountService.saveUser(
                                            providerId,
                                            nickname,
                                            kakaoResponse.getEmail()
                                    )
                            )
                    );
        };
    }
}


