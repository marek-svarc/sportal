package com.clubeek.ui;

import java.util.List;

import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ContextLoaderListener;

import com.clubeek.dao.UserDao;
import com.clubeek.dao.impl.ownframework.UserDaoImpl;
import com.clubeek.enums.Role;
import com.clubeek.model.User;
import com.clubeek.ui.main.HorzMenuAbsOnTop;
import com.clubeek.ui.views.Navigation;
import com.clubeek.ui.views.Navigation.ViewId;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.ErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.EnableVaadin;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Theme("valo_base")
@Widgetset("com.clubeek.MyAppWidgetset")
@SpringUI
public class MyUI extends UI {
	// TODO vitfo, created on 11. 6. 2015 
	private UserDao userDao = new UserDaoImpl();

    @WebServlet(urlPatterns = "/*", asyncSupported = true)
//    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class Servlet extends SpringVaadinServlet {
    }
    
    @WebListener
    public static class MyContextLoaderListener extends ContextLoaderListener {
    }
    
    @Configuration
    @EnableVaadin
    public static class MyConfiguration {
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
        
//        List<User> admins = RepUser.selectAllAdministrators(new RepUser.TableColumn[]{RepUser.TableColumn.ID});
        List<User> admins = userDao.getAllAdministrators();
        if ((admins == null) || (admins.size() <= 0)) {
            User user = new User();
            user.setName("admin");
            user.setPassword("admin");
            user.setRole(Role.ADMINISTRATOR);
//            RepUser.insert(user);
            userDao.insertUser(user);
        }

        // pouziti prostredi s horizontalnim navigacnim menu
        setContent(new HorzMenuAbsOnTop(this));
    }
}
