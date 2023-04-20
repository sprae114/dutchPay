package com.example.dutchpay.service;

import com.example.dutchpay.domain.UserAccount;
import com.example.dutchpay.dto.LoginPrincipal;
import com.example.dutchpay.dto.UserAccountDto;
import com.example.dutchpay.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Transactional
@Service
public class UserAccountService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    public Optional<UserAccountDto> searchUser(Long providerId) {
        return userAccountRepository.findById(providerId)
                .map(UserAccountDto::from);
    }

    public Optional<UserAccountDto> searchUser(LoginPrincipal loginPrincipal){
        return userAccountRepository.findById(loginPrincipal.getId())
                .map(UserAccountDto::from);
    }

    public UserAccountDto saveUser(Long id, String name, String email) {
        return UserAccountDto.from(userAccountRepository.save(UserAccount.of(id, name, email)));
    }

    public UserAccountDto saveUser(LoginPrincipal loginPrincipal) {
        return UserAccountDto.from(userAccountRepository.save(
                UserAccount.of(
                        loginPrincipal.getId(),
                        loginPrincipal.getName(),
                        loginPrincipal.getEmail()
                )
        ));
    }
}
