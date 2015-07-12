package com.clubeek.dao.impl.springjdbctemplate.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.clubeek.model.ClubUrl;

public class ClubUrlMapper implements RowMapper<ClubUrl> {

    @Override
    public ClubUrl mapRow(ResultSet rs, int rowNum) throws SQLException {
        ClubUrl u = new ClubUrl();
        
        u.setId(rs.getInt("id"));
        u.setUrl(rs.getString("url"));
        u.setClubId(rs.getInt("club_id"));
        
        return u;
    }
}
