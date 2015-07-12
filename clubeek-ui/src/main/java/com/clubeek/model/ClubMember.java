package com.clubeek.model;

import java.util.Date;
import java.util.List;

import com.clubeek.dao.ContactDao;
import com.clubeek.dao.impl.ownframework.ContactDaoImpl;
import com.clubeek.dao.impl.ownframework.rep.RepContact;
import com.clubeek.util.DateTime;

/**
 * Trida zapouzdrujici informace o clenech klubu
 * 
 * @author Marek Svarc
 */
public class ClubMember extends Model {
    // TODO vitfo, created on 12. 6. 2015 
    private ContactDao contactDao = new ContactDaoImpl();

	/* PRIVATE */
    
    /** Id of the club the member belongs. It cannot be null. */
    private int clubId;

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

    public int getClubId() {
        return clubId;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
    }

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
		return getBirthdate() != null ? DateTime.dateToString(getBirthdate(), DateTime.DateStyle.SHORT_DAY) : "";
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
	
	public List<Contact> getContacts() {
		if (contacts == null)
//			contacts = RepContact.selectByClubMemberId(getId(), null);
		    contacts = contactDao.getContactsByClubMemberId(getId());
		
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
        
    public String getMemberFullName() {
        return name + " " + surname;
    }
}
