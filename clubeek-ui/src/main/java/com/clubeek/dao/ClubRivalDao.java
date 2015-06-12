package com.clubeek.dao;

import java.util.List;

import com.clubeek.model.ClubRival;

public interface ClubRivalDao extends Dao<ClubRival> {

    public List<ClubRival> getAllClubRivals();
    
    public ClubRival getClubRivalById(int id);
}
