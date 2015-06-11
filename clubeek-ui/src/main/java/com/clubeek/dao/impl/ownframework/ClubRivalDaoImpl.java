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

    @Override
    public void updateRow(ClubRival object) {
        RepClubRival.update(object);        
    }

    @Override
    public void insertRow(ClubRival object) {
        RepClubRival.insert(object);        
    }

    @Override
    public void deleteRow(int id) {
        RepClubRival.delete(id);        
    }

    @Override
    public void exchangeRows(int idA, int idB) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not supported.");
    }

}
