package com.example.persistence.repository;

import com.example.persistence.entity.UserRoles;

public interface UserRolesRepository {
    Integer insert(UserRoles userRoles);

    Integer update(UserRoles userRoles);

    Integer delete(String userId);

    Integer adminCheck();
}