package com.clubeek.model;

/**
 * Spolecny predek pro objekty modelu
 */
public class Model implements Unique {

	/** Jednoznacny identifikator */
	private int id;

	/** Vraci Jednoznacny identifikator */
	public int getId() {
		return id;
	}

	/** Nastavi Jednoznacny identifikator */
	public void setId(int id) {
		this.id = id;
	}

}
