package com.clubeek.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

import com.clubeek.enums.UserRoleType;

public class User extends Model {

//	public enum Role {
//
//		EMPTY("member"), EDITOR("editor"), SPORT_MANAGER("sportManager"), CLUB_MANAGER("clubManager"), ADMINISTRATOR( //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//				"administrator"); //$NON-NLS-1$
//
//		private Role(String textId) {
//			this.textId = textId;
//		}
//
//		@Override
//		public String toString() {
//			return Messages.getString(textId);
//		}
//
//		public final String textId;
//	}

	/* PRIVATE */

	/** Uzivatelske jmeno */
	private String name = ""; //$NON-NLS-1$

	/** Heslo */
	private String password = ""; //$NON-NLS-1$

	/** Uzivatelska role */
	private UserRoleType role = UserRoleType.EMPTY;

	/** identifikator asociovaneho clena klubu */
	private int clubMemberId;

	/** Data asociovaneho clena klubu */
	private ClubMember clubMember;

	/** Pole identifikatoru tymu do kterych uzivatel patri */
	private int[] teams;

	/* PUBLIC */

	public static String hashPassword(String password, String salt) {
		MessageDigest hashProc = null;
		try {
			hashProc = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException ex) {
			return null;
		}

		if (hashProc != null) {
			hashProc.update((salt + password).getBytes());
			return Hex.encodeHexString(hashProc.digest(password.getBytes()));
		}

		return null;
	}

	/** Zakoduje heslo podle pravidel pouzivaji trida User */
	public String hashPassword(String password) {
		return hashPassword(password, getName());
	}

	/** Vraci uzivatelske jmeno */
	public String getName() {
		return name;
	}

	/** Nastavi uzivatelske jmeno */
	public void setName(String name) {
		this.name = name;
	}

	/** Vraci uzivatelskou roli */
	public UserRoleType getRole() {
		return role;
	}

	/** Nastavi uzivatelskou roli */
	public void setRole(UserRoleType role) {
		this.role = role;
	}

	/** Vraci uzivatelske heslo */
	public String getPassword() {
		return password;
	}

	/** Vraci zaheshovane heslo */
	public String GetHashPassword() {
		return getPassword() != null ? hashPassword(getPassword(), getName()) : null;
	}

	/** Nastavi uzivatelske heslo */
	public void setPassword(String password) {
		this.password = password;
	}

	/** Vraci identifikator asociovaneho clena klubu */
	public int getClubMemberId() {
		return clubMember != null ? clubMember.getId() : clubMemberId;
	}

	/** Nastavi identifikator asociovaneho clena klubu */
	public void setClubMemberId(int clubMemberId) {
		this.clubMemberId = clubMemberId;
	}

	/** Vraci odkaz asociovaneho clena klubu */
	public ClubMember getClubMember() {
		return clubMember;
	}

	/** Nastavi odkaz na asociovaneho clena klubu */
	public void setClubMember(ClubMember clubMember) {
		this.clubMember = clubMember;
	}

	/** Vraci seznam tymu do kterych uzivatel patri */
	public int[] getTeams() {
		return teams;
	}

	/** Nastavi seznam tymu do kterych uzivatel patri */
	public void setTeams(int[] teams) {
		this.teams = teams;
	}

	/** Testuje zda je uzivatel clenem tymu */
	public boolean isMemberOfTeam(int clubTeamId) {
		for (int id : teams)
			if (clubTeamId == id)
				return true;
		return false;
	}
}
