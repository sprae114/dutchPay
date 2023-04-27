package com.example.dutchpay.service;

import com.example.dutchpay.domain.DutchResult;
import com.example.dutchpay.domain.type.SearchType;
import com.example.dutchpay.dto.DutchResultDTO;
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
    public Page<DutchResultDTO> searchDutchResult(Long id, SearchType searchType, String searchKeyword, Pageable pageable){
        if (searchKeyword == null || searchKeyword.isBlank()) {
            return dutchResultRepository.findAllByUserAccountId(id, pageable);
        }

        switch (searchType){
            case NAMES:
                return dutchResultRepository.findAllByUserAccountIdAndNamesContaining(id, searchKeyword, pageable);
            case CREATED_AT_STRING:
                return dutchResultRepository.findAllByUserAccountIdAndCreatedAtStringContaining(id, searchKeyword, pageable);
            default:
                throw new IllegalArgumentException("타입오류");
        }
    }

    public void saveDutchResult(DutchResult dutchResult){
        dutchResultRepository.save(dutchResult);
    }
}
