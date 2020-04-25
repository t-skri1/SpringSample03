package com.example.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.persistence.entity.UserInfo;
import com.example.persistence.entity.UserRoles;
import com.example.persistence.repository.UserInfoRepository;

public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    UserInfoRepository userInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<UserInfo> userInfoOpt = userInfoRepository.selectDetailByUserId(userId);
        if (userInfoOpt == null) {
            throw new UsernameNotFoundException("");
        }

        UserInfo userInfo = userInfoOpt.get();

        List<SimpleGrantedAuthority> authorityList = new ArrayList<SimpleGrantedAuthority>();

        List<UserRoles> roles = userInfo.getUserRolesList();
        for (UserRoles role: roles) {
            authorityList.add(new SimpleGrantedAuthority(role.getAuthority()));
        }

        return new MyUserDetails(userInfo, authorityList);
    }
}