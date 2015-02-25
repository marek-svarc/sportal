package com.clubeek.model;

/**
 * Trida umoznujici trideni polozek do kategorii
 * 
 * @author Marek Svarc
 */
public class Category  extends Model{

	// Hodnoty vlastnosti

	/** Popis kategorie */
	private String description = ""; //$NON-NLS-1$

	/** priznak zda je kategorie aktivni */
	private Boolean active = true;

	/** priorita zobrazeni (vyssi cislo => vetsi priorita) */
	private int sorting = 0;

	// Konstruktor

	/** Vytvori prazdnou instanci tridy "Category" */
	public Category() {
	}

	// Vlastnosti

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
	public int getSorting() {
		return sorting;
	}
	
	public void setSorting(int sorting) {
		this.sorting = sorting;
	}

	@Override
	public String toString() {
		return description;
	}
}
