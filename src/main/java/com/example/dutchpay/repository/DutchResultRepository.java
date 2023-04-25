package com.example.dutchpay.repository;

import com.example.dutchpay.domain.DutchResult;
import com.example.dutchpay.domain.QDutchResult;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface DutchResultRepository extends
        JpaRepository<DutchResult, Long>,
        QuerydslPredicateExecutor<DutchResult>,
        QuerydslBinderCustomizer<QDutchResult> {
    Page<DutchResult> findAllByUserAccountId(Long userAccountId, Pageable pageable);

    Page<DutchResult> findAllByUserAccountIdAndNamesContaining(Long userAccountId, String names, Pageable pageable);

    Page<DutchResult> findAllByUserAccountIdAndCreatedAtStringContaining(Long userAccountId, String createdAt, Pageable pageable);

    @Override
    default void customize(QuerydslBindings bindings, QDutchResult root){
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.names, root.createdAtString);
        bindings.bind(root.names).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAtString).first(StringExpression::containsIgnoreCase);
    };
}