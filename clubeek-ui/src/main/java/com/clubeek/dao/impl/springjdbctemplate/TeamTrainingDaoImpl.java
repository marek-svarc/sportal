package com.clubeek.dao.impl.springjdbctemplate;

import java.util.List;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;

import com.clubeek.dao.TeamTrainingDao;
import com.clubeek.dao.impl.springjdbctemplate.mappers.TeamTrainingMapper;
import com.clubeek.model.TeamTraining;
import org.springframework.stereotype.Repository;

@Repository
public class TeamTrainingDaoImpl extends DaoImpl implements TeamTrainingDao {

    @Override
    public void updateRow(TeamTraining object) {
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(object);
        template.update("update t_team_training set "
                + "start = :start, "
                + "finish = :end, "
                + "place = :place, "
                + "comment = :comment "
                + "where id = :id", source);   
    }

    @Override
    public void insertRow(TeamTraining object) {
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(object);
        template.update("insert into t_team_training "
                + "(start, finish, place, comment, club_team_id) values "
                + "(:start, :end, :place, :comment, :clubTeamId)", source);        
    }

    @Override
    public void deleteRow(int id) {
        template.getJdbcOperations().update("delete from t_team_training where id = ?", new Object[] {id});        
    }

    @Override
    public void exchangeRows(int idA, int idB) {
        throw new UnsupportedOperationException();        
    }

    @Override
    public List<TeamTraining> getAllTeamTrainings() {
        return template.query("select * from t_team_training", new TeamTrainingMapper());
    }

}
