package com.clubeek.dao.impl.ownframework;

import java.util.List;

import com.clubeek.dao.ClubSettingDao;
import com.clubeek.dao.impl.ownframework.rep.RepClubSettings;
import com.clubeek.model.ClubSetting;

public class ClubSettingsDaoImpl implements ClubSettingDao {

    @Override
    public ClubSetting getClubSettingById(int id) {
        return RepClubSettings.select(1, null);
    }

    @Override
    public void updateClubSetting(ClubSetting clubSetting) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ClubSetting> getAllClubSettings() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteClubSetting(int id) {
        throw new UnsupportedOperationException();        
    }

    @Override
    public void addClubSetting(ClubSetting clubSetting) {
        throw new UnsupportedOperationException();                
    }

}
