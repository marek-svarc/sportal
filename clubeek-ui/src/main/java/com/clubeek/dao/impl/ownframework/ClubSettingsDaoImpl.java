package com.clubeek.dao.impl.ownframework;

import com.clubeek.dao.ClubSettingDao;
import com.clubeek.dao.impl.ownframework.rep.RepClubSettings;
import com.clubeek.model.ClubSettings;

public class ClubSettingsDaoImpl implements ClubSettingDao {

    @Override
    public ClubSettings getClubSettingById(int id) {
        return RepClubSettings.select(1, null);
    }

}
