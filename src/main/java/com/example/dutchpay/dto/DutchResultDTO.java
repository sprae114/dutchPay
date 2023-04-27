package com.example.dutchpay.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DutchResultDTO {
    private String createdAtString;
    private String names;
    private String result;

    public DutchResultDTO(String createdAtString, String names, String result) {
        this.createdAtString = createdAtString;
        this.names = names;
        this.result = result;
    }
}
