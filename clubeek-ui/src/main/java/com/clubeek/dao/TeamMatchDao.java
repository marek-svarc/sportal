package com.clubeek.dao;

import java.util.List;

import com.clubeek.model.TeamMatch;

public interface TeamMatchDao extends Dao<TeamMatch> {

    public List<TeamMatch> getAllTeamMatches();
}
