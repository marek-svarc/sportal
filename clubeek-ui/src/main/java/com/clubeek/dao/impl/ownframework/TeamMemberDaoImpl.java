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
    public RepTeamMember getInstance() {
        return RepTeamMember.getInstance();
    }


}
