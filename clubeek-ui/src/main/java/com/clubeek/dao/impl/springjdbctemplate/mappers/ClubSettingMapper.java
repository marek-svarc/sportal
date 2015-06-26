package com.clubeek.dao.impl.springjdbctemplate.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.clubeek.model.ClubSetting;

public class ClubSettingMapper implements RowMapper<ClubSetting> {

    @Override
    public ClubSetting mapRow(ResultSet rs, int rowNum) throws SQLException {
        ClubSetting cs = new ClubSetting();
        
        cs.setId(rs.getInt("id"));
        cs.setTitle(rs.getString("title"));
        cs.setComment(rs.getString("comment"));
        cs.setLogo(rs.getBytes("logo"));
        
        return cs;
    }

}
