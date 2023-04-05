package com.example.dutchpay.repository;

import com.example.dutchpay.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RepositoryRestResource
public interface UserAccountRepository extends JpaRepository<UserAccount, Long>{
}
