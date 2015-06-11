package com.clubeek.dao.impl.ownframework;

import java.util.List;

import com.clubeek.dao.ClubRivalDao;
import com.clubeek.dao.impl.ownframework.rep.RepClubRival;
import com.clubeek.model.ClubRival;

public class ClubRivalDaoImpl implements ClubRivalDao {

    @Override
    public List<ClubRival> getAllClubRivals() {
        return RepClubRival.selectAll(null);
    }

    @Override
    public ClubRival getClubRivalById(int id) {
        return RepClubRival.selectById(id, null);
    }

    @Override
    public RepClubRival getInstance() {
        return RepClubRival.getInstance();
    }

}
