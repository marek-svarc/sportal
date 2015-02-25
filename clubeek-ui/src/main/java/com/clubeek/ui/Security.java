package com.clubeek.ui;

import java.sql.SQLException;

import com.clubeek.db.RepUser;
import com.clubeek.model.User;
import com.clubeek.ui.views.Navigation;
import com.clubeek.ui.views.Navigation.ViewId;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

final public class Security {

	@SuppressWarnings("serial")
	static public class UnauthorizedAcess extends RuntimeException {
		@Override
		public String getMessage() {
			return "Nepovolený přístup";
		}
	}

	public static void login(Navigation navigation, String name, String password) {
		try {
			// nacteni uzivatele podle zadaneho uzivatelskeho jmena
			User user = RepUser.selectByName(name, true, null);
			if (user == null) {
				Notification.show(Messages.getString("error"), //$NON-NLS-1$
						String.format("Uživatelské jméno '%s' neexistuje.", name), Type.ERROR_MESSAGE);
				navigation.navigateTo(ViewId.CLUB_NEWS, null);
				return;
			}

			// hashovani zadaneho hesla a porovnani s ulozenym
			password = user.hashPassword(password);
			if (password.equals(user.getPassword())) {
				// prirazeni aktualniho uzivatele do session
				VaadinSession.getCurrent().setAttribute(User.class, user);
				// aktualizace menu
				navigation.updateNavigationMenu();
				// zobrazi se stranka tymu jehoz je uzivatel clenem
				if ((user.getTeams() != null) && (user.getTeams().length > 0))
					navigation.navigateTo(ViewId.TEAM_NEWS, Integer.toString(user.getTeams()[0]));
			} else {
				Notification.show(Messages.getString("error"), "Heslo není zadané správně", Type.ERROR_MESSAGE);//$NON-NLS-1$
				navigation.navigateTo(ViewId.CLUB_NEWS, null);
				return;
			}
		} catch (SQLException e) {
			Tools.msgBoxSQLException(e);
		}
	}

	/** Ukonci sezeni jednoho uzivatele */
	public static void logout(Navigation navigation) {
		VaadinSession.getCurrent().setAttribute(User.class, null);
		navigation.updateNavigationMenu();
		navigation.navigateTo(ViewId.CLUB_NEWS, null);
	}

	/**
	 * Kontroluje uroven uzivatelskeho opravneni
	 * 
	 * @param role
	 *            uroven pozadovaneho opravneni
	 * @return
	 */
	public static boolean authorizeRole(User.Role role) {
		// kontrola existence uzivate
		User user = getUser();
		// autorizace
		return ((user != null) && (user.getRole().ordinal() >= role.ordinal()));
	}

	/**
	 * Kontroluje zda je uzivatel prihlasen a zda je clenem tymu
	 * 
	 * @param clubTeamId
	 *            unikatni identifikator tymu
	 * @return
	 */
	public static boolean authorizeTeamMember(int clubTeamId) {
		// kontrola existence uzivate
		User user = getUser();
		// autorizace
		return ((user != null) && (user.isMemberOfTeam(clubTeamId)));
	}

	/** Kontroluje prihlaseni a autorizuje podle role */
	public static void authorize(User.Role role) {
		if (!authorizeRole(role))
			throw new UnauthorizedAcess();
	}

	/** Vraci aktualniho uzivatele, pokud neni prihlasen vraci null */
	public static User getUser() {
		return VaadinSession.getCurrent().getAttribute(User.class);
	}
	
	public static boolean checkRole(User.Role userRole, User.Role authRole) {
		return userRole.ordinal() >= authRole.ordinal();
	}

}
