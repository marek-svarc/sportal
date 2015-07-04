package com.clubeek.model;

import java.util.Date;

import com.clubeek.enums.OwnerType;

public class Event extends Model {

    private Date start;
    private Date finish;
    private String caption;
    private String place;
    private String description;
    // TODO vitfo, created on 30. 6. 2015 - change name of variable
    private Boolean signParticipation;
    private OwnerType ownerType;
    private Integer clubTeamId;
    private Integer categoryId;
    
    public Date getStart() {
        return start;
    }
    public void setStart(Date start) {
        this.start = start;
    }
    public Date getFinish() {
        return finish;
    }
    public void setFinish(Date finish) {
        this.finish = finish;
    }
    public String getCaption() {
        return caption;
    }
    public void setCaption(String caption) {
        this.caption = caption;
    }
    public String getPlace() {
        return place;
    }
    public void setPlace(String place) {
        this.place = place;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Boolean getSignParticipation() {
        return signParticipation;
    }
    public void setSignParticipation(Boolean signParticipation) {
        this.signParticipation = signParticipation;
    }
    public Integer getClubTeamId() {
        return clubTeamId;
    }
    public void setClubTeamId(Integer clubTeamId) {
        this.clubTeamId = clubTeamId;
    }
    public Integer getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    public OwnerType getOwnerType() {
        return ownerType;
    }
    public void setOwnerType(OwnerType ownerType) {
        this.ownerType = ownerType;
    }
}
