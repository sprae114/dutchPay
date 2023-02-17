package com.example.dutchpay.service;

import com.example.dutchpay.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long>{
}
