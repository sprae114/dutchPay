package com.example.dutchpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UserAccount {

    @Id
    @Column(name = "userAccount_id")
    @Getter
    public Long id;

    @Getter @Column(nullable = false, length = 50) public String name;
    @Getter @Column(nullable = false, length = 50) public String email;

    @OneToMany(mappedBy = "userAccount")
    @Getter
    public List<DutchResult> dutchResults = new ArrayList<>();

    public void setDutchResult(DutchResult dutchResult) {
        dutchResults.add(dutchResult);

        if (dutchResult.getUserAccount() != this) {
            dutchResult.setUserAccount(this);
        }
    }

    @OneToMany(mappedBy = "userAccount")
    @Getter
    public List<Friend> friends = new ArrayList<>();

    public void setFriend(Friend friend) {
        friends.add(friend);

        if (friend.getUserAccount() != this) {
            friend.setUserAccount(this);
        }
    }


    public UserAccount() {
    }

    public UserAccount(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    static public UserAccount of (Long id,String name, String email) {
        return new UserAccount(id, name, email);
    }
}
