package com.example.security;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.example.persistence.entity.UserInfo;

public class MyUserDetails extends User {
    private static final long serialVersionUID = 1L;
    private final String userNameJP;
    private final String sectionNameJP;

    public MyUserDetails(UserInfo userInfo, List<? extends GrantedAuthority> authorityList) {
        super(userInfo.getUserId(), userInfo.getPassword(), userInfo.getEnabled(), true, true, true, authorityList);
        this.userNameJP = userInfo.getUserNameJP();
        this.sectionNameJP = userInfo.getSectionNameJP();
    }

    public String getUserNameJP() {
        return userNameJP;
    }

    public String getSectionNameJP() {
        return sectionNameJP;
    }
}