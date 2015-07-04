package com.clubeek.dao.impl.springjdbctemplate.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.clubeek.enums.UserRoleType;
import com.clubeek.model.User;

public class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User u = new User();
        
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setUserRoleType(UserRoleType.values()[rs.getInt("user_role_type")]);
        u.setClubMemberId((Integer)rs.getObject("club_member_id"));
        
        return u;
    }

}
