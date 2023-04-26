package com.example.dutchpay.repository;

import com.example.dutchpay.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface FriendRepository extends JpaRepository<Friend, Long> {

    List<Friend> findAllByUserAccountId(Long userAccountId);

    @Query("select f from Friend f where f.userAccount.id = :accountId")
    List<Friend> findAllByAccountIdQueryOptimization(Long accountId);

    @Query("SELECT f FROM Friend f JOIN FETCH f.userAccount WHERE f.userAccount.id = :accountId")
    List<Friend> findAllByAccountIdWithFetchJoin(Long accountId);
}
