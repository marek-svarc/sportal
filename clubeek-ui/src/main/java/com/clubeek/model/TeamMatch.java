package com.clubeek.model;

import java.util.Date;

import com.clubeek.enums.MatchType;

/**
 * Trida zapouzdrujici informace o zapase
 * 
 * @author Marek Svarc
 * 
 */
public class TeamMatch extends Model implements IEvent, Publishable {

    /* PRIVATE */
    
    private boolean isHomeMatch;
    
    private MatchType matchType;
    
    /** Start of the match. */
    private Date start = null;
    
    /** Match's comment. */
    private String comment = ""; //$NON-NLS-1$
    
    /** Comment to the rival. */
    private String clubRivalComment = ""; //$NON-NLS-1$
    
    /** Whether the result should be published. */
    private boolean publish = false;
    
    private Integer scoreA;
    private Integer scoreB;
    private String scoreDetail;
    
    private Integer seasonId;
    
    /** Id of the team. */
    private int clubTeamId;
    
    /** Id of the rival. */
    private Integer clubRivalId = null;
    
    
    
    /** Vlastnosti tymu hrajici zapas */
    private ClubTeam clubTeam;

    /** Vlastnosti soupere */
    private ClubRival clubRival = null;

	@Override
	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	@Override
	public Date getEnd() {
		return null;
	}

	@Override
	public String getPlace() {
		return null;
	}

	/** Vraci identifikator tymu */
	public int getClubTeamId() {
		return clubTeam != null ? clubTeam.getId() : clubTeamId;
	}

	/** Nastvi identifikator tymu */
	public void setClubTeamId(int clubTeamId) {
		this.clubTeamId = clubTeamId;
	}

	/** Vraci instanci tymu hrajici zapas */
	public ClubTeam getClubTeam() {
		return clubTeam;
	}

	/** Nastavi instanci tymu hrajici zapas */
	public void setClubTeam(ClubTeam clubTeam) {
		this.clubTeam = clubTeam;
	}

	/** Vraci identifikator soupere */
	public Integer getClubRivalId() {
		return clubRivalId;
	}

	/** Nastavi identifikator soupere */
	public void setClubRivalId(Integer clubRivalId) {
		this.clubRivalId = clubRivalId;
	}

	/** Vraci vlastnosti soupere */
	public ClubRival getClubRival() {
		return clubRival;
	}

	/** Nastavi vlastnosti soupere */
	public void setClubRival(ClubRival clubRival) {
		this.clubRival = clubRival;
	}

	/** Nastavi doplnek k popisu soupere */
	public void setClubRivalComment(String comment) {
		this.clubRivalComment = comment;
	}

	/** Vraci doplnek k popisu soupere */
	public String getClubRivalComment() {
		return clubRivalComment;
	}

	/** Sestavi textovy popis soupere */
	public String getClubRivalTitle() {
		if (getClubRival() != null) {
			String title = getClubRival().getName();
			if ((getClubRivalComment() != null) && !getClubRivalComment().isEmpty())
				title += " " + getClubRivalComment(); //$NON-NLS-1$
			return title;
		} else {
			return getClubRivalComment();
		}
	}


	/** Nastavi priznak zda se ma vysledek publikovat */
	public void setPublish(boolean publish) {
		this.publish = publish;
	}

	/** Vraci priznak zda se ma vysledek publikovat */
	public boolean getPublish() {
		return this.publish;
	}

	/** Nastavi komentar k zapasu */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/** Vraci komentar k zapasu */
	public String getComment() {
		return comment;
	}
	
	public boolean isHomeMatch() {
        return isHomeMatch;
    }

    public void setHomeMatch(boolean isHomeMatch) {
        this.isHomeMatch = isHomeMatch;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    public Integer getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(Integer seasonId) {
        this.seasonId = seasonId;
    }
    
    public Integer getScoreA() {
        return scoreA;
    }

    public void setScoreA(Integer scoreA) {
        this.scoreA = scoreA;
    }

    public Integer getScoreB() {
        return scoreB;
    }

    public void setScoreB(Integer scoreB) {
        this.scoreB = scoreB;
    }

    public String getScoreDetail() {
        return scoreDetail;
    }

    public void setScoreDetail(String scoreDetail) {
        this.scoreDetail = scoreDetail;
    }
    
    // TODO vitfo, created on 14. 7. 2015 
    public String getScoreAsString() {
        return scoreA + " : " + scoreB;
    }

    // interface Publishable
    @Override
	public String getCaption() {
		if ((getClubTeam() != null) && (getClubRival() != null)) {
			String teamName = getClubTeam().getName();
			String rivalName = getClubRivalTitle();
			return "TODO - caption of the team match";
//			return String.format("%s - %s   %s", getHomeCourt() ? teamName : rivalName, getHomeCourt() ? rivalName : teamName, //$NON-NLS-1$
//					getScoreAsStr());
		} else
			return null;
	}

	@Override
	public String getSummary() {
		return getComment();
	}

	@Override
	public String getContent() {
		return null;
	}

	@Override
	public Date getLastChangeDate() {
		return getStart();
	}

	@Override
	public boolean getPriority() {
		return false;
	}

	@Override
	public String getDescription() {
		return Messages.getString("competitiveMatch"); //$NON-NLS-1$
	}
}
