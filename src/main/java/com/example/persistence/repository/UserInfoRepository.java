package com.example.persistence.repository;

import java.util.List;
import java.util.Optional;

import com.example.persistence.entity.UserInfo;

public interface UserInfoRepository {
    Optional<UserInfo> selectDetailByUserId(String id);

    Integer insert(UserInfo userInfo);

    Integer update(UserInfo userInfo);

    Integer delete(String userId);

    List<UserInfo> findUserAll();
}