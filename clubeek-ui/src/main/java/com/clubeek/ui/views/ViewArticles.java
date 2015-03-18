package com.clubeek.ui.views;

import java.sql.SQLException;
import java.util.List;

import com.clubeek.db.RepArticle;
import com.clubeek.db.RepCategory;
import com.clubeek.db.RepClubTeam;
import com.clubeek.model.Article;
import com.clubeek.model.Category;
import com.clubeek.model.ClubTeam;
import com.clubeek.model.User.Role;
import com.clubeek.ui.ModalDialog;
import com.clubeek.ui.Security;
import com.clubeek.ui.Tools;
import com.clubeek.ui.ModalDialog.Mode;
import com.clubeek.ui.Tools.DateTime.DateStyle;
import com.clubeek.ui.components.ActionTable;
import com.clubeek.ui.frames.FrameArticle;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ViewArticles extends VerticalLayout implements View, ActionTable.OnActionListener {

    /* PUBLIC */
    public enum Columns {

        CAPTION, LOCATION, LAST_UPDATE, EXPIRATION_DATE;
    }

    public ViewArticles(Navigation navigation) {
        this.navigation = navigation;
        this.setSizeFull();

        // view caption
        this.setCaption(Messages.getString("articles"));

        // columns definition
        ActionTable.UserColumnInfo[] columns = {
            new ActionTable.UserColumnInfo(Columns.CAPTION, String.class, Messages.getString("caption")),
            new ActionTable.UserColumnInfo(Columns.LOCATION, String.class, Messages.getString("ViewArticles.8")),
            new ActionTable.UserColumnInfo(Columns.LAST_UPDATE, String.class, Messages.getString("lastUpdate")),
            new ActionTable.UserColumnInfo(Columns.EXPIRATION_DATE, String.class, Messages.getString("expirationDate")),};

        // create and place table
        table = new ActionTable(ActionTable.Action.getStandardSet(), columns, this);
        table.addToOwner(this);
    }

    // interface View
    @Override
    public void enter(ViewChangeEvent event) {

        Security.authorize(Role.EDITOR);

        try {
            List<Article> articles = RepArticle.selectAll(null);

            table.removeAllRows();
            for (int i = 0; i < articles.size(); ++i) {
                Article article = articles.get(i);
                table.addRow(new Object[]{article.getCaption(), GetArticleLocationAsString(article),
                    Tools.DateTime.dateToString(article.getCreationDate(), DateStyle.DAY_AND_TIME),
                    getExpirationInfoAsString(article)}, article.getId());
            }
        } catch (SQLException e) {
            Tools.msgBoxSQLException(e);
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
                new Article(), RepArticle.getInstance(), navigation);
    }

    public void editArticle(int id) {
        try {
            Article article = RepArticle.selectById(id, null);
            if (article != null) {
                ModalDialog.show(this, Mode.EDIT, Messages.getString("ViewArticles.10"), new FrameArticle(),
                        article, RepArticle.getInstance(), navigation);
            }
        } catch (SQLException e) {
            Tools.msgBoxSQLException(e);
        }
    }

    public void deleteArticle(int id) {
        table.deleteRow(id, RepArticle.getInstance(), this, navigation);
    }
    
    // Auxiliary
    /** Generuje textovy popis umisteni clanku na strankach */
    public static String GetArticleLocationAsString(Article article) {
        String locationStr = article.getLocation().toString();

        try {
            switch (article.getOwner()) {
                case CLUB_ALL:
                    locationStr += ", " + Messages.getString("ViewArticles.1"); //$NON-NLS-1$ //$NON-NLS-2$
                    break;
                case CLUB:
                    locationStr += ", " + Messages.getString("ViewArticles.3"); //$NON-NLS-1$ //$NON-NLS-2$
                    break;
                case CATEGORY:
                    Category category = RepCategory.selectById(article.getCategoryId(),
                            new RepCategory.TableColumn[]{RepCategory.TableColumn.DESCRIPTION});
                    if (category != null) {
                        locationStr += ", " + category.getDescription(); //$NON-NLS-1$
                    }
                    break;
                case TEAM:
                    ClubTeam clubTeam = RepClubTeam.selectById(article.getClubTeamId(),
                            new RepClubTeam.TableColumn[]{RepClubTeam.TableColumn.NAME});
                    if (clubTeam != null) {
                        locationStr += ", " + clubTeam.getName(); //$NON-NLS-1$
                    }
                    break;
            }
        } catch (SQLException e) {
            Tools.msgBoxSQLException(e);
        }

        if (article.getPriority()) {
            locationStr += " (!)"; //$NON-NLS-1$
        }
        return locationStr;
    }

    public static String getExpirationInfoAsString(Article article) {
        String expirationStr = ""; //$NON-NLS-1$
        if (article.getExpirationDate() != null) {
            expirationStr = Tools.DateTime.dateToString(article.getExpirationDate(), DateStyle.DAY);
        }
        return expirationStr;
    }

    /* PRIVATE */
    /** Rozhrani pro navigaci webem a aktualizaci webu */
    private final Navigation navigation;

    /** Komponenty tabulky */
    private final ActionTable table;

}
