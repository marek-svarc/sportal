package com.clubeek.model;

import java.util.Date;

/**
 * Trida zapouzdrujici informace o zapase
 * 
 * @author Marek Svarc
 * 
 */
public class TeamMatch extends Model implements Event, Publishable {

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
	public int getClubRivalId() {
		return clubRival != null ? clubRival.getId() : clubRivalId;
	}

	/** Nastavi identifikator soupere */
	public void setClubRivalId(int clubRivalId) {
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

	/** Nastavi priznak zda se jedna o domaci zapas */
	public void setHomeCourt(boolean homeCourt) {
		this.homeCourt = homeCourt;
	}

	/** Vraci priznak zda se jedna o domaci zapas */
	public boolean getHomeCourt() {
		return homeCourt;
	}

	/** Nastavi pocet bodu */
	public void setScorePos(int scorePos) {
		this.scorePos = scorePos;
	}

	/** Vraci pocet bodu */
	public int getScorePos() {
		return scorePos;
	}

	/** Nastavi pocet bodu soupere */
	public void setScoreNeg(int scoreNeg) {
		this.scoreNeg = scoreNeg;
	}

	/** Vraci pocet bodu soupere */
	public int getScoreNeg() {
		return scoreNeg;
	}

	/** Nastavi pocet bodu domaciho muzstva */
	public void setScoreHomeTeam(int score) {
		if (getHomeCourt())
			this.setScorePos(score);
		else
			this.setScoreNeg(score);
	}

	/** Vraci pocet bodu domaciho muzstva */
	public int getScoreHomeTeam() {
		return getHomeCourt() ? getScorePos() : getScoreNeg();
	}

	/** Nastavi pocet bodu hostujiciho muzstva */
	public void setScoreVisitingTeam(int score) {
		if (getHomeCourt())
			this.setScoreNeg(score);
		else
			this.setScorePos(score);
	}

	/** Vraci pocet bodu hostujicicho muzstva */
	public int getScoreVisitingTeam() {
		return getHomeCourt() ? getScoreNeg() : getScorePos();
	}

	/** Prevede skore zapasu na text */
	public String getScoreAsStr() {
		if ((getScoreNeg() >= 0) && (getScorePos() >= 0)) {
			String pos = Integer.toString(getScorePos());
			String neg = Integer.toString(getScoreNeg());
			return getHomeCourt() ? String.format("%s : %s", pos, neg) : String.format("%s : %s", neg, pos); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return ""; //$NON-NLS-1$
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

	// interface Publishable

	@Override
	public String getCaption() {
		if ((getClubTeam() != null) && (getClubRival() != null)) {
			String teamName = getClubTeam().getName();
			String rivalName = getClubRivalTitle();
			return String.format("%s - %s   %s", getHomeCourt() ? teamName : rivalName, getHomeCourt() ? rivalName : teamName, //$NON-NLS-1$
					getScoreAsStr());
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

	/* PRIVATE */

	/** Zacatek zapasu */
	private Date start = null;

	/** Identifikator tymu hrajici zapas */
	private int clubTeamId;

	/** Vlastnosti tymu hrajici zapas */
	private ClubTeam clubTeam;

	/** Identifikator soupere */
	private int clubRivalId = 0;

	/** Vlastnosti soupere */
	private ClubRival clubRival = null;

	/** Doplnek k popisu soupere */
	private String clubRivalComment = ""; //$NON-NLS-1$

	/** Zapas na domacim hristi */
	private boolean homeCourt = true;

	/** Priznak zda se ma vysledek publikovat */
	private boolean publish = false;

	/** Komentar k zapasu */
	private String comment = ""; //$NON-NLS-1$

	/** Pocet bodu */
	private int scorePos = -1;

	/** Pocet bodu soupere */
	private int scoreNeg = -1;
}
