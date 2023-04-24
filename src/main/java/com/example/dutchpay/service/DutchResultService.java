package com.example.dutchpay.service;

import com.example.dutchpay.domain.DutchResult;
import com.example.dutchpay.domain.type.SearchType;
import com.example.dutchpay.dto.LoginPrincipal;
import com.example.dutchpay.repository.DutchResultRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional(readOnly = true)
    public Page<DutchResult> searchDutchResult(SearchType searchType, String searchKeyword, Pageable pageable){
        if (searchKeyword == null || searchKeyword.isBlank()) {
            return dutchResultRepository.findAll(pageable);
        }

        return switch (searchType){
            case NAMES -> dutchResultRepository.findByNamesContaining(searchKeyword, pageable);
            case CREATED_AT -> dutchResultRepository.findByCreatedAtContaining(searchKeyword, pageable);
        };
    }

    @Transactional(readOnly = true)
    public Page<DutchResult> getDutchResult(LoginPrincipal loginPrincipal, Pageable pageable){
        return dutchResultRepository.findAllByUserAccountId(loginPrincipal.getId(), pageable);
    }

    public void saveDutchResult(DutchResult dutchResult){
        dutchResultRepository.save(dutchResult);
    }
}
