package com.clubeek.model;

import java.util.Date;

/**
 * Trida zapouzdrujici data o treninku
 * 
 * @author Marek Svarc
 */
public class TeamTraining extends Model implements Event {
	/* PRIVATE */

	/** Zacatek treninku */
	private Date start = null;

	/** Konec treninku */
	private Date end = null;

	/** Misto konani treninku */
	private String place = ""; //$NON-NLS-1$

	/** Identifikator tymu pro ktery je trenink urcen */
	private int clubTeamId;
	
	/** Comment for this training. */
	private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    /** Vraci datum a cas zacatku treninku */
    @Override
    public Date getStart() {
        return start;
    }

    /** Nastavi datum a cas zacatku treninku */
    public void setStart(Date date) {
        this.start = date;
    }

    /** Vraci datum a cas konce treninku */
    @Override
    public Date getEnd() {
        return end;
    }

    /** Nastavi datum a cas konce treninku */
    public void setEnd(Date till) {
        this.end = till;
    }

    /** Vraci misto konani treninku */
    @Override
    public String getPlace() {
        return place;
    }

    /** Nastavi misto konani treninku */
    public void setPlace(String place) {
        this.place = place;
    }

    /** Vraci identifikator tymu pro ktery je trenink urcen */
    public int getClubTeamId() {
        return clubTeamId;
    }

    /** Nastavi identifikator tymu pro ktery je trenink urcen */
    public void setClubTeamId(int clubTeamId) {
        this.clubTeamId = clubTeamId;
    }

    /** Vraci true pokud trenink jiz probehnul */
    public boolean isGone() {
        Date now = new Date();
        return now.after(getStart());
    }
}
