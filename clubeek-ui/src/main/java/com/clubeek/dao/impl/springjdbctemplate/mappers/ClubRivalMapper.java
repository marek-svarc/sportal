package com.clubeek.dao.impl.springjdbctemplate.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.clubeek.model.ClubRival;

public class ClubRivalMapper implements RowMapper<ClubRival> {

    @Override
    public ClubRival mapRow(ResultSet rs, int rowNum) throws SQLException {
        ClubRival cr = new ClubRival();
        
        cr.setId(rs.getInt("id"));
        cr.setName(rs.getString("name"));
        cr.setWeb(rs.getString("web"));
        cr.setGPS(rs.getString("gps"));
        cr.setStreet(rs.getString("street"));
        cr.setCity(rs.getString("city"));
        cr.setCode(rs.getString("code"));
        cr.setIcon(rs.getBytes("icon"));
        
        return cr;
    }

}
