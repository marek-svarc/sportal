package com.clubeek.ui.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.clubeek.dao.CategoryDao;
import com.clubeek.dao.ClubSettingDao;
import com.clubeek.dao.ClubTeamDao;
import com.clubeek.dao.impl.ownframework.CategoryDaoImpl;
import com.clubeek.dao.impl.ownframework.ClubSettingsDaoImpl;
import com.clubeek.dao.impl.ownframework.ClubTeamDaoImpl;
import com.clubeek.enums.Role;
import com.clubeek.model.Category;
import com.clubeek.model.ClubSettings;
import com.clubeek.model.ClubTeam;
import com.clubeek.model.User;
import com.clubeek.service.SecurityService;
import com.clubeek.service.impl.SecurityServiceImpl;
import com.clubeek.ui.LayoutTabSheet;
import com.clubeek.ui.Messages;
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
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Trida uzivatelskeho prostredi aplikace. - pro navigaci je pouzito
 * horizontalni menu - zobrazeno vice stranek je reseno pomoci zalozek
 *
 * @author Marek Svarc
 */
@SuppressWarnings("serial")
public abstract class HorzMenuBase extends VerticalLayout implements Navigation {

    /* PRIVATE */
    /** Trida zapouzdrujici informace o jedne polozce menu */
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

    // Data
    /** Nadrazene uzivatelske prostredi */
    private final UI ui;

    // TODO vitfo, created on 11. 6. 2015 
    private final SecurityService securityService = new SecurityServiceImpl();

    // TODO vitfo, created on 11. 6. 2015 
    private final CategoryDao categoryDao = new CategoryDaoImpl();

    // TODO vitfo, created on 11. 6. 2015 
    private final ClubSettingDao clubSettingDao = new ClubSettingsDaoImpl();

    // TODO vitfo, created on 11. 6. 2015 
    private final ClubTeamDao clubTeamDao = new ClubTeamDaoImpl();

    private ClubSettings clubSettings;

    /** Navigation menu in application */
    private MenuBar horzMenu = null;

    /** Input of the user name */
    private TextField fieldName = null;

    /** Input of the user password */
    private PasswordField fieldPassword = null;

    /** Panel pro zobrazovani komponent pohledu */
    private final Panel viewsContainer;

    /** Run standard autentication */
    private Button buttonSignInOut = null;

    /** Run OAuth - Facebook autentication */
    private Button buttonOAuthFacebook = null;

    /** Zaznam vsech spoustenych prikazu */
    private final ArrayList<MenuCommandInfo> commands = new ArrayList<>();

    /** Navigace */
    private Navigator navigator;

    /**
     * Aktualizuje nadpis pro zobrazenou skupinu pohledu
     *
     * @param view Aktualne zobrazeny pohled (skupina pohledu)
     * @param viewParameters Parametry zobrazeni (napr. index muzstva)
     */
    private void updateCommandCaption(View view, String viewParameters) {
        // vyhledavani prikazu asociovaneho s viewId a viewParam
        MenuCommandInfo commandInfo = null;
        for (MenuCommandInfo item : commands) {
            if ((item.view == view) && ((item.viewParameters == null) || (item.viewParameters.compareTo(viewParameters) == 0))) {
                commandInfo = item;
            }
        }

        // zobrazeni popisu prikazu
        if (commandInfo != null) {
            viewsContainer.setCaption(commandInfo.caption);
        }
    }

    /**
     * Sestavi prikaz pro jednu polozku v horizontalni nabidce
     *
     * @param menuItem
     * @param caption
     * @param views
     * @param viewId
     * @param viewParameters
     */
    private void addMenuCommand(User user, MenuItem menuItem, String caption, View[] views, final HorzMenuNavigationViews viewId,
            final String viewParameters) {

        View view = views[viewId.ordinal()];

        if (view != null) {
            // zaznam o polozce, pouzivane pro zobrazeni aktivni polozky
            commands.add(new MenuCommandInfo(caption, view, viewParameters));

            // anonymni trida pro zachyceni vyberu polozku v menu
            Command command = new MenuBar.Command() {
                @Override
                public void menuSelected(MenuItem selectedItem) {
                    navigateTo(viewId, viewParameters);
                }
            };

            // add command to the menu
            MenuItem newItem;
            caption = GetMenuItemCaption(user, viewId, caption);
            if (menuItem != null) {
                newItem = menuItem.addItem(caption, command);
            } else {
                newItem = horzMenu.addItem(caption, command);
            }

            // add external style
            String style = GetMenuItemStyle(viewId);
            if (style != null) {
                newItem.setStyleName(style);
            }
        }
    }

    private void addHideCommand(String caption, View[] views, final HorzMenuNavigationViews viewId, String viewParameters) {

        View view = views[viewId.ordinal()];

        // zaznam o polozce, pouzivane pro zobrazeni aktivni polozky
        if (view != null) {
            commands.add(new MenuCommandInfo(caption, views[viewId.ordinal()], viewParameters));
        }
    }

    private void navigateTo(HorzMenuNavigationViews viewId, String viewParam) {
        if (viewParam != null) {
            navigator.navigateTo(viewId.id + "/" + viewParam); //$NON-NLS-1$
        } else {
            navigator.navigateTo(viewId.id);
        }
    }

    /* PROTECTED */
    /** Identifikatory skupin zobrazovanych stranek (View) */
    protected enum HorzMenuNavigationViews {

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

    /** Returns application navigation menu */
    protected MenuBar getHorzMenu() {
        return horzMenu;
    }

    /** Returns container component for all views */
    protected Panel getViewsContainer() {
        return viewsContainer;
    }

    /** Input of the user name */
    protected TextField getFieldName() {
        return fieldName;
    }

    /** Input of the user password */
    protected PasswordField getFieldPassword() {
        return fieldPassword;
    }

    /** Returns buttons that run sgni in/out process */
    protected Button getButtonSignInOut() {
        return buttonSignInOut;
    }

    /** Returns button that run OAuth authentication using Facebook */
    protected Button getButtonOAuthFacebook() {
        return buttonOAuthFacebook;
    }

    /** Enables to change default manu item caption */
    protected String GetMenuItemCaption(User user, HorzMenuNavigationViews viewId, String caption) {
        return caption;
    }

    protected String GetMenuItemStyle(HorzMenuNavigationViews viewId) {
        return null;
    }

    /** Creates all components of the page */
    protected void createUiControls() {

        fieldName = new TextField();
        fieldName.setInputPrompt(Messages.getString("name"));
        fieldPassword = new PasswordField();
        //fieldPassword.setInputPrompt(Messages.getString("password"));

        final Navigation navigation = this;
        buttonSignInOut = new Button(null, new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                User user = securityService.getUser();
                if (user == null) {
                    securityService.login(navigation, fieldName.getValue(), fieldPassword.getValue());
                    fieldPassword.setValue("");
                } else {
                    securityService.logout(navigation);
                }
            }
        });

        buttonOAuthFacebook = new Button(null, new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        buttonOAuthFacebook.setIcon(FontAwesome.FACEBOOK);
        buttonOAuthFacebook.setDescription("Přihlásit se pomocí aplikace Facebook");
    }

    /** Update components on the page */
    protected void updateUiControls(User user) {

        /* NAVIGATOR */
        // vytvoreni navigace
        navigator = new Navigator(ui, viewsContainer);
        navigator.addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(ViewChangeListener.ViewChangeEvent event) {
                return true;
            }

            @Override
            public void afterViewChange(ViewChangeListener.ViewChangeEvent event) {
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

        if ((user != null) && securityService.checkRole(user.getRole(), Role.EDITOR)) {
            views[HorzMenuNavigationViews.ARTICLES.ordinal()] = (View) new ViewArticles(this);
        }
        if ((user != null) && securityService.checkRole(user.getRole(), Role.SPORT_MANAGER)) {
            views[HorzMenuNavigationViews.TEAM.ordinal()] = new LayoutTabSheet(new ViewNews(this), new ViewTeamRoster(this),
                    new ViewTeamMatches(), new ViewTeamTrainings());
            views[HorzMenuNavigationViews.RIVALS.ordinal()] = (View) new ViewClubRivals();
        } else {
            views[HorzMenuNavigationViews.TEAM.ordinal()] = new LayoutTabSheet(new ViewNews(this), new ViewTeamRoster(this));
        }
        if ((user != null) && securityService.checkRole(user.getRole(), Role.CLUB_MANAGER)) {
            views[HorzMenuNavigationViews.SETTINGS.ordinal()] = (View) new LayoutTabSheet(new ViewCategories(this),
                    new ViewClubTeams(this), new ViewClubMembers(), new ViewTeamMembers());
        }
        if ((user != null) && securityService.checkRole(user.getRole(), Role.ADMINISTRATOR)) {
            views[HorzMenuNavigationViews.USERS.ordinal()] = (View) new ViewUsers();
        }

        // prirazeni nenulovych navigovatelnych komponent do navigatoru
        navigator.addView("", views[HorzMenuNavigationViews.NEWS.ordinal()]); //$NON-NLS-1$
        for (HorzMenuNavigationViews item : viewsValues) {
            if (views[item.ordinal()] != null) {
                navigator.addView(item.id, views[item.ordinal()]);
            }
        }

        /* MENU */
        MenuItem menuItem;

        // odstraneni puvodnich prvku menu
        horzMenu.removeItems();

        List<Category> categoryList = categoryDao.getActiveCategories();
        List<ClubTeam> teamList = clubTeamDao.getActiveClubTeams();
        addMenuCommand(user, null, "Klub", views, HorzMenuNavigationViews.NEWS, null);
        if (teamList != null) {
            for (ClubTeam team : teamList) {
                if ((team.getCategory() == null) || (!team.getCategory().getActive())) {
                    addMenuCommand(user, null, team.getName(), views, HorzMenuNavigationViews.TEAM, Integer.toString(team.getId()));
                }
            }
        }
        if (categoryList != null) {
            for (Category category : categoryList) {
                menuItem = horzMenu.addItem(category.getDescription(), null);
                // vkladana aktivnich tymu asociovanych s kategorii
                for (ClubTeam team : teamList) {
                    if (team.getCategoryId() == category.getId()) {
                        addMenuCommand(user, menuItem, team.getName(), views, HorzMenuNavigationViews.TEAM,
                                Integer.toString(team.getId()));
                    }
                }
            }
        }

        // menu nastaveni
        if ((user != null) && (securityService.checkRole(user.getRole(), Role.EDITOR))) {
            menuItem = horzMenu.addItem(Messages.getString("settings"), null); //$NON-NLS-1$
            addMenuCommand(user, menuItem, Messages.getString("articles"), views, HorzMenuNavigationViews.ARTICLES, null); //$NON-NLS-1$
            addMenuCommand(user, menuItem, Messages.getString("rivalsCatalogue"), views, HorzMenuNavigationViews.RIVALS, null); //$NON-NLS-1$
            addMenuCommand(user, menuItem, Messages.getString("clubManagement"), views, HorzMenuNavigationViews.SETTINGS, null); //$NON-NLS-1$
            addMenuCommand(user, menuItem, "Správa uživatelů", views, HorzMenuNavigationViews.USERS, null);
        }

        // prikazy, ktere nejsou spoustene z menu
        addHideCommand(Messages.getString("articles"), views, HorzMenuNavigationViews.ARTICLE, null); //$NON-NLS-1$
        addHideCommand("Karta hráče", views, HorzMenuNavigationViews.MEMBER_CARD, null);
        addHideCommand("Karta soupeře", views, HorzMenuNavigationViews.RIVAL_CARD, null);
        addHideCommand("Informace o zápase", views, HorzMenuNavigationViews.MATCH_CARD, null);

    }

    /* PUBLIC */
    public HorzMenuBase(UI ui) {
        this.ui = ui;
        this.clubSettings = clubSettingDao.getClubSettingById(1);

        // controls initialization
        horzMenu = new MenuBar();
        viewsContainer = new Panel();
        createUiControls();

        // update of the menu
        updateNavigationMenu();
    }

    // properties
    /** Informations about club */
    public ClubSettings getClubSettings() {
        return clubSettings;
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
        updateUiControls(securityService.getUser());
    }
}
