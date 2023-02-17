package com.example.dutchpay.service;

import com.example.dutchpay.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface FriendRepository extends JpaRepository<Friend, Long> {
}
