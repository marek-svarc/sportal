package com.clubeek.dao.impl.springjdbctemplate;

import java.util.List;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;

import com.clubeek.dao.ClubDao;
import com.clubeek.dao.impl.springjdbctemplate.mappers.ClubMapper;
import com.clubeek.model.Club;
import org.springframework.stereotype.Repository;

@Repository
public class ClubDaoImpl extends DaoImpl implements ClubDao {

    @Override
    public Club getClubById(int id) {
        return template.getJdbcOperations().queryForObject("select * from t_club where id = ?", new Integer[] {id}, new ClubMapper());
    }

    @Override
    public void updateClub(Club club) {
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(club);
        template.update(""
                + "update t_club set "
                + "title = :title, "
                + "comment = :comment, "
                + "logo = :logo "
                + "where id = :id", 
                source);       
    }

    @Override
    public List<Club> getAllClubs() {
        return template.query("select * from t_club", new ClubMapper());
    }

    @Override
    public void deleteClub(int id) {
        template.getJdbcOperations().update("delete from t_club where id = ?", new Object[] {id});        
    }

    @Override
    public void addClub(Club club) {
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(club);
        template.update("insert into t_club "
                + "(title, comment, logo) values "
                + "(:title, :comment, :logo)", 
                source);        
    }

}
