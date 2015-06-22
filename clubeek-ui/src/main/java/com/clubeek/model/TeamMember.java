package com.clubeek.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.clubeek.dao.ClubMemberDao;
import com.clubeek.dao.ClubTeamDao;
import com.clubeek.dao.impl.ownframework.ClubMemberDaoImpl;
import com.clubeek.dao.impl.ownframework.ClubTeamDaoImpl;
import com.clubeek.enums.TeamFunction;

public class TeamMember extends Model {
    // TODO vitfo, created on 11. 6. 2015 
    private ClubMemberDao clubMemberDao = new ClubMemberDaoImpl();
    // TODO vitfo, created on 11. 6. 2015 
    private ClubTeamDao clubTeamDao = new ClubTeamDaoImpl();

//	public enum TeamFunction {
//		PLAYER(Messages.getString("player")), CAPTAIN(Messages.getString("captain")), TEAM_LEADERSHIP(Messages.getString("teamManager")), COACH_ASSISTANT(Messages.getString("assistant")), COACH(Messages.getString("coach")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
//
//		/** Vraci hodnotu flagu pro binarni skladani */
//		public int toFlag() {
//			return 1 << ordinal();
//		}
//
//		/**
//		 * Kontroluje zda parametr "flags" obsahuje
//		 * 
//		 * @param flags
//		 * @return
//		 */
//		public boolean isFlag(int flags) {
//			return (toFlag() & flags) != 0;
//		}
//
//		@Override
//		public String toString() {
//			return this.text;
//		}
//
//		private TeamFunction(String text) {
//			this.text = text;
//		}
//
//		private String text;
//	}

	/* PRIVATE */

	/** identifikator asociovaneho Trida */
	private int clubTeamId;

	/** Data asociovaneho Trida */
	private ClubTeam clubTeam;

	/** identifikator asociovaneho clena klubu */
	private int clubMemberId;

	/** Data asociovaneho clena klubu */
	private ClubMember clubMember;

	/** priznak zda ma clen Trida nejakou funkci */
	private int functions;

	/* PUBLIC */

	public TeamMember() {
		this.setId(0);
		this.setClubTeamId(0);
		this.setClubMemberId(0);
		this.setFunctions(TeamFunction.PLAYER.toFlag());
	}

	public TeamMember(int temId, int clubMemberId) {
		this.setId(0);
		this.setClubTeamId(temId);
		this.setClubMemberId(clubMemberId);
		this.setFunctions(TeamFunction.PLAYER.toFlag());
	}

	public static List<TeamMember> createTeamMembers(int clubTeamId, List<ClubMember> clubMembers) {
		List<TeamMember> teamMembers = new ArrayList<>(clubMembers.size());

		for (ClubMember clubMember : clubMembers)
			teamMembers.add(new TeamMember(clubTeamId, clubMember.getId()));

		return teamMembers;
	}

	public int getClubTeamId() {
		return clubTeamId;
	}

	public void setClubTeamId(int clubTeamId) {
		if (this.clubTeamId != clubTeamId) {
			this.clubTeam = null;
			this.clubTeamId = clubTeamId;
		}
	}

	public ClubTeam getClubTeam() {
		if (clubTeam == null)
//			clubTeam = RepClubTeam.selectById(clubTeamId, null);
		    clubTeam = clubTeamDao.getClubTeamById(clubTeamId);
		return clubTeam;
	}

	/** Nastavi identifikator asociovaneho clena klubu */
	public int getClubMemberId() {
		return clubMemberId;
	}

	/** Vraci identifikator asociovaneho clena klubu */
	public void setClubMemberId(int clubMemberId) {
		if (this.clubMemberId != clubMemberId) {
			this.clubMember = null;
			this.clubMemberId = clubMemberId;
		}
	}

	/**
	 * Vraci clena klubu asociovaneho s clenem Trida. Nacita se z databaze dle
	 * hodnoty ClubMemberId.
	 * 
	 * @throws SQLException
	 */
	public ClubMember getClubMember() {
		if (clubMember == null)
//			clubMember = RepClubMember.selectById(getClubMemberId(), null);
		    // TODO vitfo, created on 11. 6. 2015 - remove from POJO
		    clubMember = clubMemberDao.getClubMember(getClubMemberId());
		return clubMember;
	}

	/** Vraci bitove priznaky vsech funkci asociovanych s clenem Trida */
	public int getFunctions() {
		return functions;
	}

	/** Nastavi bitove priznaky vsech funkci asociovanych s clenem Trida */
	public void setFunctions(int functions) {
		this.functions = functions;
	}

	/** Vraci seznam vsech funkci sociovanych se clenem Trida */
	public List<TeamFunction> getFunctionsAsList() {
		TeamFunction[] allFunctions = TeamFunction.values();

		List<TeamFunction> myFunctions = new ArrayList<>();
		for (TeamFunction teamFunction : allFunctions) {
			if (isFunction(teamFunction))
				myFunctions.add(teamFunction);
		}

		return myFunctions;
	}

	/** Funkce testuje zda je s clenem Trida asociovana dana funkce */
	public boolean isFunction(TeamFunction function) {
		return function.isFlag(this.functions);
	}
}
