package com.example.dutchpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;


@ToString
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class DutchResult {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dutchResult_id")
    public Long id;

    @Column(length=1000) @Getter
    @Setter public String result;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "userAccount_id")
    @Getter
    public UserAccount userAccount;

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;

        if (!userAccount.getDutchResults().contains(this)) {
            userAccount.getDutchResults().add(this);
        }
    }

    public DutchResult() {
    }

    public DutchResult(String result) {
        this.result = result;
    }

    public DutchResult(String result, UserAccount userAccount) {
        this.result = result;
        this.userAccount = userAccount;
    }

    public DutchResult(String result, LocalDateTime createdAt, UserAccount userAccount) {
        this.result = result;
        this.createdAt = createdAt;
        this.userAccount = userAccount;
    }

    static public DutchResult of(String result) {
        return new DutchResult(result);
    }
}
