package com.clubeek.db;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Rozhrani definujici operace nad jednou tabulkou databaze
 * 
 * @param <T>
 *            Trida zapouzdrujici data z databaze
 */
public interface Repository<T> {

	/**
	 * Modifikuje radek tabulky
	 * 
	 */
	void updateRow(T value);

	/**
	 * Vlozi a inicializuje radek v tabulce
	 * 
	 */
	void insertRow(T value);

	/**
	 * Odmaze radek z tabulky
	 * 
	 * @param id
	 *            index radky ktera ma byt vymazana s tabulky
	 */
	void deleteRow(int id);

	/**
	 * Vysledek SQL prikazu SELECT zap�e do dat
	 * 
	 * @param result
	 *            Vysledek SQL prikazu
	 * @param resultsColumnId
	 *            index sloupce tabulky, ze ktereho se mel nacist Vysledek
	 * @param data
	 *            data do kterych se zapisuje Vysledek SELECTu
	 * @param dataColumnId
	 *            identifiktor datoveho pole, kam se zapise vysledek
	 */
	void readValue(ResultSet result, int resultsColumnId, T data, Object dataColumnId);
}
