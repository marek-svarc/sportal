package com.clubeek.dao;

import java.util.List;

import com.clubeek.model.ClubSetting;

public interface ClubSettingDao {

    public ClubSetting getClubSettingById(int id);
    
    public void updateClubSetting(ClubSetting clubSetting); 
    
    public List<ClubSetting> getAllClubSettings();
    
    public void deleteClubSetting(int id);
    
    public void addClubSetting(ClubSetting clubSetting);
}
