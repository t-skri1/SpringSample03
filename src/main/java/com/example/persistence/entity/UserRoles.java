package com.example.persistence.entity;

import lombok.Data;

@Data
public class UserRoles {
    private String userId;
    private String authority;

    public UserRoles() {}

    public UserRoles(String userId, String authority) {
        this.userId = userId;
        this.authority = authority;
    }

}