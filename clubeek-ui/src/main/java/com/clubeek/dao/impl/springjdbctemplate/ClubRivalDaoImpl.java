package com.clubeek.dao.impl.springjdbctemplate;

import java.util.List;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import com.clubeek.dao.ClubRivalDao;
import com.clubeek.dao.impl.springjdbctemplate.mappers.ClubRivalMapper;
import com.clubeek.model.ClubRival;
import org.springframework.stereotype.Repository;

@Repository
public class ClubRivalDaoImpl extends DaoImpl implements ClubRivalDao {

    @Override
    public void updateRow(ClubRival object) {
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(object);
        template.update("update t_club_rival set "
                + "name = :name, "
                + "web = :web, "
                + "gps = :GPS, "
                + "street = :street, "
                + "city = :city, "
                + "code = :code, "
                + "icon = :icon "
                + "where id = :id"
                , source);    
    }

    @Override
    public void insertRow(ClubRival object) {
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(object);
        template.update("insert into t_club_rival "
                + "( name,  web,  gps,  street,  city,  code,  icon) values "
                + "(:name, :web, :GPS, :street, :city, :code, :icon)"
                , source);    
    }

    @Override
    public void deleteRow(int id) {
        template.getJdbcOperations().update("delete from t_club_rival where id = ?", new Integer[] {id});                
    }

    @Override
    public void exchangeRows(int idA, int idB) {
        throw new UnsupportedOperationException();        
    }

    @Override
    public List<ClubRival> getAllClubRivals() {
        return template.query("select * from t_club_rival", new ClubRivalMapper());
    }

    @Override
    public ClubRival getClubRivalById(int id) {
        return template.getJdbcOperations().queryForObject("select * from t_club_rival where id = ?", new Integer[] {id}, new ClubRivalMapper());
    }

    @Override
    public void deleteRows(List<ClubRival> objects) {
        SqlParameterSource[] sources = new SqlParameterSourceUtils().createBatch(objects.toArray());
        template.batchUpdate("delete from t_club_rival where id = :id", sources);        
    }

}
