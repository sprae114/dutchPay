package com.example.dutchpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@ToString
@Entity
public class Friend {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_id")
    public Long id;

    @Setter
    @Column(nullable = false, length = 50) public String name;
    @Setter @Column(nullable = false, length = 30) public String phoneNum;

    @ManyToOne
    @JoinColumn(name = "userAccount_id")
    @Getter
    public UserAccount userAccount;

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;

        if (!userAccount.getFriends().contains(this)) {
            userAccount.getFriends().add(this);
        }
    }

    public Friend() {
    }

    public Friend(String name, String phoneNum) {
        this.name = name;
        this.phoneNum = phoneNum;
    }

    public Friend(String name, String phoneNum, UserAccount userAccount) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.userAccount = userAccount;
    }

    static public Friend of(String name, String phoneNum, UserAccount userAccount) {
        return new Friend(name, phoneNum, userAccount);
    }
}
