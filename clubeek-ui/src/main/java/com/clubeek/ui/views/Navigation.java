package com.clubeek.ui.views;

/**
 * Rozhrani popisujici navigaci v aplikaci
 * 
 * @author Marek Svarc
 * 
 */
public interface Navigation {

	public enum ViewId {
		CLUB_USERS("clubUsers"), //$NON-NLS-1$ 
		CLUB_NEWS("clubNews"), //$NON-NLS-1$ 
		CLUB_CATEGORIES("categories"), //$NON-NLS-1$
		CLUB_TEAMS("teams"), //$NON-NLS-1$ 
		CLUB_RIVALS("clubs"), //$NON-NLS-1$ 
		CLUB_MEMBERS("clubMembers"), //$NON-NLS-1$ 
		TEAM_MEMBERS("temMembers"), //$NON-NLS-1$ 
		TEAM_NEWS("teamNews"), //$NON-NLS-1$ 
		TEAM_ROSTER("teamRoster"), //$NON-NLS-1$ 
		TEAM_MATCHES("teamMatches"), //$NON-NLS-1$ 
		TEAM_TRAININGS("temTrainings"), //$NON-NLS-1$
		ARTICLES("articles"), //$NON-NLS-1$
		ARTICLE("article"); //$NON-NLS-1$

		private ViewId(String id) {
			this.id = id;
		}

		/** Identifikator pohledu */
		public final String id;
	}

	/**
	 * Navigace v ramci aplikace
	 * 
	 * @param view
	 *            identifikator pohledu (komponent implementujici rozhrani "view")
	 * @param id
	 *            parametr predavany vstupni metode Enter
	 */
	void navigateTo(ViewId view, String param);

	/**
	 * Pregeneruje hlavni nabidku aplikace
	 */
	void updateNavigationMenu();
}
