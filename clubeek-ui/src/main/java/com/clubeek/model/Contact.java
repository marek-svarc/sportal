package com.clubeek.model;

import com.clubeek.enums.ContactType;
import com.clubeek.enums.NotificationType;

public class Contact extends Model implements Cloneable {

	/* PRIVATE */

	/** Typ kontaktu */
	private ContactType contactType;

	/** Popis kontaktu */
	private String description;

	/** Hodnota kontaktu */
	private String contact;

	/** Zpusob zasilani zprav */
	private NotificationType notifictionType;

	/** identifikator asociovaneho clena klubu */
	private int clubMemberId;

	/* PUBLIC */

	// Datove typy

	/** Popisuje typ kontaktu */
//	public enum ContactType {
//		EMAIL, PHONE;
//
//		public String toString() {
//			switch (ordinal()) {
//			case 0:
//				return Messages.getString("email"); //$NON-NLS-1$
//			case 1:
//				return Messages.getString("phone"); //$NON-NLS-1$
//			default:
//				return ""; //$NON-NLS-1$
//			}
//		};
//	}

//	/** Zpusob oznamovani zprav */
//	public enum NotificationType {
//		NEVER, ALWAYS, ONLY_IMPORTANT;
//
//		public String toString() {
//			switch (ordinal()) {
//			case 0:
//				return Messages.getString("no"); //$NON-NLS-1$
//			case 1:
//				return Messages.getString("yesAlways"); //$NON-NLS-1$
//			case 2:
//				return Messages.getString("yesSignificantOnly"); //$NON-NLS-1$
//			default:
//				return ""; //$NON-NLS-1$
//			}
//		};
//	}

	// Vlastnosti

	public ContactType getContactType() {
		return contactType;
	}

	public void setContactType(ContactType contactType) {
		this.contactType = contactType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public NotificationType getNotificationType() {
		return notifictionType;
	}

	public void setNotificationType(NotificationType notificationType) {
		this.notifictionType = notificationType;
	}

	public int getClubMemberId() {
		return clubMemberId;
	}

	public void setClubMemberId(int clubMemberId) {
		this.clubMemberId = clubMemberId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Contact) {
			Contact with = (Contact) obj;
			return (getId() == with.getId()) && (contactType == with.contactType) && description.equals(with.description)
					&& (contact.equals(with.contact)) && (notifictionType == with.notifictionType) && (clubMemberId == with.clubMemberId);
		} else {
			return false;
		}
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
