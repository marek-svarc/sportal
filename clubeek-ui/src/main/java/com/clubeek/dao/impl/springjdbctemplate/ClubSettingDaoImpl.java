package com.clubeek.dao.impl.springjdbctemplate;

import com.clubeek.dao.ClubSettingDao;
import com.clubeek.dao.impl.springjdbctemplate.mappers.ClubSettingMapper;
import com.clubeek.model.ClubSettings;

public class ClubSettingDaoImpl extends DaoImpl implements ClubSettingDao {

    @Override
    public ClubSettings getClubSettingById(int id) {
        return template.getJdbcOperations().queryForObject("select * from t_club_setting where id = ?", new Integer[] {id}, new ClubSettingMapper());
    }

}
