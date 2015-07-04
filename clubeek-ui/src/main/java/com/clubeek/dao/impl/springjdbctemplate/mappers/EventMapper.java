package com.clubeek.dao.impl.springjdbctemplate.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.clubeek.enums.OwnerType;
import com.clubeek.model.Event;

public class EventMapper implements RowMapper<Event> {

    @Override
    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        Event e = new Event();
        
        e.setId(rs.getInt("id"));
        e.setStart(rs.getTimestamp("start"));
        e.setFinish(rs.getTimestamp("finish"));
        e.setCaption(rs.getString("caption"));
        e.setPlace(rs.getString("place"));
        e.setDescription(rs.getString("description"));
        e.setSignParticipation(rs.getBoolean("sign_participation"));
        e.setOwnerType(OwnerType.values()[rs.getInt("owner_type")]);
        e.setClubTeamId((Integer)rs.getObject("club_team_id"));
        e.setCategoryId((Integer)rs.getObject("category_id"));
        
        return e;
    }

}
