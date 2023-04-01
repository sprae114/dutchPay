package com.example.dutchpay.service;

import com.example.dutchpay.domain.DutchResult;
import com.example.dutchpay.domain.UserAccount;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DutchResultService {
    public DutchResultRepository dutchResultRepository;

    public DutchResultService(DutchResultRepository dutchResultRepository) {
        this.dutchResultRepository = dutchResultRepository;
    }

    public List<DutchResult> getDutchResult(OAuth2User principal){
        return dutchResultRepository.findAllByUserAccountId((Long) principal.getAttribute("id"));
    }
}
