package com.example.dutchpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UserAccount {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userAccount_id")
    @Getter
    public Long id;

    @Setter @Column(nullable = false, length = 50) public String name;
    @Setter @Column(nullable = false, length = 50) public String email;
    @Setter @Column(nullable = false, length = 50) public String password;


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

    public UserAccount(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public UserAccount(String name,
                       List<DutchResult> dutchResults,
                       List<Friend> friends,
                       String email,
                       String password) {

        this.name = name;
        this.dutchResults = dutchResults;
        this.friends = friends;
        this.email = email;
        this.password = password;
    }

    static public UserAccount of (String name, String email, String password) {
        return new UserAccount(name, email, password);
    }
}
