package com.clubeek.dao.impl.ownframework;

import java.util.List;

import com.clubeek.dao.TeamTrainingDao;
import com.clubeek.dao.impl.ownframework.rep.RepTeamTraining;
import com.clubeek.model.TeamTraining;

public class TeamTrainingDaoImpl implements TeamTrainingDao {

    @Override
    public void updateRow(TeamTraining object) {
        RepTeamTraining.update(object);
    }

    @Override
    public void insertRow(TeamTraining object) {
        RepTeamTraining.insert(object);        
    }

    @Override
    public void deleteRow(int id) {
        RepTeamTraining.delete(id);        
    }

    @Override
    public void exchangeRows(int idA, int idB) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TeamTraining> getAllTeamTrainings() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteRows(List<TeamTraining> objects) {
        throw new UnsupportedOperationException("Not supported.");
    }

}
