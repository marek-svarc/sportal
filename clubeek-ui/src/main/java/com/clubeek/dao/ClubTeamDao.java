package com.clubeek.dao;

import java.util.List;

import com.clubeek.dao.impl.ownframework.rep.RepClubTeam;
import com.clubeek.model.ClubTeam;

public interface ClubTeamDao {

    public ClubTeam getClubTeamById(int id);
    
    public List<ClubTeam> getActiveClubTeams();
    
    public List<ClubTeam> getAllClubTeams();
    
    // TODO vitfo, created on 11. 6. 2015 - remove
    public RepClubTeam getInstance();
}
