package com.clubeek.dao.impl.springjdbctemplate;

import java.util.List;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;

import com.clubeek.dao.ClubSettingDao;
import com.clubeek.dao.impl.springjdbctemplate.mappers.ClubSettingMapper;
import com.clubeek.model.ClubSetting;

public class ClubSettingDaoImpl extends DaoImpl implements ClubSettingDao {

    @Override
    public ClubSetting getClubSettingById(int id) {
        return template.getJdbcOperations().queryForObject("select * from t_club_setting where id = ?", new Integer[] {id}, new ClubSettingMapper());
    }

    @Override
    public void updateClubSetting(ClubSetting clubSetting) {
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(clubSetting);
        template.update(""
                + "update t_club_setting set "
                + "title = :title, "
                + "comment = :comment, "
                + "logo = :logo "
                + "where id = :id", 
                source);       
    }

    @Override
    public List<ClubSetting> getAllClubSettings() {
        return template.query("select * from t_club_setting", new ClubSettingMapper());
    }

    @Override
    public void deleteClubSetting(int id) {
        template.getJdbcOperations().update("delete from t_club_setting where id = ?", new Object[] {id});        
    }

    @Override
    public void addClubSetting(ClubSetting clubSetting) {
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(clubSetting);
        template.update("insert into t_club_setting "
                + "(title, comment, logo) values "
                + "(:title, :comment, :logo)", 
                source);        
    }

}
