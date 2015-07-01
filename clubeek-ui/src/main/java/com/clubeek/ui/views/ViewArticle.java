package com.clubeek.ui.views;

import com.clubeek.dao.ArticleDao;
import com.clubeek.dao.impl.ownframework.ArticleDaoImpl;
import com.clubeek.model.Article;
import com.clubeek.ui.Tools;
import com.clubeek.util.DateTime;
import com.clubeek.util.DateTime.DateStyle;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@SuppressWarnings("serial")
@Theme("baseTheme")
@Component
@Scope("prototype")
public class ViewArticle extends VerticalLayout implements View {
    // TODO vitfo, created on 11. 6. 2015
    @Autowired
    private ArticleDao articleDao;

    /* PRIVATE */
    /** Label pro vypis nadpisu clanku */
    final private Label laCaption;

    /** Label pro vypis data posledni upravy */
    final private Label laCreationDate;

    /** Label pro vypis obsahu clanku */
    final private Label laContent;

    /* PUBLIC */
    public ViewArticle() {
        this.setStyleName("layout-container"); //$NON-NLS-1$

        laCaption = new Label();
        laCaption.setContentMode(ContentMode.TEXT);
        laCaption.addStyleName("label-h2"); //$NON-NLS-1$

        laCreationDate = new Label();
        laCreationDate.setContentMode(ContentMode.TEXT);
        laCreationDate.addStyleName("label-h4"); //$NON-NLS-1$
        laCreationDate.addStyleName("label-rightAlign"); //$NON-NLS-1$

        laContent = new Label();
        laContent.setSizeFull();
        laContent.setContentMode(ContentMode.HTML);
        laContent.addStyleName("label-h4"); //$NON-NLS-1$

        HorizontalLayout hlLayoutTop = new HorizontalLayout(laCaption, laCreationDate);
        hlLayoutTop.setSizeFull();
        this.addComponent(hlLayoutTop);
        this.addComponent(laContent);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        int articleId = 0;
        if (event != null) {
            articleId = Tools.Strings.analyzeParameters(event);
        }

        if (articleId > 0) {
            // Article article = RepArticle.selectById(articleId, new RepArticle.TableColumn[]{RepArticle.TableColumn.CAPTION,
            // RepArticle.TableColumn.CONTENT, RepArticle.TableColumn.CREATION_DATE});
            Article article = articleDao.getArticleById(articleId);
            laCaption.setValue(article.getCaption());
            laCreationDate.setValue(String.format("%s %s", Messages.getString("ViewArticle.6"),
                    DateTime.dateToString(article.getCreationDate(), DateStyle.SHORT_DAY)));
            laContent.setValue(article.getContent());
        }
    }
}
