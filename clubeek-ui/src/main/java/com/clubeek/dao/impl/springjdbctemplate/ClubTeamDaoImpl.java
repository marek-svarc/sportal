package com.clubeek.dao.impl.springjdbctemplate;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import com.clubeek.dao.ClubTeamDao;
import com.clubeek.dao.impl.springjdbctemplate.mappers.ClubTeamMapper;
import com.clubeek.model.ClubTeam;

/**
 * Implementation of @link {@link ClubTeamDao} interface.
 * 
 * @author vitfo
 */
@Repository
public class ClubTeamDaoImpl extends DaoImpl implements ClubTeamDao {

    @Override
    public void updateRow(ClubTeam object) {
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(object);
        template.update("update t_club_team set "
                + "name = :name, "
                + "active = :active, "
                + "sorting = :sorting, "
                + "category_id = :categoryId "
                + "where id = :id"
                , source);        
    }

    @Override
    public void insertRow(ClubTeam object) {
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(object);
        template.update("insert into t_club_team "
                + "( name,  active,  sorting,  category_id) values "
                + "(:name, :active, :sorting, :categoryId)"
                , source);
    }

    @Override
    public void deleteRow(int id) {
        template.getJdbcOperations().update("delete from t_club_team where id = ?", new Integer[] {id});        
    }

    @Override
    public void exchangeRows(int idA, int idB) {
        throw new UnsupportedOperationException();        
    }

    @Override
    public ClubTeam getClubTeamById(int id) {
        return template.getJdbcOperations().queryForObject("select * from t_club_team where id = ?", new Integer[] {id}, new ClubTeamMapper());
    }

    @Override
    public List<ClubTeam> getActiveClubTeams() {
        return template.query("select * from t_club_team where active = true", new ClubTeamMapper());
    }

    @Override
    public List<ClubTeam> getAllClubTeams() {
        return template.query("select * from t_club_team", new ClubTeamMapper());
    }

    @Override
    public void deleteRows(List<ClubTeam> objects) {
        SqlParameterSource[] sources = new SqlParameterSourceUtils().createBatch(objects.toArray());
        template.batchUpdate("delete from t_club_team where id = :id", sources);   
        
    }

}
