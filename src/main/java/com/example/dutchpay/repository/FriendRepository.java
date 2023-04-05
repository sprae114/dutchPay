package com.example.dutchpay.repository;

import com.example.dutchpay.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findAllByUserAccountId(Long userAccountId);
}
