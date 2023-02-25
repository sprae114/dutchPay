package com.example.dutchpay.dto;

import com.example.dutchpay.domain.Friend;
import com.example.dutchpay.domain.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendSaveDto {
    String name;
    String phoneNum;

    public Friend toEntity(UserAccount userAccount) {
        return Friend.of(name, phoneNum, userAccount);
    }
}
