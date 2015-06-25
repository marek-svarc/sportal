package com.clubeek.dao;

import java.util.List;

import com.clubeek.model.TeamTraining;

public interface TeamTrainingDao extends Dao<TeamTraining> {

    public List<TeamTraining> getAllTeamTrainings();
}
