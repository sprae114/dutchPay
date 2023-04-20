package com.example.dutchpay.dto;

import com.example.dutchpay.domain.Friend;
import com.example.dutchpay.domain.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendSaveDto {
    @NotBlank
    @Column(length = 30)
    String name;

    @NotBlank
    @Column(length = 30)
    String phoneNum;

    public Friend toEntity(UserAccount userAccount) {
        return Friend.of(name, phoneNum, userAccount);
    }
}
