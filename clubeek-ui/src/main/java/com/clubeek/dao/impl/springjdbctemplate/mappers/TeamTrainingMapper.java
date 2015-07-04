package com.clubeek.dao.impl.springjdbctemplate.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.clubeek.model.TeamTraining;

/**
 * Mapper for {@link TeamTraining}.
 *
 * @author vitfo
 */
public class TeamTrainingMapper implements RowMapper<TeamTraining> {

    @Override
    public TeamTraining mapRow(ResultSet rs, int rowNum) throws SQLException {
        TeamTraining tt = new TeamTraining();
        
        tt.setId(rs.getInt("id"));
        tt.setStart(rs.getTimestamp("start"));
        tt.setEnd(rs.getTimestamp("finish"));
        tt.setPlace(rs.getString("place"));
        tt.setComment(rs.getString("comment"));
        tt.setClubTeamId(rs.getInt("club_team_id"));
        
        return tt;
    }
}
