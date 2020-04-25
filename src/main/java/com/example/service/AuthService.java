package com.example.service;

import java.util.List;

import com.example.persistence.entity.UserInfo;
import com.example.web.form.AuthForm;

public interface AuthService {
    void insertAdmin(AuthForm authForm);

    void insertUser(AuthForm authForm);

    void updateUser(AuthForm authForm);

    void deleteUser(String id);

    Integer adminCheck();

    List<UserInfo> findUserAll();

    AuthForm userRegById(String id);

}