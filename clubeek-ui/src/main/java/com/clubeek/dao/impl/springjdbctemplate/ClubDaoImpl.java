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
        template.getJdbcOperations().update(""
                + "update t_club set "
                + "licence_type = ?, "
                + "title = ?, "
                + "comment = ?, "
                + "logo = ? "
                + "where id = ?", 
                new Object[] {
                     club.getLicenceType().ordinal(),
                     club.getTitle(),
                     club.getComment(),
                     club.getLogo(),
                     club.getId()
                });       
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
        template.getJdbcOperations().update("insert into t_club "
                + "("
                + "licence_type, "
                + "title, "
                + "comment, "
                + "logo) values (?, ?, ?, ?)", 
                new Object[] {
                        club.getLicenceType().ordinal(),
                        club.getTitle(),
                        club.getComment(),
                        club.getLogo()
                });        
    }

}
