package com.example.persistence.entity;

import java.util.List;

import lombok.Data;

@Data
public class UserInfo {
    private String userId;
    private String password;
    private String userNameJP;
    private String sectionNameJP;
    private Boolean enabled;
    private List<UserRoles> userRolesList;

    public UserInfo() {}

    public UserInfo(String userId, String userNameJP, String sectionNameJP, Boolean enabled) {
        this.userId = userId;
        this.userNameJP = userNameJP;
        this.sectionNameJP = sectionNameJP;
        this.enabled = enabled;
    }
}