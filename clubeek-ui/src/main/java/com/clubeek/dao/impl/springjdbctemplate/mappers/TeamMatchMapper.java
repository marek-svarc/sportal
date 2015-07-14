package com.clubeek.dao.impl.springjdbctemplate.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.clubeek.enums.MatchType;
import com.clubeek.model.TeamMatch;

public class TeamMatchMapper implements RowMapper<TeamMatch> {

    @Override
    public TeamMatch mapRow(ResultSet rs, int rowNum) throws SQLException {
        TeamMatch t = new TeamMatch();
        
        t.setId(rs.getInt("id"));
        
        t.setHomeMatch(rs.getBoolean("is_home_match"));
        t.setMatchType(MatchType.values()[rs.getInt("match_type")]);
        t.setStart(rs.getTimestamp("start"));
        t.setComment(rs.getString("comment"));
        t.setClubRivalComment(rs.getString("club_rival_comment"));
        t.setPublish(rs.getBoolean("publish"));
        t.setScoreA((Integer)rs.getObject("score_A"));
        t.setScoreB((Integer)rs.getObject("score_B"));
        t.setScoreDetail(rs.getString("score_detail"));
        
        t.setSeasonId((Integer)rs.getObject("season_id"));
        t.setClubTeamId(rs.getInt("club_team_id"));
        t.setClubRivalId((Integer)rs.getObject("club_rival_id"));
        
        return t;
    }

}
