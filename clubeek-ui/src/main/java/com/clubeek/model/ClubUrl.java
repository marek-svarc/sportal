package com.clubeek.model;

public class ClubUrl extends Model {

    /** String that represents url of the club. */
    private String url;
    
    /** Id of the club the url belongs. */
    private int clubId;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getClubId() {
        return clubId;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
    }
}
