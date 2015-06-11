package com.clubeek.dao;

import java.util.List;

import com.clubeek.dao.impl.ownframework.rep.RepTeamMember;
import com.clubeek.model.ClubMember;
import com.clubeek.model.TeamMember;

public interface TeamMemberDao {

    public List<TeamMember> getTeamMembersByTeamId(int clubTeamId);
    
    // TODO vitfo, created on 11. 6. 2015 - dodělat
    public void update(int clubTeamId, List<TeamMember> teamMembers, List<ClubMember> clubMembers);
    
    public RepTeamMember getInstance();
}
