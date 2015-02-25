package com.clubeek.ui;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.clubeek.db.RepUser;
import com.clubeek.model.User;
import com.clubeek.ui.views.Navigation;
import com.clubeek.ui.views.Navigation.ViewId;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.ErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Theme("valo_base")
@Widgetset("com.clubeek.MyAppWidgetset")
public class MyUI extends UI {

    @WebServlet(urlPatterns = "/*", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class Servlet extends VaadinServlet {
    }

    /* PROTECTED */
    @Override
    protected void init(VaadinRequest request) {

        setErrorHandler(new ErrorHandler() {

            @Override
            public void error(com.vaadin.server.ErrorEvent event) {
                if (getContent() instanceof Navigation) {
                    ((Navigation) (getContent())).navigateTo(ViewId.CLUB_NEWS, "");
                } else {
                    getUI().close();
                }
            }
        });

        // kontrola existence administratorskeho uctu
        try {
            List<User> admins = RepUser.selectAllAdministrators(new RepUser.TableColumn[]{RepUser.TableColumn.ID});
            if ((admins == null) || (admins.size() <= 0)) {
                User user = new User();
                user.setName("admin");
                user.setPassword("admin");
                user.setRole(User.Role.ADMINISTRATOR);
                RepUser.insert(user);
            }
        } catch (SQLException e) {
            Tools.msgBoxSQLException(e);
        }

        // pouziti prostredi s horizontalnim navigacnim menu
        setContent(new HorzMenuGUI(this));
    }
}
