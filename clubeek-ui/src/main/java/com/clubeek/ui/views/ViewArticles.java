package com.clubeek.ui.views;

import java.util.List;

import com.clubeek.dao.ArticleDao;
import com.clubeek.dao.CategoryDao;
import com.clubeek.dao.ClubTeamDao;
import com.clubeek.enums.UserRoleType;
import com.clubeek.model.Article;
import com.clubeek.model.Category;
import com.clubeek.model.ClubTeam;
import com.clubeek.service.SecurityService;
import com.clubeek.ui.ModalDialog;
import com.clubeek.ui.ModalDialog.Mode;
import com.clubeek.ui.components.ActionTable;
import com.clubeek.ui.frames.FrameArticle;
import com.clubeek.util.DateTime;
import com.clubeek.util.DateTime.DateStyle;
import com.vaadin.data.Container;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@SuppressWarnings("serial")
@Component
@Scope("prototype")
public class ViewArticles extends VerticalLayout implements View, ActionTable.OnActionListener {

    /* PRIVATE */
    @Autowired
    private SecurityService securityService;

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private ClubTeamDao clubTeamDao;

    /** Application navigation provider */
    private Navigation navigation;

    /** Actions table */
    private ActionTable table;

    /* PUBLIC */
    public enum Columns {

        CAPTION, LOCATION, LAST_UPDATE, EXPIRATION_DATE;
    }

    public ViewArticles() {
        this.setSizeFull();

        // view caption
        this.setCaption(Messages.getString("articles"));

        // columns definition
        ActionTable.UserColumnInfo[] columns = {
            new ActionTable.UserColumnInfo(Columns.CAPTION, String.class, Messages.getString("caption")),
            new ActionTable.UserColumnInfo(Columns.LOCATION, String.class, Messages.getString("ViewArticles.8")),
            new ActionTable.UserColumnInfo(Columns.EXPIRATION_DATE, String.class, Messages.getString("expirationDate")),
            new ActionTable.UserColumnInfo(Columns.LAST_UPDATE, String.class, Messages.getString("lastUpdate"))
        };

        // create and place table
        table = new ActionTable(ActionTable.Action.getStandardSet(false, true), columns, this);
        table.addToOwner(this);
    }

    // interface View
    @Override
    public void enter(ViewChangeEvent event) {
        this.navigation = (Navigation) getUI().getContent();

        securityService.authorize(UserRoleType.EDITOR);

        List<Article> articles = articleDao.getAllArticles();

        table.removeAllRows();
        if (articles != null) {
            Container container = table.createDataContainer();
            for (int i = 0; i < articles.size(); ++i) {
                Article article = articles.get(i);
                table.addRow(container, new Object[]{article.getCaption(),
                    GetArticleLocationAsString(article), getExpirationInfoAsString(article),
                    DateTime.dateToString(article.getCreationDate(), DateStyle.SHORT_DAY_AND_TIME)
                }, article.getId());
            }
            table.setDataContainer(container);
        }
    }

    // interface ActionTable.OnActionListener
    @Override
    public boolean doAction(ActionTable.Action action, Object data) {
        switch (action) {
            case SINGLE_ADD:
                addArticle();
                break;
            case SINGLE_EDIT:
                editArticle((int) data);
                break;
            case SINGLE_DELETE:
                deleteArticle((int) data);
                break;
        }
        return true;
    }

    // operations
    public void addArticle() {
        ModalDialog.show(this, Mode.ADD_ONCE, Messages.getString("ViewArticles.9"), new FrameArticle(),
                new Article(), articleDao, navigation);
    }

    public void editArticle(int id) {
        Article article = articleDao.getArticleById(id);
        if (article != null) {
            ModalDialog.show(this, Mode.EDIT, Messages.getString("ViewArticles.10"), new FrameArticle(),
                    article, articleDao, navigation);
        }
    }

    public void deleteArticle(int id) {
        table.deleteRow(id, id, articleDao, this, navigation, Columns.CAPTION);
    }

    // Auxiliary
    /** Generuje textovy popis umisteni clanku na strankach */
    public String GetArticleLocationAsString(Article article) {
        String locationStr = article.getLocationType().toString();

        switch (article.getOwnerType()) {
            case CLUB_ALL:
                locationStr += ", " + Messages.getString("ViewArticles.1"); //$NON-NLS-1$ //$NON-NLS-2$
                break;
            case CLUB:
                locationStr += ", " + Messages.getString("ViewArticles.3"); //$NON-NLS-1$ //$NON-NLS-2$
                break;
            case CATEGORY:
                Category category = categoryDao.getCategory(article.getCategoryId());
                if (category != null) {
                    locationStr += ", " + category.getDescription(); //$NON-NLS-1$
                }
                break;
            case TEAM:
                ClubTeam clubTeam = clubTeamDao.getClubTeamById(article.getClubTeamId());
                if (clubTeam != null) {
                    locationStr += ", " + clubTeam.getName(); //$NON-NLS-1$
                }
                break;
        }

        if (article.getPriority()) {
            locationStr += " (!)"; //$NON-NLS-1$
        }
        return locationStr;
    }

    public static String getExpirationInfoAsString(Article article) {
        String expirationStr = ""; //$NON-NLS-1$
        if (article.getExpirationDate() != null) {
            DateTime.dateToString(article.getExpirationDate(), DateStyle.DAY);
        }
        return expirationStr;
    }

}
