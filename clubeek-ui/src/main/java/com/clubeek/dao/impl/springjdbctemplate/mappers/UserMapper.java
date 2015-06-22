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
        // TODO vitfo, created on 16. 6. 2015 - change name to username
        u.setName(rs.getString("name"));
        u.setPassword(rs.getString("password"));
        u.setUserRoleType(UserRoleType.values()[rs.getInt("user_role_type")]);
        u.setClubMemberId(rs.getInt("club_member_id"));
        
        return u;
    }

}
