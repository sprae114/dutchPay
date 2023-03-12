package com.example.dutchpay.service;

import com.example.dutchpay.domain.DutchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface DutchResultRepository extends JpaRepository<DutchResult, Long> {
}
