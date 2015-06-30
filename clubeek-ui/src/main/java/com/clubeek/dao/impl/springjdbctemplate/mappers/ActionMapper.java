package com.clubeek.dao.impl.springjdbctemplate.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.clubeek.model.Action;

public class ActionMapper implements RowMapper<Action> {

    @Override
    public Action mapRow(ResultSet rs, int rowNum) throws SQLException {
        Action a = new Action();
        
        a.setId(rs.getInt("id"));
        a.setStart(rs.getTimestamp("start"));
        a.setFinish(rs.getTimestamp("finish"));
        a.setCaption(rs.getString("caption"));
        a.setPlace(rs.getString("place"));
        a.setDescription(rs.getString("description"));
        a.setSignParticipation(rs.getBoolean("sign_participation"));
        a.setClubTeamId((Integer)rs.getObject("club_team_id"));
        a.setCategoryId((Integer)rs.getObject("category_id"));
        
        return a;
    }

}
