package com.clubeek.model;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.clubeek.db.RepContact;

/**
 * Trida zapouzdrujici informace o clenech klubu
 * 
 * @author Marek Svarc
 */
public class ClubMember extends Model {

	/* PRIVATE */

	/** Rodne cislo */
	private String idPersonal = ""; //$NON-NLS-1$

	/** Registracni cislo */
	private String idRegistration = ""; //$NON-NLS-1$

	/** Jmeno */
	private String name = ""; //$NON-NLS-1$

	/** Prijmeni */
	private String surname = ""; //$NON-NLS-1$

	/** Datum narozeni */
	private Date birthdate = null;

	/** Ulice */
	private String street = ""; //$NON-NLS-1$

	/** Mesto */
	private String city = ""; //$NON-NLS-1$

	/** Postovni smerovaci cislo */
	private String code = ""; //$NON-NLS-1$
	
	/** Fotografie */
	private byte[] photo = null;

	/** Seznam kontaktu asociovanych se clenem  */ 
	private List<Contact> contacts = null;

	/* PUBLIC */
	
	@Override
	public String toString() {
		return getName() + " " + getSurname() + ", "+getBirthdateAsString();
	}
	
	// Vlastnosti

	public String getIdPersonal() {
		return idPersonal;
	}

	public void setIdPersonal(String idPersonal) {
		this.idPersonal = idPersonal;
	}

	public String getIdRegistration() {
		return idRegistration;
	}

	public void setIdRegistration(String idRegistration) {
		this.idRegistration = idRegistration;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getBirthdateAsString() {
		return getBirthdate() != null ? getBirthdate().toString() : ""; //$NON-NLS-1$
	}
	
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public List<Contact> getContacts() throws SQLException {
		if (contacts == null)
			contacts = RepContact.selectByClubMemberId(getId(), null);
		
		return contacts;
	}
	
	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	
	public byte[] getPhoto() {
		return photo;
	}
	
	public void setPhoto(byte[] image) {
		this.photo = image;
	}
}
