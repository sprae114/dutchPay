package com.example.dutchpay.domain;

import com.example.dutchpay.dto.FriendSelectSaveDto;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@ToString
@Entity
public class Friend {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_id")
    @Getter
    public Long id;

    @Setter @Getter
    @NotBlank
    @Column(length = 30)
    public String name;

    @Setter @Getter
    @NotBlank
    @Column(length = 30)
    public String phoneNum;

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

    public FriendSelectSaveDto toFriendSelectSaveDto(Friend friend) {
        return new FriendSelectSaveDto(friend.getId(), friend.getName(), friend.getPhoneNum());
    }
}
