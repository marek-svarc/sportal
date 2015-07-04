package com.clubeek.dao;

import java.util.List;

import com.clubeek.model.Club;

public interface ClubDao {

    public Club getClubById(int id);
    
    public void updateClub(Club club); 
    
    public List<Club> getAllClubs();
    
    public void deleteClub(int id);
    
    public void addClub(Club club);
}
