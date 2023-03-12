package com.example.dutchpay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendSelectSaveDto {
    private Long id;
    private String name;
    private String phoneNum;
}
