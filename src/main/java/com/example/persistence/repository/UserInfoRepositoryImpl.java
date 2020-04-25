package com.example.persistence.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.persistence.entity.UserInfo;
import com.example.persistence.entity.UserRoles;

@Repository
public class UserInfoRepositoryImpl implements UserInfoRepository{
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    private static final RowMapper<UserInfo> INFO_MAPPER = (rs, rowNum) ->
    new UserInfo(rs.getString("UserId"),
            rs.getString("UserNameJP"),
            rs.getString("SectionNameJP"),
            rs.getBoolean("Enabled")
            );

    private static final RowMapper<UserRoles> ROLES_MAPPER = (rs, rowNum) ->
    new UserRoles(rs.getString("UserId"),
            rs.getString("Authority")
            );

    @Override
    public Optional<UserInfo> selectDetailByUserId(String id) {
        try {

            UserInfo userInfo = new UserInfo();
            List<UserRoles> userRolesList = new ArrayList<UserRoles>();

            String sql = "SELECT UserInfo.UserId, UserInfo.Password, UserInfo.UserNameJP, UserInfo.SectionNameJP, UserInfo.Enabled"
                    + " , UserRoles.Authority"
                    + " FROM UserInfo INNER JOIN UserRoles ON UserInfo.UserId = UserRoles.UserId "
                    + " WHERE (UserInfo.UserId = :id)";

            MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue("id", id);
            List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, parameterSource);

            for(Map<String, Object> result : resultList) {
                UserRoles userRoles = new UserRoles();
                userRoles.setUserId((String) result.get("UserId"));
                userRoles.setAuthority((String) result.get("Authority"));
                userRolesList.add(userRoles);

                userInfo.setUserId((String) result.get("UserId"));
                userInfo.setPassword((String) result.get("Password"));
                userInfo.setUserNameJP((String) result.get("UserNameJP"));
                userInfo.setSectionNameJP((String) result.get("SectionNameJP"));
                userInfo.setEnabled((Boolean) result.get("Enabled"));
            }

            userInfo.setUserRolesList(userRolesList);

            return Optional.of(userInfo);

        }catch (EmptyResultDataAccessException e) {

            return Optional.empty();
        }
    }

    @Override
    public Integer insert(UserInfo userInfo) {
        String sql = "INSERT INTO UserInfo"
                + " VALUES(:UserId, :Password, :UserNameJP, :SectionNameJP, :Enabled);";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("UserId",          userInfo.getUserId());
        parameterSource.addValue("Password",        userInfo.getPassword());
        parameterSource.addValue("UserNameJP",      userInfo.getUserNameJP());
        parameterSource.addValue("SectionNameJP",   userInfo.getSectionNameJP());
        parameterSource.addValue("Enabled",         userInfo.getEnabled());

        Integer rows = jdbcTemplate.update(sql, parameterSource);
        return rows;
    }

    @Override
    public Integer update(UserInfo userInfo) {
        String sql = "UPDATE UserInfo SET "
                + " Password = :Password "
                + ",UserNameJP = :UserNameJP "
                + ",SectionNameJP = :SectionNameJP "
                + ",Enabled = :Enabled "
                + " WHERE UserId = :UserId;";

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("UserId",          userInfo.getUserId());
        parameterSource.addValue("Password",        userInfo.getPassword());
        parameterSource.addValue("UserNameJP",      userInfo.getUserNameJP());
        parameterSource.addValue("SectionNameJP",   userInfo.getSectionNameJP());
        parameterSource.addValue("Enabled",         userInfo.getEnabled());

        Integer rows = jdbcTemplate.update(sql, parameterSource);
        return rows;
    }

    @Override
    public Integer delete(String userId) {
        String sql = "DELETE FROM UserInfo WHERE UserId = :UserId;";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("UserId", userId);

        Integer rows = jdbcTemplate.update(sql, parameterSource);
        return rows;
    }

    @Override
    public List<UserInfo> findUserAll() {
        String sql1 = "SELECT DISTINCT UserInfo.UserId, UserInfo.UserNameJP, UserInfo.SectionNameJP, UserInfo.Enabled"
                + " FROM UserInfo INNER JOIN UserRoles ON UserInfo.UserId = UserRoles.UserId "
                + " WHERE UserRoles.Authority = 'ROLE_USER'"
                + " ORDER BY UserInfo.UserId;" ;

        List<UserInfo> userInfoList = jdbcTemplate.query(sql1, INFO_MAPPER);

        userInfoList.forEach(userInfo -> {
            String sql2 = "SELECT * FROM UserRoles WHERE UserId = :UserId;";
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue("UserId", userInfo.getUserId());
            List<UserRoles> userRolesList = jdbcTemplate.query(sql2, parameterSource, ROLES_MAPPER);

            userInfo.setUserRolesList(userRolesList);
        });

        return userInfoList;
    }
}