package com.example.dutchpay.repository;

import com.example.dutchpay.domain.DutchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface DutchResultRepository extends JpaRepository<DutchResult, Long> {
    List<DutchResult> findAllByUserAccountId(Long userAccountId);
}
