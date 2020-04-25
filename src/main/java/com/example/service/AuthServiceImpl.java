package com.example.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.persistence.entity.UserInfo;
import com.example.persistence.entity.UserRoles;
import com.example.persistence.repository.UserInfoRepository;
import com.example.persistence.repository.UserRolesRepository;
import com.example.web.form.AuthForm;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    UserRolesRepository userRolesRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void insertAdmin(AuthForm authForm) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        UserInfo userInfo = new UserInfo();
        UserRoles userRoles = new UserRoles();
        List<UserRoles> userRokesList  = new  ArrayList<UserRoles>();

        userInfo.setUserId(authForm.getUserId());
        userInfo.setPassword(encoder.encode( authForm.getPassword() ));

        userInfo.setUserNameJP("管理者");
        userInfo.setSectionNameJP("管理者");
        userInfo.setEnabled(true);

        userRoles.setUserId(authForm.getUserId());
        userRoles.setAuthority("ROLE_ADMIN");
        userRokesList.add(userRoles);

        userInfo.setUserRolesList(userRokesList);

        userInfoRepository.insert(userInfo);

        userInfo.getUserRolesList().forEach(s -> {
            userRolesRepository.insert(s);
        });
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void insertUser(AuthForm authForm) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        UserInfo userInfo = new UserInfo();

        userInfo.setUserId(authForm.getUserId());
        userInfo.setPassword(encoder.encode( authForm.getPassword() ));

        userInfo.setUserNameJP(authForm.getUserNameJP());
        userInfo.setSectionNameJP(authForm.getSectionNameJP());
        userInfo.setEnabled(true);

        userInfoRepository.insert(userInfo);

        List<String> authorityList = new ArrayList<String>(Arrays.asList(authForm.getAuthority()));
        authorityList.add("ROLE_USER");

        authorityList.forEach(s -> {
            UserRoles userRoles = new UserRoles();
            userRoles.setUserId(authForm.getUserId());
            userRoles.setAuthority(s);

            userRolesRepository.insert(userRoles);
        });
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Integer adminCheck() {
        return userRolesRepository.adminCheck();
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateUser(AuthForm authForm){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        UserInfo userInfo = new UserInfo();

        userInfo.setUserId(authForm.getUserId());
        userInfo.setPassword(encoder.encode(authForm.getPassword()));
        userInfo.setUserNameJP(authForm.getUserNameJP());
        userInfo.setSectionNameJP(authForm.getSectionNameJP());
        userInfo.setEnabled(authForm.getEnabled());

        userInfoRepository.update(userInfo);

        userRolesRepository.delete(authForm.getUserId());

        List<String> authorityList = new ArrayList<String>(Arrays.asList(authForm.getAuthority()));
        authorityList.add("ROLE_USER");

        authorityList.forEach(s -> {
            UserRoles userRoles = new UserRoles();
            userRoles.setUserId(authForm.getUserId());
            userRoles.setAuthority(s);

            userRolesRepository.insert(userRoles);
        });
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteUser(String id) {
        userInfoRepository.delete(id);
        userRolesRepository.delete(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<UserInfo> findUserAll() {
        return userInfoRepository.findUserAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public AuthForm userRegById(String id){
        AuthForm authForm = new AuthForm();

        Optional<UserInfo> userInfoOpt = userInfoRepository.selectDetailByUserId(id);

        userInfoOpt.ifPresentOrElse(userInfo -> {
            authForm.setUserId(userInfo.getUserId());
            authForm.setPassword(userInfo.getPassword());
            authForm.setUserNameJP(userInfo.getUserNameJP());
            authForm.setSectionNameJP(userInfo.getSectionNameJP());
            authForm.setEnabled(userInfo.getEnabled());

            String[] arrayAuthority = new String[userInfo.getUserRolesList().size()];

            Integer i = 0;
            for (UserRoles ur : userInfo.getUserRolesList()) {
                arrayAuthority[i] = ur.getAuthority();
                i++;
            }

            authForm.setAuthority(arrayAuthority);
        },null);

        return authForm;
    }
}