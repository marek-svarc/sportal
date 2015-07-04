package com.clubeek.model;

public class Club extends Model {

	/* PRIVATE */

	/** Nazev klubu */
	private String title;

	/** Podrobny popis klubu */
	private String comment;

	/** Logo klubu */
	private byte[] logo = null;

	/* PUBLIC */

	/** Vraci nazev klubu */
	public String getTitle() {
		return title;
	}

	/** Nastavi nazev klubu */
	public void setTitle(String title) {
		this.title = title;
	}

	/** Vraci podrobny popis klubu */
	public String getComment() {
		return comment;
	}

	/** Nastavi podrobny popis klubu */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/** Vraci logo klubu */
	public byte[] getLogo() {
		return logo;
	}

	/** Nastavi logo klubu */
	public void setLogo(byte[] logo) {
		this.logo = logo;
	}

}
