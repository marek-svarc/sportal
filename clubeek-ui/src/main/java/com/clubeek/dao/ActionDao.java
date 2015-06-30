package com.clubeek.dao;

import java.util.List;

import com.clubeek.model.Action;

public interface ActionDao extends Dao<Action> {

    public List<Action> getAllActions();
    
    // TODO vitfo, created on 30. 6. 2015 - will it be needed?
//    public List<Action> getAllActionsByClubTeamId(int clubTeamId);
//    public List<Action> getAllActionsByCategoryId(int categoryId);
}
