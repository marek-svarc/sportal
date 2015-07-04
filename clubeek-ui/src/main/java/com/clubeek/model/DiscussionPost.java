package com.clubeek.model;

import java.util.Date;

public class DiscussionPost {

    private int id;
    private Date creationTime;
    private String comment;
    private int referencedObjectId;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Date getCreationTime() {
        return creationTime;
    }
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public int getReferencedObjectId() {
        return referencedObjectId;
    }
    public void setReferencedObjectId(int referencedObjectId) {
        this.referencedObjectId = referencedObjectId;
    }
}
