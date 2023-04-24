package com.example.dutchpay.repository;

import com.example.dutchpay.domain.DutchResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface DutchResultRepository extends JpaRepository<DutchResult, Long> {
    Page<DutchResult> findAllByUserAccountId(Long userAccountId, Pageable pageable);

    Page<DutchResult> findByNamesContaining(String names, Pageable pageable);

    Page<DutchResult> findByCreatedAtContaining(String createdAt, Pageable pageable);
}