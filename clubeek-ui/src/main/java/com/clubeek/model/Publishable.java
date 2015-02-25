package com.clubeek.model;

import java.util.Date;

/**
 * Rozhrani definujici pravidla pro publikovani nejakeho clanku
 * 
 * @author Marek Svarc
 * 
 */
public interface Publishable {

	/** Vraci nadpis */
	public String getDescription();
	
	/** Vraci nadpis */
	public String getCaption();

	/** Vraci zkraceny popis clanku */
	public String getSummary();

	/** Vraci obsah clanku */
	public String getContent();

	/** Vraci datum posledni upravy clanku */
	public Date getLastChangeDate();

	/** Vraci priznak zda jde o dulezity clanek */
	public boolean getPriority();

}
