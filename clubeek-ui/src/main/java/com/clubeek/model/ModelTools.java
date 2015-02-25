package com.clubeek.model;

import java.util.List;

public class ModelTools {

	/**
	 * Vyhledava objekt podle Id
	 * 
	 * @param list
	 *            prohledavany seznam
	 * @param id
	 *            pozadovane Id
	 * @return objekt jehoz Id se rovn pozadovanemu
	 */
	public static <T extends Unique> T listFindById(Iterable<T> list, int id) {
		for (T item : list)
			if (item.getId() == id)
				return item;
		return null;
	}

	/**
	 * Odstrani duplicitni objekty
	 * 
	 * @param list
	 *            seznam ze ktereho se duplicitni objekty odstranuji
	 * @param duplicates
	 *            seznam ve kterem se duplicitni objekty vyhledavaji
	 */
	public static <T1 extends Unique, T2 extends Unique> void listRemoveDuplicates(List<T1> list, Iterable<T2> duplicates) {
		for (int i = list.size() - 1; i >= 0; --i) {
			if (listFindById(duplicates, list.get(i).getId()) != null)
				list.remove(i);
		}
	}

	/**
	 * Sestavi text obsahujici polozku 'getId()' vsech prvku seznamu, hodnot
	 * jsou oddeleny ", "
	 * 
	 * @param list
	 *            prohledavany seznam
	 * @return objekt jehoz Id se rovna pozadovanemu
	 */
	public static <T extends Unique> String listToString(Iterable<T> list) {
		StringBuilder text = new StringBuilder();

		for (T item : list) {
			if (text.length() > 0)
				text.append(", "); //$NON-NLS-1$
			text.append(item.getId());
		}

		return text.toString();
	}
	
}
