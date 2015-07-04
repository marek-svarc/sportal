package com.clubeek.dao;

import java.util.List;

import com.clubeek.model.Event;

public interface EventDao extends Dao<Event> {

    public List<Event> getAllEvents();
    
    // TODO vitfo, created on 30. 6. 2015 - will it be needed?
//    public List<Action> getAllActionsByClubTeamId(int clubTeamId);
//    public List<Action> getAllActionsByCategoryId(int categoryId);
}
