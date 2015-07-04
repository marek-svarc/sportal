package com.clubeek.dao.impl.springjdbctemplate.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.clubeek.model.TeamMember;

public class TeamMemberMapper implements RowMapper<TeamMember> {

    @Override
    public TeamMember mapRow(ResultSet rs, int rowNum) throws SQLException {
        TeamMember tm = new TeamMember();
        
        tm.setId(rs.getInt("id"));
        tm.setFunctions(rs.getInt("functions"));
        tm.setClubMemberId(rs.getInt("club_member_id"));
        tm.setClubTeamId(rs.getInt("club_team_id"));
        
        return tm;
    }

}
