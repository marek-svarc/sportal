package com.clubeek.dao;

import java.util.List;

import com.clubeek.model.ClubUrl;

public interface ClubUrlDao {

    /**
     * Gets all urls that belong the club.
     * 
     * @param clubId - id of the club
     * @return all urls that belongs the club
     */
    public List<ClubUrl> getAllClubUrls(int clubId);
    
    /** 
     * Gets id of the club the url belongs to.
     * 
     * @param url - url of the club
     * @return id of the club the url belongs to
     */
    public Integer getClubId(String url);
    
    public void updateClubUrl(ClubUrl clubUrl);
    
    public List<ClubUrl> getAllClubUrls();
    
    public void addClubUrl(ClubUrl clubUrl);
    
    public void deleteClubUrl(int id);
}
