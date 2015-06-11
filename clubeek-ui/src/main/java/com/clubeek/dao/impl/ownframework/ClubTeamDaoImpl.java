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

}
