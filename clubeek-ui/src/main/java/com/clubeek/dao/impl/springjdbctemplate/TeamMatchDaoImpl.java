package com.clubeek.dao.impl.springjdbctemplate;

import java.util.List;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import com.clubeek.dao.TeamMatchDao;
import com.clubeek.dao.impl.springjdbctemplate.mappers.TeamMatchMapper;
import com.clubeek.model.TeamMatch;

public class TeamMatchDaoImpl extends DaoImpl implements TeamMatchDao {

    @Override
    public void updateRow(TeamMatch object) {
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(object);
        template.update("update t_team_match set "
                + "start = :start, "
                + "score_pos = :scorePos, "
                + "score_neg = :scoreNeg, "
                + "home_court = :homeCourt, "
                + "comment = :comment, "
                + "publish = :publish, "
                + "club_team_id = :clubTeamId, "
                + "club_rival_id = :clubRivalId, "
                + "club_rival_comment = :clubRivalComment "
                + "where id = :id", source);
        
    }

    @Override
    public void insertRow(TeamMatch object) {
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(object);
        template.update("insert into t_team_match "
                + "(start, score_pos, score_neg, home_court, comment, publish, club_team_id, club_rival_id, club_rival_comment) values "
                + "(:start, :scorePos, :scoreNeg, :homeCourt, :comment, :publish, :clubTeamId, :clubRivalId, :clubRivalComment)", source);        
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
}
