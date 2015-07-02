package com.clubeek.model;

/**
 * Trida zapouzdrujici data klubu
 * 
 * @author Marek Svarc
 */
public class ClubRival extends Model {
    
    /** Nazev klubu */
    private String name = ""; //$NON-NLS-1$

    /** internetove stranky klubu */
    private String web = ""; //$NON-NLS-1$

    /** GPS souradnice klubu */
    private String GPS = ""; //$NON-NLS-1$

    /** Ulice */
    private String street = ""; //$NON-NLS-1$

    /** mesto */
    private String city = ""; //$NON-NLS-1$

    /** Postovni smerovaci cislo */
    private String code = ""; //$NON-NLS-1$

    /** Znak klubu (64px x 64px) */
    private byte[] icon = null;

	/** Vraci jmeno klubu */
	public String getName() {
		return name;
	}

	/** Nastavi jmeno klubu */
	public void setName(String name) {
		this.name = name;
	}

	/** Nastavi internetove stranky klubu */
	public void setWeb(String web) {
		this.web = web;
	}
	
	/** Vraci internetove stranky klubu */
	public String getWeb() {
		return web;
	}

	/** Vraci GPS souradnice klubu */
	public String getGPS() {
		return GPS;
	}

	/** Nastavi GPS souradnice klubu */
	public void setGPS(String gps) {
		this.GPS = gps;
	}

	/** Vraci ulici */
	public String getStreet() {
		return street;
	}

	/** Nastavi ulici */
	public void setStreet(String street) {
		this.street = street;
	}

	/** Vraci mesto kde se klub nal�z� */
	public String getCity() {
		return city;
	}

	/** Nastavi mesto kde se klub nal�z� */
	public void setCity(String city) {
		this.city = city;
	}

	/** Vraci smerovaci cislo klubu */
	public String getCode() {
		return code;
	}

	/** Nastavi smerovaci cislo klubu */
	public void setCode(String code) {
		this.code = code;
	}

	/** Vraci znak klubu */
	public byte[] getIcon() {
		return icon;
	}

	/** Nastavi znak klubu */
	public void setIcon(byte[] icon) {
		this.icon = icon;
	}
}
