package com.clubeek.dao.impl.ownframework;

import java.util.List;

import com.clubeek.dao.ClubDao;
import com.clubeek.dao.impl.ownframework.rep.RepClubSettings;
import com.clubeek.model.Club;

public class ClubSettingsDaoImpl implements ClubDao {

    @Override
    public Club getClubById(int id) {
        return RepClubSettings.select(1, null);
    }

    @Override
    public void updateClub(Club clubSetting) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Club> getAllClubs() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteClub(int id) {
        throw new UnsupportedOperationException();        
    }

    @Override
    public void addClub(Club clubSetting) {
        throw new UnsupportedOperationException();                
    }

}
