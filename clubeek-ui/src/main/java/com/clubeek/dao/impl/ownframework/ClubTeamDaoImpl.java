package com.clubeek.dao.impl.ownframework;

import java.util.List;

import com.clubeek.dao.ClubTeamDao;
import com.clubeek.dao.impl.ownframework.rep.RepClubTeam;
import com.clubeek.model.ClubTeam;

public class ClubTeamDaoImpl implements ClubTeamDao {

    @Override
    public ClubTeam getClubTeamById(int id) {
        return RepClubTeam.selectById(id, null);
    }

    @Override
    public List<ClubTeam> getActiveClubTeams() {
        return RepClubTeam.select(true, null);
    }

    @Override
    public List<ClubTeam> getAllClubTeams() {
        return RepClubTeam.select(false, null);
    }

    @Override
    public RepClubTeam getInstance() {
        return RepClubTeam.getInstance();
    }

    @Override
    public void updateRow(ClubTeam object) {
        RepClubTeam.update(object);
    }

    @Override
    public void insertRow(ClubTeam object) {
        RepClubTeam.insert(object);        
    }

    @Override
    public void deleteRow(int id) {
        RepClubTeam.delete(id);        
    }

    @Override
    public void exchangeRows(int idA, int idB) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not supported.");
    }

}
