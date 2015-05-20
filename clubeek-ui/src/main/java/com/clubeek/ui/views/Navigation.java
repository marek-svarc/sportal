package com.clubeek.ui.views;

/**
 * Interface that defines navigation in application
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
		CLUB_MEMBER_CARD("clubMemberInfo"), //$NON-NLS-1$ 
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

		/** Identifier of the view */
		public final String id;
	}

	/**
	 * Show view according to the ViewId
	 * 
	 * @param view
	 *            identifier of the view (view is component implementing interface View)
	 * @param param
	 *            text parameters pushed to the entrance method Enter
	 */
	void navigateTo(ViewId view, String param);

	/**
	 * Update application menu
	 */
	void updateNavigationMenu();
}
