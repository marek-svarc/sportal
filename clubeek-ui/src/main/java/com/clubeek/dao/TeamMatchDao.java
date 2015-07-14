package com.clubeek.dao;

import java.util.List;

import com.clubeek.model.TeamMatch;

public interface TeamMatchDao extends Dao<TeamMatch> {

    /**
     * Gets all matches for all teams for all clubs. Use it just in tests.
     * 
     * @return all matches.
     */
    public List<TeamMatch> getAllTeamMatches();
    
    /**
     * Gets all matches for club team with id.
     * 
     * @param clubTeamId - id of the club
     * @return all matches for club team
     */
    public List<TeamMatch> getAllTeamMatchesForClubTeam(int clubTeamId);
    
    /**
     * Gets all publishable matches for club team with id.
     * 
     * @param clubTeamId - id of the club
     * @return all publishable matches for club team
     */
    public List<TeamMatch> getAllPublishableTeamMatchesForClubTeam(int clubTeamId);
    
    /**
     * Gets all home publishable matches for club team with id. The request can be limited by number of months -> how old the match could be.
     * 
     * @param clubTeamId - id of the club
     * @return all publishable matches for club team (limited by number of months)
     */
    public List<TeamMatch> getAllPublishableHomeTeamMatchesForClubTeam(int clubTeamId, int monthLimit);
}
