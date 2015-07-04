package com.clubeek.ui;

import java.util.List;

import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ContextLoaderListener;

import com.clubeek.dao.UserDao;
import com.clubeek.enums.UserRoleType;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

@SuppressWarnings("serial")
@Theme("valo_base")
@Widgetset("com.clubeek.MyAppWidgetset")
@SpringUI
public class MyUI extends UI {

    @Autowired
    private UserDao userDao;

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
    
    @Autowired
    private ApplicationContext applicationContext;

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
        
        List<User> admins = userDao.getAllAdministrators();
        if ((admins == null) || (admins.size() <= 0)) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword("admin");
            user.setUserRoleType(UserRoleType.ADMINISTRATOR);
            userDao.insertUser(user);
        }

        // pouziti prostredi s horizontalnim navigacnim menu
        HorzMenuAbsOnTop content = applicationContext.getBean(HorzMenuAbsOnTop.class);
        //HorzMenuAbsOnTop content = new HorzMenuAbsOnTop();
        setContent(content);
        content.setUI();
        content.updateNavigationMenu();
        
    }
}
