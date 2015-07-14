package com.clubeek.dao.impl.springjdbctemplate;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import com.clubeek.dao.TeamMatchDao;
import com.clubeek.dao.impl.springjdbctemplate.mappers.TeamMatchMapper;
import com.clubeek.model.TeamMatch;

@Repository
public class TeamMatchDaoImpl extends DaoImpl implements TeamMatchDao {

    @Override
    public void updateRow(TeamMatch object) {
        template.getJdbcOperations().update("update t_team_match set "
                + "is_home_match = ?, "
                + "match_type = ?, "
                + "start = ?, "
                + "comment = ?, "
                + "club_rival_comment = ?, "
                + "publish = ?, "
                + "score_A = ?, "
                + "score_B = ?, "
                + "score_detail = ?, "
                
                + "season_id = ?, "
                + "club_team_id = ?, "
                + "club_rival_id = ? "
                + "where id = ?", new Object[]{
                        object.isHomeMatch(),
                        object.getMatchType().ordinal(),
                        object.getStart(),
                        object.getComment(),
                        object.getClubRivalComment(),
                        object.getPublish(),
                        object.getScoreA(),
                        object.getScoreB(),
                        object.getScoreDetail(),
                        
                        object.getSeasonId(),
                        object.getClubTeamId(),
                        object.getClubRivalId(),
                        object.getId()
                });
        
    }

    @Override
    public void insertRow(TeamMatch object) {
        template.getJdbcOperations().update("insert into t_team_match ("
                + "is_home_match, "
                + "match_type, "
                + "start , "
                + "comment, "
                + "club_rival_comment, "
                + "publish, "
                + "score_A, "
                + "score_B, "
                + "score_detail, "
                
                + "season_id, "
                + "club_team_id, "
                + "club_rival_id) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new Object[]{
                        object.isHomeMatch(),
                        object.getMatchType().ordinal(),
                        object.getStart(),
                        object.getComment(),
                        object.getClubRivalComment(),
                        object.getPublish(),
                        object.getScoreA(),
                        object.getScoreB(),
                        object.getScoreDetail(),
                        
                        object.getSeasonId(),
                        object.getClubTeamId(),
                        object.getClubRivalId()
                });
    }

    @Override
    public void deleteRow(int id) {
        template.getJdbcOperations().update("delete from t_team_match where id = ?", new Object[] {id});        
    }

    @Override
    public void exchangeRows(int idA, int idB) {
        throw new UnsupportedOperationException();        
    }

    @Override
    public void deleteRows(List<TeamMatch> objects) {
        SqlParameterSource[] sources = new SqlParameterSourceUtils().createBatch(objects.toArray());
        template.batchUpdate("delete from t_team_match where id = :id", sources);
    }

    @Override
    public List<TeamMatch> getAllTeamMatches() {
        return template.query("select * from t_team_match", new TeamMatchMapper());
    }

    @Override
    public List<TeamMatch> getAllTeamMatchesForClubTeam(int clubTeamId) {
        return template.getJdbcOperations().query("select * from t_team_match where club_team_id = ?", new Object[]{clubTeamId}, new TeamMatchMapper());
    }

    @Override
    public List<TeamMatch> getAllPublishableTeamMatchesForClubTeam(int clubTeamId) {
        return template.getJdbcOperations().query("select * from t_team_match where club_team_id = ? and publish = true", new Object[]{clubTeamId}, new TeamMatchMapper());
    }

    @Override
    public List<TeamMatch> getAllPublishableHomeTeamMatchesForClubTeam(int clubTeamId, int monthLimit) {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.MONTH, - monthLimit);
        return template.getJdbcOperations().query(
                "select * from t_team_match where club_team_id = ? and publish = true and start > ?", 
                new Object[]{clubTeamId, cal.getTime()}, new TeamMatchMapper());
    }


}
