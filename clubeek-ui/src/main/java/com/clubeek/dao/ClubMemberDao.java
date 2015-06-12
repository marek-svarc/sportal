package com.clubeek.dao;

import java.util.List;

import com.clubeek.model.ClubMember;

public interface ClubMemberDao extends Dao<ClubMember> {

    public ClubMember getClubMember(int id);
    
    public List<ClubMember> getClubMembersByTeamId(int teamId);
    
    public List<ClubMember> getClubMembersByDateOfBirth(int yearMin, int yearMax);
    
    public List<ClubMember> getAllClubMembers();
    
    public void updateClubMember(ClubMember clubMember);
}
