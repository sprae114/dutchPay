package com.example.dutchpay.service;

import com.example.dutchpay.domain.DutchResult;
import com.example.dutchpay.domain.UserAccount;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DutchResultService {
    public DutchResultRepository dutchResultRepository;
    public UserAccountRepository userAccountRepository;

    public DutchResultService(DutchResultRepository dutchResultRepository) {
        this.dutchResultRepository = dutchResultRepository;
    }

    public void addDutchResult(String dutchResult){
        UserAccount userAccount = userAccountRepository.findById(1L).orElse(null);
        dutchResultRepository.save(new DutchResult(dutchResult, userAccount));
    }

    public List<DutchResult> getDutchResult(){
        return dutchResultRepository.findAllByUserAccountId(1L);
    }
}
