package com.clubeek.dao.impl.ownframework;

import java.util.List;

import com.clubeek.dao.TeamMemberDao;
import com.clubeek.dao.impl.ownframework.rep.RepTeamMember;
import com.clubeek.model.ClubMember;
import com.clubeek.model.TeamMember;

public class TeamMemberDaoImpl implements TeamMemberDao {

    @Override
    public List<TeamMember> getTeamMembersByTeamId(int clubTeamId) {
        return RepTeamMember.selectByTeamId(clubTeamId, null);
    }

    @Override
    public void update(int clubTeamId, List<TeamMember> teamMembers, List<ClubMember> clubMembers) {
        RepTeamMember.update(teamMembers,
                RepTeamMember.selectOrCreateByClubMembers(clubTeamId, clubMembers, null));
        
    }

    @Override
    public void updateRow(TeamMember object) {
        RepTeamMember.update(object);        
    }

    @Override
    public void insertRow(TeamMember object) {
        RepTeamMember.insert(object);        
    }

    @Override
    public void deleteRow(int id) {
        RepTeamMember.delete(id);        
    }

    @Override
    public void exchangeRows(int idA, int idB) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not supported.");
    }


}
