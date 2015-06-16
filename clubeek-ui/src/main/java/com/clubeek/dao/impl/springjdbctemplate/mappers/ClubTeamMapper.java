package com.clubeek.dao.impl.springjdbctemplate.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.clubeek.model.ClubTeam;

/**
 * Mapper for {@link ClubTeam} model object.
 * 
 * @author vitfo
 */
public class ClubTeamMapper implements RowMapper<ClubTeam> {

    @Override
    public ClubTeam mapRow(ResultSet rs, int rowNum) throws SQLException {
        ClubTeam ct = new ClubTeam();
        
        ct.setId(rs.getInt("id"));
        ct.setActive(rs.getBoolean("active"));
        ct.setCategoryId(rs.getInt("category_id"));
        ct.setName(rs.getString("name"));
        ct.setSorting(rs.getInt("sorting"));
        
        return ct;
    }
}
