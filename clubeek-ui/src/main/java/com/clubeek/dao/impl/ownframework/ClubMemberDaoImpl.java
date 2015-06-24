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
    public void updateClubMember(ClubMember clubMember) {
        RepClubMember.update(clubMember);        
    }

    @Override
    public void updateRow(ClubMember object) {
        RepClubMember.update(object);        
    }

    @Override
    public void insertRow(ClubMember object) {
        RepClubMember.insert(object);        
    }

    @Override
    public void deleteRow(int id) {
        RepClubMember.delete(id);        
    }

    @Override
    public void exchangeRows(int idA, int idB) {
        throw new UnsupportedOperationException("Not supported.");
    }

}
