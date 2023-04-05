package com.example.dutchpay.service;

import com.example.dutchpay.domain.UserAccount;
import com.example.dutchpay.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserAccountService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    public void saveUser(OAuth2User principal) {
        Long id = (Long) principal.getAttribute("id");

        if(userAccountRepository.findById(id).isPresent()) {return;}

        Map<String, Object> properties = (Map<String, Object>) principal.getAttribute("properties");
        String nickname = (String) properties.get("nickname");

        Map<String, Object> kakao_account = principal.getAttribute("kakao_account");
        String email = kakao_account.get("email").toString();

        UserAccount loginUser = new UserAccount(id, nickname, (String) email);
        userAccountRepository.save(loginUser);
    }

}
