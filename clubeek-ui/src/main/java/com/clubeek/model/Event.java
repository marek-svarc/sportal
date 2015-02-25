package com.clubeek.model;

import java.util.Date;

/**
 * Rozhrani poskytujici zakladni informace o poradane akci
 * 
 * @author Marek Svarc
 * 
 */
public interface Event {

	/**
	 * Zacatek akce
	 * 
	 * @return cas zacatku akce
	 */
	public Date getStart();

	/**
	 * Konec akce
	 * 
	 * @return cas ukonceni akce
	 */
	public Date getEnd();

	/**
	 * Misto konani akce
	 * 
	 * @return popis mista konani akce
	 */
	public String getPlace();
}
