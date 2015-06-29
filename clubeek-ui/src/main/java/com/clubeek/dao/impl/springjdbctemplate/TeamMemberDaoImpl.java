package com.clubeek.dao.impl.springjdbctemplate;

import java.util.List;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;

import com.clubeek.dao.TeamMemberDao;
import com.clubeek.dao.impl.springjdbctemplate.mappers.TeamMemberMapper;
import com.clubeek.model.ClubMember;
import com.clubeek.model.TeamMember;
import org.springframework.stereotype.Repository;

@Repository
public class TeamMemberDaoImpl extends DaoImpl implements TeamMemberDao {

    @Override
    public void updateRow(TeamMember object) {
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(object);
        template.update("update t_team_member set "
                + "functions = :functions, "
                + "club_member_id = :clubMemberId, "
                + "club_team_id = :clubTeamId "
                + "where id = :id", 
                source);
    }

    @Override
    public void insertRow(TeamMember object) {
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(object);
        template.update("insert into t_team_member "
                + "(functions, club_member_id, club_team_id) values"
                + "(:functions, :clubMemberId, :clubTeamId)", 
                source);        
    }

    @Override
    public void deleteRow(int id) {
        template.getJdbcOperations().update("delete from t_team_member where id = ?", id);
    }

    @Override
    public void exchangeRows(int idA, int idB) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TeamMember> getTeamMembersByTeamId(int clubTeamId) {
        return template.getJdbcOperations().query("select * from t_team_member where club_team_id = ?", new Integer[] {clubTeamId}, new TeamMemberMapper());
    }

    @Override
    public void update(int clubTeamId, List<TeamMember> teamMembers, List<ClubMember> clubMembers) {
        // TODO vitfo, created on 16. 6. 2015 - where it is needed?
        throw new UnsupportedOperationException();
    }

}
