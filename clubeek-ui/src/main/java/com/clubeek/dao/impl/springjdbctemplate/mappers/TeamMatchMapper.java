package com.clubeek.dao.impl.springjdbctemplate.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.clubeek.model.TeamMatch;

public class TeamMatchMapper implements RowMapper<TeamMatch> {

    @Override
    public TeamMatch mapRow(ResultSet rs, int rowNum) throws SQLException {
        TeamMatch t = new TeamMatch();
        
        t.setId(rs.getInt("id"));
        t.setStart(rs.getTimestamp("start"));
        t.setScorePos(rs.getInt("score_pos"));
        t.setScoreNeg(rs.getInt("score_neg"));
        t.setHomeCourt(rs.getBoolean("home_court"));
        t.setComment(rs.getString("comment"));
        t.setPublish(rs.getBoolean("publish"));
        t.setClubTeamId(rs.getInt("club_team_id"));
        t.setClubRivalId(rs.getInt("club_rival_id"));
        t.setClubRivalComment(rs.getString("club_rival_comment"));
        
        return t;
    }

}
