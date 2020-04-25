package com.example.persistence.repository;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.persistence.entity.UserRoles;

@Repository
public class UserRolesRepositoryImpl implements UserRolesRepository {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Integer insert(UserRoles userRoles) {
        String sql = "INSERT INTO UserRoles"
                + " VALUES(:UserId, :Authority);";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("UserId", userRoles.getUserId());
        parameterSource.addValue("Authority", userRoles.getAuthority());

        Integer rows = jdbcTemplate.update(sql, parameterSource);
        return rows;
    }

    @Override
    public Integer update(UserRoles userRoles) {
        String sql = "UPDATE UserRoles SET Authority = :Authority"
                + " WHERE UserId = :UserId;";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("UserId", userRoles.getUserId());
        parameterSource.addValue("Authority", userRoles.getAuthority());

        Integer rows = jdbcTemplate.update(sql, parameterSource);
        return rows;
    }

    @Override
    public Integer delete(String userId) {
        String sql = "DELETE FROM UserRoles WHERE UserId = :UserId;";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("UserId", userId);

        Integer rows = jdbcTemplate.update(sql, parameterSource);
        return rows;
    }

    @Override
    public Integer adminCheck() {
        Integer rows = 0;

        String sql = "SELECT COUNT(*) AS CNT"
                + " FROM UserRoles"
                + " WHERE Authority = 'ROLE_ADMIN';" ;
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();

        Map<String, Object> result = jdbcTemplate.queryForMap(sql,parameterSource);
        rows = Integer.parseInt(result.get("CNT").toString());

        return rows;
    }
}