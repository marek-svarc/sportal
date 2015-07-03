package com.clubeek.ui.main;

import com.clubeek.model.User;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Class that defines main page with the horizontal menu which is still on the
 * top of the page
 *
 * @author Marek Svarc
 */
@Component("navigation")
@Scope("prototype")
public class HorzMenuAbsOnTop extends HorzMenuBase {

    /* PRIVATE */
    private void setMenuButtonStyle(Button btn) {
        btn.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        btn.addStyleName(ValoTheme.BUTTON_TINY);
    }

    /* PROTECTED */
    @Override
    protected String GetMenuItemCaption(User user, HorzMenuNavigationViews viewId, String caption) {
        switch (viewId) {
            case NEWS:
                return this.getClub().getTitle();
        }
        return super.GetMenuItemCaption(user, viewId, caption);
    }

    @Override
    protected String GetMenuItemStyle(HorzMenuNavigationViews viewId) {
        switch (viewId) {
            case NEWS:
                return "clubTitle";
        }
        return super.GetMenuItemStyle(viewId);
    }

    @Override
    protected void createUiControls() {

        super.createUiControls();

        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeightUndefined();

        // Top menu area
        MenuBar menu = getHorzMenu();
        menu.setWidth(100, Unit.PERCENTAGE);
        menu.addStyleName(ValoTheme.MENUBAR_BORDERLESS);

        TextField tfName = getFieldName();
        tfName.addStyleName(ValoTheme.TEXTFIELD_TINY);

        PasswordField tfPassword = getFieldPassword();
        tfPassword.addStyleName(ValoTheme.TEXTFIELD_TINY);

        Button btnSignInOut = getButtonSignInOut();
        setMenuButtonStyle(btnSignInOut);

        Button btnFacebook = getButtonOAuthFacebook();
        setMenuButtonStyle(btnFacebook);

        HorizontalLayout hlTopMenu = new HorizontalLayout();
        hlTopMenu.addStyleName(ValoTheme.LAYOUT_CARD);
        hlTopMenu.addStyleName("topLayout");
        hlTopMenu.setWidth(100, Unit.PERCENTAGE);
        this.addComponent(hlTopMenu);

        hlTopMenu.addComponents(menu, tfName, tfPassword, btnSignInOut, btnFacebook);
        hlTopMenu.setExpandRatio(menu, 1);

        // Views area
        VerticalLayout vlViews = new VerticalLayout();
        vlViews.addStyleName("viewsLayout");
        vlViews.setWidth(1000, Unit.PIXELS);
        vlViews.setHeightUndefined();
        vlViews.addComponent(getViewsContainer());
        this.addComponent(vlViews);
        this.setComponentAlignment(vlViews, Alignment.TOP_CENTER);

        this.setExpandRatio(hlTopMenu, 0);
        this.setExpandRatio(vlViews, 1);
    }

    @Override
    protected void updateUiControls(User user) {
        if (user == null) {
            getFieldName().setVisible(true);
            getFieldPassword().setVisible(true);
            getButtonSignInOut().setIcon(FontAwesome.SIGN_IN);
            getButtonSignInOut().setDescription("Přihlásit se");
            getButtonOAuthFacebook().setVisible(true);
        } else {
            getFieldName().setVisible(false);
            getFieldPassword().setVisible(false);
            getButtonSignInOut().setIcon(FontAwesome.SIGN_OUT);
            getButtonSignInOut().setDescription("Odhlásit se");
            getButtonOAuthFacebook().setVisible(false);
        }

        super.updateUiControls(user);
    }

    public HorzMenuAbsOnTop() {
        super();
    }

    @Override
    public void setUI() {
        createUiControls();
    }
}
