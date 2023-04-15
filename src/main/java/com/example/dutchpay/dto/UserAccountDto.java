package com.example.dutchpay.dto;


import com.example.dutchpay.domain.UserAccount;

import java.util.Objects;

public class UserAccountDto {
    Long id;
    String name;
    String email;

    public UserAccountDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static UserAccountDto from(UserAccount entity) {
        return new UserAccountDto(entity.getId(), entity.getName(), entity.getEmail());
    }

    public static UserAccountDto of(Long id, String name, String email) {
        return new UserAccountDto(id, name, email);
    }

    public UserAccount toEntity() {
        return UserAccount.of(id, name, email);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccountDto that = (UserAccountDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }
}

