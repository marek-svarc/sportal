package com.clubeek.model;

import java.util.Date;

import com.clubeek.enums.LocationType;
import com.clubeek.enums.OwnerType;

/**
 * Trida obsahujici informace o jednom clanku.
 * 
 * @author Marek Svarc
 * 
 */
public class Article extends Model implements Publishable {

	// Hodnoty vlastnosti

	/** Typ umisteni clanku na jedne strance */
	private LocationType locationType = LocationType.NEWS;

	/** priznak zda ma clanek vysokou prioritu */
	private Boolean priority = false;

	/** Nadpis clanku */
	private String caption = ""; //$NON-NLS-1$

	/** Zkraceny popis clanku */
	private String summary = ""; //$NON-NLS-1$

	/** Obsah clanku */
	private String content = ""; //$NON-NLS-1$

	/** Datum vytvoreni/posledni zmeny clanku */
	private Date creationDate = null;

	/** Datum znepristupneni clanku */
	private Date expirationDate = null;

	/** Definuje umisteni na celem webu */
	private OwnerType ownerType = OwnerType.CLUB_ALL;

	/** Index kategorie pro kterou je clanek urcen */
	private Integer categoryId;

	/** Index tymu pro ktery je clanek urcen */
	private Integer clubTeamId;

	// Konstruktor

	/** Vytvori prazdnou instanci tridy "Category" */
	public Article() {
	}

	/** Nastavi typ umisteni clanku na webu */
	public void setOwnerType(OwnerType ownerType) {
		this.ownerType = ownerType;
	}

	/** Vraci typ umisteni clanku na webu */
	public OwnerType getOwnerType() {
		return ownerType;
	}

	/** Vraci typ umisteni clanku na jedne strance */
	public LocationType getLocationType() {
		return locationType;
	}

	/** Nastavi typ umisteni clanku na jedne strance */
	public void setLocationType(LocationType locationType) {
		this.locationType = locationType;
	}

	/** Vraci priznak zda ma clanek vysokou prioritu */
	public boolean getPriority() {
		return priority;
	}

	/** Nastavi priznak zda ma clanek vysokou prioritu */
	public void setPriority(Boolean priority) {
		this.priority = priority;
	}

	/** Vraci nadpis clanku */
	public String getCaption() {
		return caption;
	}

	/** Nastavi nadpis clanku */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/** Vraci zkraceny popis clanku */
	public String getSummary() {
		return summary;
	}

	/** Nastavi zkraceny popis clanku */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/** Vraci obsah clanku */
	public String getContent() {
		return content;
	}

	/** Nastavi obsah clanku */
	public void setContent(String content) {
		this.content = content;
	}

	/** Vraci datum a cas vytvoreni/posledni zmeny clanku */
	public Date getCreationDate() {
		return creationDate;
	}

	/** Nastavi datum a �as vytvoreni/posledn� zm�ny clanku */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/** Vraci datum znepristupneni clanku */
	public Date getExpirationDate() {
		return expirationDate;
	}

	/** Nastavi datum znepristupneni clanku */
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	/** Vraci index kategorie pro kterou je clanek urcen */
	public Integer getCategoryId() {
		return categoryId;
	}

	/** Nastavi index kategorie pro kterou je clanek urcen */
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	/** Vraci index tymu pro ktery je clanek urcen */
	public Integer getClubTeamId() {
		return clubTeamId;
	}

	/** Nastavi index tymu pro ktery je clanek urcen */
	public void setClubTeamId(Integer clubTeamId) {
		this.clubTeamId = clubTeamId;
	}

	// interface Publishable

	@Override
	public Date getLastChangeDate() {
		return getCreationDate();
	}

	@Override
	public String getDescription() {
		return Messages.getString("article"); //$NON-NLS-1$
	}

}
