package com.clubeek.dao;

import java.util.List;

import com.clubeek.dao.impl.ownframework.rep.RepClubRival;
import com.clubeek.model.Article;
import com.clubeek.model.ClubRival;

public interface ClubRivalDao extends Dao<ClubRival> {

    public List<ClubRival> getAllClubRivals();
    
    public ClubRival getClubRivalById(int id);
    
    // TODO vitfo, created on 11. 6. 2015 - remove
    public RepClubRival getInstance();
}
