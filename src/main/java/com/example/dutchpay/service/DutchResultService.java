package com.example.dutchpay.service;

import com.example.dutchpay.domain.DutchResult;
import com.example.dutchpay.dto.LoginPrincipal;
import com.example.dutchpay.repository.DutchResultRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class DutchResultService {
    public DutchResultRepository dutchResultRepository;

    public DutchResultService(DutchResultRepository dutchResultRepository) {
        this.dutchResultRepository = dutchResultRepository;
    }

    public List<DutchResult> getDutchResult(LoginPrincipal loginPrincipal){
        return dutchResultRepository.findAllByUserAccountId(loginPrincipal.getId());
    }
}
