package com.example.dutchpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;


@ToString
@Entity
@Table(indexes = {
        @Index(name = "idx_user_account_id", columnList = "userAccount_id"),
        @Index(name = "idx_user_account_id_names", columnList = "userAccount_id,names"),
        @Index(name = "idx_user_account_id_created_at_string", columnList = "userAccount_id,createdAtString")
})
@EntityListeners(AuditingEntityListener.class)
public class DutchResult {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dutchResult_id") @Getter
    public Long id;

    @Column(length=1500) @Getter @Setter
    public String result;

    @Getter
    public String names;

    @Getter @Setter
    public String createdAtString;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Getter
    LocalDate createdAt;

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

    public DutchResult(String result, UserAccount userAccount, String names) {
        this.result = result;
        this.userAccount = userAccount;
        this.names = names;
    }

    public DutchResult(String result, UserAccount userAccount, String names, String createdAtString) {
        this.result = result;
        this.userAccount = userAccount;
        this.names = names;
        this.createdAtString = createdAtString;
    }

    static public DutchResult of(String result) {
        return new DutchResult(result);
    }
}
