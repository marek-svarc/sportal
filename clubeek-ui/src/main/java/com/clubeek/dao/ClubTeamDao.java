package com.clubeek.dao;

import java.util.List;

import com.clubeek.dao.impl.ownframework.rep.RepClubTeam;
import com.clubeek.model.Article;
import com.clubeek.model.ClubTeam;

public interface ClubTeamDao extends Dao<ClubTeam> {

    public ClubTeam getClubTeamById(int id);
    
    public List<ClubTeam> getActiveClubTeams();
    
    public List<ClubTeam> getAllClubTeams();
}
