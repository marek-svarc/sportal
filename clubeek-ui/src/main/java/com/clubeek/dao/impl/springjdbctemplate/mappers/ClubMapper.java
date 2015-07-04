package com.clubeek.dao.impl.springjdbctemplate.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.clubeek.model.Club;

public class ClubMapper implements RowMapper<Club> {

    @Override
    public Club mapRow(ResultSet rs, int rowNum) throws SQLException {
        Club cs = new Club();
        
        cs.setId(rs.getInt("id"));
        cs.setTitle(rs.getString("title"));
        cs.setComment(rs.getString("comment"));
        cs.setLogo(rs.getBytes("logo"));
        
        return cs;
    }

}
