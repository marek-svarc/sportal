package com.clubeek.dao.impl.ownframework;

import java.util.List;

import com.clubeek.dao.TeamMatchDao;
import com.clubeek.dao.impl.ownframework.rep.RepTeamMatch;
import com.clubeek.model.TeamMatch;

public class TeamMatchDaoImpl implements TeamMatchDao {

    @Override
    public void updateRow(TeamMatch object) {
        RepTeamMatch.update(object);        
    }

    @Override
    public void insertRow(TeamMatch object) {
        RepTeamMatch.insert(object);        
    }

    @Override
    public void deleteRow(int id) {
        RepTeamMatch.delete(id);        
    }

    @Override
    public void exchangeRows(int idA, int idB) {
        throw new UnsupportedOperationException("Not supported."); 
    }

    @Override
    public void deleteRows(List<TeamMatch> objects) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public List<TeamMatch> getAllTeamMatches() {
        throw new UnsupportedOperationException("Not supported.");
    }

}
