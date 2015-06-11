package com.clubeek.dao;

import java.util.List;

import com.clubeek.dao.impl.ownframework.rep.RepClubMember;
import com.clubeek.model.ClubMember;

public interface ClubMemberDao {

    public ClubMember getClubMember(int id);
    
    public List<ClubMember> getClubMembersByTeamId(int teamId);
    
    public List<ClubMember> getClubMembersByDateOfBirth(int yearMin, int yearMax);
    
    public List<ClubMember> getAllClubMembers();
    
    // TODO vitfo, created on 11. 6. 2015 - remove
    public RepClubMember getInstance();
    
    public void updateClubMember(ClubMember clubMember);
}
