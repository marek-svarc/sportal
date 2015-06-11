package com.clubeek.dao.impl.ownframework;

import java.util.List;

import com.clubeek.dao.ClubMemberDao;
import com.clubeek.dao.impl.ownframework.rep.RepClubMember;
import com.clubeek.model.ClubMember;
import com.clubeek.model.ClubTeam;

public class ClubMemberDaoImpl implements ClubMemberDao {

    @Override
    public ClubMember getClubMember(int id) {
        return RepClubMember.selectById(id, null);
    }

    @Override
    public List<ClubMember> getClubMembersByTeamId(int teamId) {
        return RepClubMember.selectByTeamId(teamId, null);
    }

    @Override
    public List<ClubMember> getClubMembersByDateOfBirth(int yearMin, int yearMax) {
        return RepClubMember.dbSelectByYearOfBirth(yearMin, yearMax, null);
    }

    @Override
    public List<ClubMember> getAllClubMembers() {
        return RepClubMember.selectAll(null);
    }

    @Override
    public RepClubMember getInstance() {
        return RepClubMember.getInstance();
    }

    @Override
    public void updateClubMember(ClubMember clubMember) {
        RepClubMember.update(clubMember);        
    }

}
