package com.clubeek.ui.main;

import com.clubeek.model.ClubSetting;
import com.clubeek.model.User;
import com.clubeek.ui.Messages;
import com.clubeek.ui.Tools;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.stereotype.Component;

/**
 * Class that defines main page with the standard horizontal menu
 *
 * @author Marek Svarc
 */
public class HorzMenuStandard extends HorzMenuBase {

    /**
     * Layout controls for user administration
     */
    private FormLayout vlLogin = null;
    
    public HorzMenuStandard() {
            super();
        }

    /* PROTECTED */
    @Override
    protected void createUiControls() {

        super.createUiControls();

        // Zakladni rozvrzeni stranky
        this.addStyleName("ss-main-page"); //$NON-NLS-1$
        this.setHeightUndefined();

        final VerticalLayout mainLayout = new VerticalLayout();
        this.addComponent(mainLayout);
        this.setComponentAlignment(mainLayout, Alignment.TOP_CENTER);
        mainLayout.addStyleName("ss-main-layout"); //$NON-NLS-1$
        mainLayout.setWidth(900, Unit.PIXELS);

        // Hlavicka stranky (logo, nadpis, prihlaseni/odhlaseni)
        ClubSetting settings = getClubSettings();
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
            Button btn = getButtonSignInOut();
            btn.setWidth(100, Unit.PERCENTAGE);
            btn.setStyleName(ValoTheme.BUTTON_TINY);

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

        // Add main application menu
        MenuBar menu = getHorzMenu();
        menu.setStyleName(ValoTheme.MENUBAR_SMALL);
        menu.setAutoOpen(true);
        menu.setSizeFull();
        mainLayout.addComponent(menu);

        // Add container for page views
        Panel views = getViewsContainer();
        views.setSizeFull();
        views.setHeightUndefined();
        views.addStyleName(ValoTheme.PANEL_BORDERLESS);
        mainLayout.addComponent(views);
    }

    @Override
    protected void updateUiControls(User user) {
        // oblat pro prihlaseni/odhlaseni uzivatele
        vlLogin.removeAllComponents();
        if (user == null) {
            // controls to authenticate user
            vlLogin.addComponents(getFieldName(), getFieldPassword(), getButtonSignInOut());
            getButtonSignInOut().setCaption(Messages.getString("signIn"));
        } else {
            // controls to show user informations and sign out
            String userName = user.getClubMember() != null ? user.getClubMember().getName() + " "
                    + user.getClubMember().getSurname() : user.getUsername();
            vlLogin.addComponents(new Label(String.format("<strong>%s</strong>", userName),
                    ContentMode.HTML), getButtonSignInOut());
            getButtonSignInOut().setCaption("Odhlásit");
        }
        
        super.updateUiControls(user);
    }

    @Override
    public void setUI() {
    }
}
