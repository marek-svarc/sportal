package com.clubeek.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.clubeek.dao.CategoryDao;
import com.clubeek.dao.ClubSettingDao;
import com.clubeek.dao.impl.ownframework.CategoryDaoImpl;
import com.clubeek.dao.impl.ownframework.ClubSettingsDaoImpl;
import com.clubeek.dao.impl.ownframework.rep.RepCategory;
import com.clubeek.dao.impl.ownframework.rep.RepClubSettings;
import com.clubeek.db.RepClubTeam;
import com.clubeek.model.Category;
import com.clubeek.model.ClubSettings;
import com.clubeek.model.ClubTeam;
import com.clubeek.model.User;
import com.clubeek.service.SecurityService;
import com.clubeek.service.impl.SecurityServiceImpl;
import com.clubeek.ui.views.Navigation;
import com.clubeek.ui.views.ViewArticle;
import com.clubeek.ui.views.ViewArticles;
import com.clubeek.ui.views.ViewCategories;
import com.clubeek.ui.views.ViewClubMemberCard;
import com.clubeek.ui.views.ViewClubMembers;
import com.clubeek.ui.views.ViewClubRivalCard;
import com.clubeek.ui.views.ViewClubRivals;
import com.clubeek.ui.views.ViewClubTeams;
import com.clubeek.ui.views.ViewNews;
import com.clubeek.ui.views.ViewTeamMatchCard;
import com.clubeek.ui.views.ViewTeamMatches;
import com.clubeek.ui.views.ViewTeamMembers;
import com.clubeek.ui.views.ViewTeamRoster;
import com.clubeek.ui.views.ViewTeamTrainings;
import com.clubeek.ui.views.ViewUsers;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Trida uzivatelskeho prostredi aplikace. - pro navigaci je pouzito
 * horizontalni menu - zobrazeno vice stranek je reseno pomoci zalozek
 * 
 * @author Marek Svarc
 */
@SuppressWarnings("serial")
public class HorzMenuGUI extends VerticalLayout implements Navigation {
	// TODO vitfo, created on 11. 6. 2015 
	private SecurityService securityService = new SecurityServiceImpl();
	// TODO vitfo, created on 11. 6. 2015 
    private CategoryDao categoryDao = new CategoryDaoImpl();
    // TODO vitfo, created on 11. 6. 2015 
    private ClubSettingDao clubSettingDao = new ClubSettingsDaoImpl();

	/* PRIVATE */

	// Interni typy

	/**
	 * Trida zapouzdrujici informace o jedne polozce menu
	 */
	private class MenuCommandInfo {

		/** Text polozky v menu */
		public final String caption;

		/** Zobrazovany objekt */
		public final View view;

		/** Parametr zobrazeni */
		public final String viewParameters;

		public MenuCommandInfo(String name, View view, String viewParameters) {
			this.caption = name;
			this.view = view;
			this.viewParameters = viewParameters;
		}
	}

	/**
	 * Identifikatory skupin zobrazovanych stranek (View)
	 */
	private enum HorzMenuNavigationViews {
		NEWS("news"), //$NON-NLS-1$
		TEAM("team"), //$NON-NLS-1$
		ARTICLES("articles"), //$NON-NLS-1$
		ARTICLE("article"), //$NON-NLS-1$
		RIVALS("rivals"), //$NON-NLS-1$
		SETTINGS("club"), //$NON-NLS-1$
		USERS("users"), //$NON-NLS-1$
		MEMBER_CARD("memberCard"), //$NON-NLS-1$
		RIVAL_CARD("rivalCard"), //$NON-NLS-1$
		MATCH_CARD("matchCard") //$NON-NLS-1$
		;

		private HorzMenuNavigationViews(String id) {
			this.id = id;
		}

		/** Identifikotor pohledu */
		public final String id;
	}

	// Data

	/** Nadrazene uzivatelske prostredi */
	private final UI mUI;

	/** Textove pole pro zadani uzivatelskeho jmena */
	private TextField tfName = null;

	/** Textove pole pro zadani hesla */
	private PasswordField tfPassword = null;

	/** Hlavni vodorovne menu */
	private MenuBar mbMainHorz = null;

	/** Rozmisteni komponent spravu uzivatelu */
	FormLayout vlLogin = null;

	/** Tlacitko pro prihlaseni do aplikace */
	private Button btSignInOut = null;

	/** Zaznam vsech spoustenych prikazu */
	private final ArrayList<MenuCommandInfo> mbCommands = new ArrayList<>();

	/** Panel pro zobrazovani komponent pohledu */
	private final Panel pnViews;

	/** Navigace */
	private Navigator nvMain;

	// metody

	/**
	 * Sestavi prikaz pro jednu polozku v horizontalni nabidce
	 * 
	 * @param viewName
	 *            identifikator pozadovaneho pohledu
	 * @param viewParameters
	 *            parametr zobrazenďż˝ pohledu
	 */
	private void addMenuCommand(MenuItem menuItem, String caption, View[] views, final HorzMenuNavigationViews viewId,
			final String viewParameters) {

		View view = views[viewId.ordinal()];

		if (view != null) {
			// zaznam o polozce, pouzivane pro zobrazeni aktivni polozky
			mbCommands.add(new MenuCommandInfo(caption, view, viewParameters));

			// anonymni trida pro zachyceni vyberu polozku v menu
			Command command = new MenuBar.Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					navigateTo(viewId, viewParameters);
				}
			};

			// pridani prikazu do menu
			if (menuItem != null)
				menuItem.addItem(caption, command);
			else
				mbMainHorz.addItem(caption, command);
		}
	}

	private void addHideCommand(String caption, View[] views, final HorzMenuNavigationViews viewId, String viewParameters) {

		View view = views[viewId.ordinal()];

		// zaznam o polozce, pouzivane pro zobrazeni aktivni polozky
		if (view != null)
			mbCommands.add(new MenuCommandInfo(caption, views[viewId.ordinal()], viewParameters));
	}

	/**
	 * Aktualizuje nadpis pro zobrazenou skupinu pohledu
	 * 
	 * @param view
	 *            Aktualne zobrazeny pohled (skupina pohledu)
	 * @param viewParameters
	 *            Parametry zobrazeni (napr. index muzstva)
	 */
	private void updateCommandCaption(View view, String viewParameters) {
		// vyhledavani prikazu asociovaneho s viewId a viewParam
		MenuCommandInfo commandInfo = null;
		for (MenuCommandInfo item : mbCommands) {
			if ((item.view == view) && ((item.viewParameters == null) || (item.viewParameters.compareTo(viewParameters) == 0)))
				commandInfo = item;
		}

		// zobrazeni popisu prikazu
		if (commandInfo != null)
			pnViews.setCaption(commandInfo.caption);
	}

	private void navigateTo(HorzMenuNavigationViews viewId, String viewParam) {
		if (viewParam != null)
			nvMain.navigateTo(viewId.id + "/" + viewParam); //$NON-NLS-1$
		else
			nvMain.navigateTo(viewId.id);
	}

	/* PUBLIC */

	public HorzMenuGUI(UI ui) {
		mUI = ui;

		ClubSettings settings = null;
//                settings = RepClubSettings.select(1, null);
		settings = clubSettingDao.getClubSettingById(1);

		// Zakladni rozvrzeni stranky

		this.addStyleName("ss-main-page"); //$NON-NLS-1$
		this.setSizeFull();

		final VerticalLayout mainLayout = new VerticalLayout();
		this.addComponent(mainLayout);
		this.setComponentAlignment(mainLayout, Alignment.TOP_CENTER);
		mainLayout.addStyleName("ss-main-layout"); //$NON-NLS-1$
		mainLayout.setWidth(900, Unit.PIXELS);

		// Hlavicka stranky (logo, nadpis, prihlaseni/odhlaseni)

		if (settings != null) {
			final int TITLE_HEIGHT = 50;
			final int LOGO_SIZE = 80;

			Image imLogo = new Image();
			imLogo.setHeight(LOGO_SIZE, Unit.PIXELS);
			Tools.Components.fillImageByPortrait(imLogo, settings.getLogo(), Integer.toString(settings.getId()));

			Label laClubTitle = new Label(settings.getTitle());
			laClubTitle.setHeight(TITLE_HEIGHT, Unit.PIXELS);
			laClubTitle.setStyleName("page-title"); //$NON-NLS-1$

			Label laClubComment = new Label(settings.getComment());
			laClubComment.setStyleName("page-comment"); //$NON-NLS-1$
			laClubComment.setHeight(LOGO_SIZE - TITLE_HEIGHT, Unit.PIXELS);

			VerticalLayout vlTitle = new VerticalLayout(laClubTitle, laClubComment);
			vlTitle.setStyleName("page-layout-header-title"); //$NON-NLS-1$

			// tlacitko pro prihlaseni/odhlaseni uzivatele
			final Navigation navigation = this;
			btSignInOut = new Button(null, new Button.ClickListener() { //$NON-NLS-1$

						@Override
						public void buttonClick(ClickEvent event) {
							User user = securityService.getUser();
							if (user == null)
								securityService.login(navigation, tfName.getValue(), tfPassword.getValue());
							else
								securityService.logout(navigation);
						}
					});
			btSignInOut.setWidth(100, Unit.PERCENTAGE);
			btSignInOut.setStyleName(ValoTheme.BUTTON_TINY);

			// Oblast pro zobrazeni informaci o uzivateli
			vlLogin = new FormLayout();
			vlLogin.setMargin(false);
			vlLogin.setSpacing(false);
			vlLogin.setWidth(200, Unit.PIXELS);
			vlLogin.setHeight(LOGO_SIZE, Unit.PIXELS);

			HorizontalLayout hlHeader = Tools.Components.createHorizontalLayout(null, imLogo, vlTitle, vlLogin);
			hlHeader.setWidth(100, Unit.PERCENTAGE);
			hlHeader.setExpandRatio(vlTitle, 1.0f);
			hlHeader.setStyleName("page-layout-header"); //$NON-NLS-1$
			mainLayout.addComponent(hlHeader);
		}

		// Hlavni menu aplikace
		mbMainHorz = new MenuBar();
		mbMainHorz.setStyleName(ValoTheme.MENUBAR_SMALL);
		mbMainHorz.setAutoOpen(true);
		mbMainHorz.setSizeFull();
		mainLayout.addComponent(mbMainHorz);

		// Panel pro zobrazovani aktualniho obsahu
		pnViews = new Panel();
		pnViews.setSizeFull();
		pnViews.setHeightUndefined();
		pnViews.addStyleName(ValoTheme.PANEL_BORDERLESS);
		//pnViews.addStyleName(ValoTheme.PANEL_WELL);
		mainLayout.addComponent(pnViews);

		// Aktualizace menu a navigovani
		updateNavigationMenu();
	}

	// rozhrani Navigation

	@Override
	public void navigateTo(ViewId view, String param) {
		switch (view) {
		case CLUB_NEWS:
			navigateTo(HorzMenuNavigationViews.NEWS, param);
			break;
		case CLUB_CATEGORIES:
		case CLUB_TEAMS:
		case CLUB_MEMBERS:
		case TEAM_MEMBERS:
			navigateTo(HorzMenuNavigationViews.SETTINGS, param);
			break;
		case CLUB_USERS:
			navigateTo(HorzMenuNavigationViews.USERS, param);
			break;
		case TEAM_NEWS:
		case TEAM_ROSTER:
		case TEAM_MATCHES:
		case TEAM_TRAININGS:
			navigateTo(HorzMenuNavigationViews.TEAM, param);
			break;
		case ARTICLES:
			navigateTo(HorzMenuNavigationViews.ARTICLES, param);
			break;
		case ARTICLE:
			navigateTo(HorzMenuNavigationViews.ARTICLE, param);
			break;
		case CLUB_RIVALS:
			navigateTo(HorzMenuNavigationViews.RIVALS, param);
			break;
                case CLUB_MEMBER_CARD:
			navigateTo(HorzMenuNavigationViews.MEMBER_CARD, param);
			break;
                case CLUB_RIVAL_CARD:
			navigateTo(HorzMenuNavigationViews.RIVAL_CARD, param);
			break;
                case TEAM_MATCH_CARD:
			navigateTo(HorzMenuNavigationViews.MATCH_CARD, param);
			break;
		}
	}

	@Override
	public void updateNavigationMenu() {

		// data aktualne prihlaseneho uzivatele
		User user = securityService.getUser();

		// oblat pro prihlaseni/odhlaseni uzivatele
		vlLogin.removeAllComponents();
		if (user == null) {
			// komponenty pro prihlaseni uzivatele
			tfName = Tools.Components.createTextField(Messages.getString("name")); //$NON-NLS-1$
			tfPassword = Tools.Components.createPasswordField(Messages.getString("password")); //$NON-NLS-1$
			vlLogin.addComponents(tfName, tfPassword, btSignInOut);
			btSignInOut.setCaption(Messages.getString("signIn"));
		} else {
			// komponenty pro zobrazeni informaci o uzivateli a odhlaseni
			String userName = user.getClubMember() != null ? user.getClubMember().getName() + " "
					+ user.getClubMember().getSurname() : user.getName();
			vlLogin.addComponents(new Label(String.format("<strong>%s</strong>", userName),
					ContentMode.HTML), btSignInOut);
			btSignInOut.setCaption("Odhlásit");
		}
		
		/* NAVIGATOR */

		// vytvoreni navigace
		nvMain = new Navigator(mUI, pnViews);

		nvMain.addViewChangeListener(new ViewChangeListener() {

			@Override
			public boolean beforeViewChange(ViewChangeEvent event) {
				return true;
			}

			@Override
			public void afterViewChange(ViewChangeEvent event) {
				updateCommandCaption(event.getNewView(), event.getParameters());
			}
		});

		// pole identifikatoru navigovatelnych komponent
		HorzMenuNavigationViews[] viewsValues = HorzMenuNavigationViews.values();

		// vytvoreni navigovatelnych komponent
		View[] views = new View[viewsValues.length];
		Arrays.fill(views, null);

		views[HorzMenuNavigationViews.NEWS.ordinal()] = new ViewNews(this);
		views[HorzMenuNavigationViews.ARTICLE.ordinal()] = (View) new ViewArticle();
		views[HorzMenuNavigationViews.MEMBER_CARD.ordinal()] = (View) new ViewClubMemberCard(this);
		views[HorzMenuNavigationViews.RIVAL_CARD.ordinal()] = (View) new ViewClubRivalCard(this);
		views[HorzMenuNavigationViews.MATCH_CARD.ordinal()] = (View) new ViewTeamMatchCard(this);
                
		if ((user != null) && securityService.checkRole(user.getRole(), User.Role.EDITOR)) {
			views[HorzMenuNavigationViews.ARTICLES.ordinal()] = (View) new ViewArticles(this);
		}
		if ((user != null) && securityService.checkRole(user.getRole(), User.Role.SPORT_MANAGER)) {
			views[HorzMenuNavigationViews.TEAM.ordinal()] = new LayoutTabSheet(new ViewNews(this), new ViewTeamRoster(this),
					new ViewTeamMatches(), new ViewTeamTrainings());
			views[HorzMenuNavigationViews.RIVALS.ordinal()] = (View) new ViewClubRivals();
		} else {
			views[HorzMenuNavigationViews.TEAM.ordinal()] = new LayoutTabSheet(new ViewNews(this), new ViewTeamRoster(this));
		}
		if ((user != null) && securityService.checkRole(user.getRole(), User.Role.CLUB_MANAGER)) {
			views[HorzMenuNavigationViews.SETTINGS.ordinal()] = (View) new LayoutTabSheet(new ViewCategories(this),
					new ViewClubTeams(this), new ViewClubMembers(), new ViewTeamMembers());
		}
		if ((user != null) && securityService.checkRole(user.getRole(), User.Role.ADMINISTRATOR)) {
			views[HorzMenuNavigationViews.USERS.ordinal()] = (View) new ViewUsers();
		}

		// prirazeni nenulovych navigovatelnych komponent do navigatoru
		nvMain.addView("", views[HorzMenuNavigationViews.NEWS.ordinal()]); //$NON-NLS-1$
		for (HorzMenuNavigationViews item : viewsValues) {
			if (views[item.ordinal()] != null)
				nvMain.addView(item.id, views[item.ordinal()]);
		}

		/* MENU */

		MenuItem menuItem;

		// odstraneni puvodnich prvku menu
		mbMainHorz.removeItems();

//                List<Category> categoryList = RepCategory.select(true, null);
		        List<Category> categoryList = categoryDao.getActiveCategories();
                List<ClubTeam> teamList = RepClubTeam.select(true, null);
                addMenuCommand(null, "Klub", views, HorzMenuNavigationViews.NEWS, null);
                if (teamList != null)
                    for (ClubTeam team : teamList)
                        if ((team.getCategory() == null) || (!team.getCategory().getActive()))
                            addMenuCommand(null, team.getName(), views, HorzMenuNavigationViews.TEAM, Integer.toString(team.getId()));
                if (categoryList != null)
                    for (Category category : categoryList) {
                        menuItem = mbMainHorz.addItem(category.getDescription(), null);
                        // vkladana aktivnich tymu asociovanych s kategorii
                        for (ClubTeam team : teamList)
                            if (team.getCategoryId() == category.getId())
                                addMenuCommand(menuItem, team.getName(), views, HorzMenuNavigationViews.TEAM,
									Integer.toString(team.getId()));
				}

		// menu nastaveni
		if ((user != null) && (securityService.checkRole(user.getRole(), User.Role.EDITOR))) {
			menuItem = mbMainHorz.addItem(Messages.getString("settings"), null); //$NON-NLS-1$
			addMenuCommand(menuItem, Messages.getString("articles"), views, HorzMenuNavigationViews.ARTICLES, null); //$NON-NLS-1$
			addMenuCommand(menuItem, Messages.getString("rivalsCatalogue"), views, HorzMenuNavigationViews.RIVALS, null); //$NON-NLS-1$
			addMenuCommand(menuItem, Messages.getString("clubManagement"), views, HorzMenuNavigationViews.SETTINGS, null); //$NON-NLS-1$
			addMenuCommand(menuItem, "Správa uživatelů", views, HorzMenuNavigationViews.USERS, null);
		}

		// prikazy, ktere nejsou spoustene z menu
		addHideCommand(Messages.getString("articles"), views, HorzMenuNavigationViews.ARTICLE, null); //$NON-NLS-1$
		addHideCommand("Karta hráče", views, HorzMenuNavigationViews.MEMBER_CARD, null);
		addHideCommand("Karta soupeře", views, HorzMenuNavigationViews.RIVAL_CARD, null);
		addHideCommand("Informace o zápase", views, HorzMenuNavigationViews.MATCH_CARD, null);
	}
}
