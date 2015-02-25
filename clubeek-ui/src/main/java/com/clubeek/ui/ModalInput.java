package com.clubeek.ui;

import com.vaadin.ui.Component;

/**
 * Rozhrani urcujici vymenu dat pri modalni editaci
 * 
 * @author Marek Svarc
 * 
 * @param <T>
 *            Typ editovanych dat
 */
public interface ModalInput<T> extends Component {

	/**
	 * Metoda pro inicializaci komponent externimy daty
	 * 
	 * @param data
	 *            externi data
	 */
	public void dataToInput(T data);

	/**
	 * Metoda pro predani hodnot z editovatelnych komponent do externich dat
	 * 
	 * @param data
	 *            externi data
	 */
	public void inputToData(T data);

}
