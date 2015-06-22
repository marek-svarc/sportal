package com.clubeek.ui.views;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.vaadin.risto.stylecalendar.DateOptionsGenerator;
import org.vaadin.risto.stylecalendar.StyleCalendar;

import com.clubeek.dao.ArticleDao;
import com.clubeek.dao.ClubTeamDao;
import com.clubeek.dao.impl.ownframework.ArticleDaoImpl;
import com.clubeek.dao.impl.ownframework.ClubTeamDaoImpl;
import com.clubeek.dao.impl.ownframework.rep.RepTeamMatch;
import com.clubeek.dao.impl.ownframework.rep.RepTeamTraining;
import com.clubeek.enums.Location;
import com.clubeek.model.Article;
import com.clubeek.model.ClubTeam;
import com.clubeek.model.TeamMatch;
import com.clubeek.model.TeamTraining;
import com.clubeek.ui.PublishableArticle;
import com.clubeek.ui.Tools;
import com.clubeek.ui.views.Navigation.ViewId;
import com.clubeek.util.DateTime;
import com.clubeek.util.DateTime.DateStyle;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Trida pro zobrazeni clanku a pripravovanych akci
 *
 * @author Marek Svarc
 *
 */
@SuppressWarnings("serial")
public class ViewNews extends VerticalLayout implements View {
    // TODO vitfo, created on 11. 6. 2015 
    private ArticleDao articleDao = new ArticleDaoImpl();
    // TODO vitfo, created on 11. 6. 2015 
    private ClubTeamDao clubTeamDao = new ClubTeamDaoImpl();

    /* PRIVATE */
    private Navigation navigation;

    /** Panel pro zobrazeni clanku na leve strane stranky */
    private VerticalLayout vlLeftPanel;

    /** Panel pro zobrazeni clanku ve stredu stranky */
    private VerticalLayout vlCenterPanel;

    /** Panel pro zobrazeni planovanych akci tymu */
    private VerticalLayout vlRightPanel;

    /** Panel pro zobrazeni planovanych akci tymu */
    private VerticalLayout vlTeamInfo;

    /** Panel pro zobrazeni planovanych akci klubu */
    private VerticalLayout vlClubInfo;

    /** Kalendar pro zobrazeni planovanych akci */
    private StyleCalendar calendar;

    /** Vypis nejblizsiho treninku */
    private Label laEarliestTraining;

    /** Vypis nejblizsi hry */
    private Label laEarliestMatch;

    /** Vypis nejblizsich domacich zapasu */
    private Label laHomeMatches;

    /** Seznam treninku pro jeden tym */
    private List<TeamTraining> trainings = null;

    /** Seznam zapasu pro jeden tym */
    private List<TeamMatch> games = null;

    /** Vytvori komponentu pro zobrazeni jednoho clanku */
    private VerticalLayout createNewsLayout(final PublishableArticle article, boolean showHeader, boolean showActiveColor,
            String layoutStyle, String captionStyle, String summaryStyle) {

        boolean activeLink = (article.getArticle().getContent() != null) && !article.getArticle().getContent().isEmpty();

        // oblast clanku
        VerticalLayout newsLayout = new VerticalLayout();
        if (article.getArticle().getPriority() && showActiveColor) {
            newsLayout.addStyleName("priority"); //$NON-NLS-1$
        }
        if (layoutStyle != null) {
            newsLayout.addStyleName(layoutStyle);
        }
        if (activeLink) {
            newsLayout.addStyleName("m-selectable"); //$NON-NLS-1$
            newsLayout.addLayoutClickListener(new LayoutClickListener() {

                @Override
                public void layoutClick(LayoutClickEvent event) {
                    navigation.navigateTo(article.getView(), Integer.toString(article.getId()));
                }
            });
        }

        // hlavicka clanku
        if (showHeader) {
            Label laHeader = new Label(article.getArticle().getDescription());
            laHeader.addStyleName("label-h6"); //$NON-NLS-1$
            Label laDate = new Label(DateTime.dateToString(article.getArticle().getLastChangeDate(),
                    DateStyle.SHORT_DAY_AND_TIME), ContentMode.TEXT);
            laDate.setStyleName("label-h6 label-rightAlign"); //$NON-NLS-1$
            HorizontalLayout hlHeader = Tools.Components.createHorizontalLayout(null, laHeader, laDate);
            hlHeader.setSizeFull();
            hlHeader.setMargin(false);
            hlHeader.setSpacing(false);
            newsLayout.addComponent(hlHeader);
        }

        // nadpis clanku
        Label laCaption = new Label(article.getArticle().getCaption());
        if (captionStyle != null) {
            laCaption.addStyleName(captionStyle);
        }
        if (activeLink) {
            laCaption.setValue(article.getArticle().getCaption() + " ..."); //$NON-NLS-1$
            //laCaption.addStyleName("selectable-caption"); //$NON-NLS-1$
        } else {
            laCaption.setValue(article.getArticle().getCaption());
        }
        newsLayout.addComponent(laCaption);

        // obsah clanku
        if ((article.getArticle().getSummary() != null) && !article.getArticle().getSummary().isEmpty()) {
            Label laSummary = new Label(article.getArticle().getSummary(), ContentMode.HTML);
            if (summaryStyle != null) {
                laSummary.setStyleName(summaryStyle);
            }
            newsLayout.addComponent(laSummary);
        }

        return newsLayout;
    }

    private void createNewsLayoutList(List<PublishableArticle> container, AbstractLayout owner, boolean showCreationTime,
            boolean showActiveColor, String layoutStyle, String captionStyle, String summaryStyle) {
        PublishableArticle.sortArticlesByLastChange(container, false);
        for (PublishableArticle item : container) {
            owner.addComponent(createNewsLayout(item, showCreationTime, showActiveColor, layoutStyle, captionStyle, summaryStyle));
        }
    }

    /**
     * Vlozi do oblasti Label pro vypsani nadpisu a Label pro vypsani textu
     *
     * @param parent nadrazena oblast
     * @param title nadpis
     * @return Label pro vypsani textu
     */
    private Label createHtmlLabel(VerticalLayout parent, String title, boolean addBtmBorder) {

        Label laTitle = new Label(title); //$NON-NLS-1$
        laTitle.setStyleName("label-h3"); //$NON-NLS-1$
        if (addBtmBorder) {
            laTitle.addStyleName("btmborder"); //$NON-NLS-1$
        }
        parent.addComponent(laTitle);

        Label laText = new Label();
        laText.setContentMode(ContentMode.HTML);
        laText.setStyleName("label-h6"); //$NON-NLS-1$
        parent.addComponent(laText);

        return laText;
    }

    private List<Article> getArticles(ClubTeam team, Location location) {
        if (team != null) {
//            return RepArticle.select(team.getId(), team.getCategoryId(), location, null);
            return articleDao.selectArticles(team.getId(), team.getCategoryId(), location);
        } else {
//            return RepArticle.select(0, 0, location, null);
            return articleDao.selectArticles(0, 0, location);
        }
    }

    /* PUBLIC */
    public ViewNews(Navigation navigation) {
        this.navigation = navigation;
        this.setCaption(Messages.getString("currently")); //$NON-NLS-1$

        HorizontalLayout laMain = new HorizontalLayout();
        laMain.setSizeFull();

        Label label;

        // leva cast, dulezite informace
        vlLeftPanel = new VerticalLayout();
        vlLeftPanel.setStyleName("layout-container"); //$NON-NLS-1$
        vlLeftPanel.setHeight(100, Unit.PERCENTAGE);
        laMain.addComponent(vlLeftPanel);
        laMain.setExpandRatio(vlLeftPanel, 2.0f);

        // stredni cast, aktuality
        vlCenterPanel = new VerticalLayout();
        vlCenterPanel.setStyleName("layout-container"); //$NON-NLS-1$
        laMain.addComponent(vlCenterPanel);
        laMain.setExpandRatio(vlCenterPanel, 5.0f);

        // prava cast, zobrazeni pripravovanych akci
        vlRightPanel = new VerticalLayout();
        vlRightPanel.setStyleName("layout-container"); //$NON-NLS-1$
        vlRightPanel.setWidth(240, Unit.PIXELS);
        laMain.addComponent(vlRightPanel);
		//laMain.setExpandRatio(vlRightPanel, 2.0f);

        // panel pripravovanych akci tymu
        vlTeamInfo = new VerticalLayout();
        vlTeamInfo.setSizeFull();
        vlRightPanel.addComponent(vlTeamInfo);

        label = new Label(Messages.getString("eventCalendar")); //$NON-NLS-1$
        label.setStyleName("label-h3"); //$NON-NLS-1$
        vlTeamInfo.addComponent(label);

        calendar = new StyleCalendar();
        calendar.setReadOnly(true);
        calendar.setSizeFull();
        calendar.setDateOptionsGenerator(new DateOptionsGenerator() {

            @Override
            public boolean isDateDisabled(Date date, StyleCalendar context) {
                return false;
            }

            @Override
            public String getTooltip(Date date, StyleCalendar context) {
                String tooltip = ""; //$NON-NLS-1$

                // trenink
                TeamTraining training = DateTime.getEvent(date, trainings);
                if (training != null) {
                    tooltip += String
                            .format("<strong>%s</strong></br>%s</br>", Messages.getString("training"), Tools.Strings.getHtmlTraining(training)); //$NON-NLS-1$ //$NON-NLS-2$
                }
                // zapas
                TeamMatch game = DateTime.getEvent(date, games);
                if (game != null) {
                    tooltip += String.format(
                            "<strong>%s</strong></br>%s</br>", Messages.getString("match"), Tools.Strings.getHtmlGame(game)); //$NON-NLS-1$ //$NON-NLS-2$
                }
                return tooltip == "" ? null : tooltip; //$NON-NLS-1$
            }

            @Override
            public String getStyleName(Date date, StyleCalendar context) {

                TeamMatch game = DateTime.getEvent(date, games);
                if (game != null) {
                    return "event-game"; //$NON-NLS-1$
                }
                TeamTraining training = DateTime.getEvent(date, trainings);
                if (training != null) {
                    return "event-training"; //$NON-NLS-1$
                }
                return null;
            }
        });
        vlTeamInfo.addComponent(calendar);

        laEarliestTraining = createHtmlLabel(vlTeamInfo, Messages.getString("earliestTraining"), true); //$NON-NLS-1$
        laEarliestMatch = createHtmlLabel(vlTeamInfo, Messages.getString("earliestMatch"), true); //$NON-NLS-1$

        // panel pripravovanych akci tymu
        vlClubInfo = new VerticalLayout();
        vlClubInfo.setSizeFull();
        vlRightPanel.addComponent(vlClubInfo);

        laHomeMatches = createHtmlLabel(vlClubInfo, Messages.getString("ViewNews.7"), true); //$NON-NLS-1$

        this.addComponent(laMain);
    }

    @Override
    public void enter(ViewChangeEvent event) {

        vlLeftPanel.removeAllComponents();
        vlCenterPanel.removeAllComponents();

        int teamId = 0;
        if (event != null) {
            teamId = Tools.Strings.analyzeParameters(event);
        }

        trainings = null;
        games = null;

        ClubTeam team = null;
        if (teamId > 0) {
//            team = RepClubTeam.selectById(teamId, new RepClubTeam.TableColumn[]{TableColumn.ID, TableColumn.CATEGORY_ID});
            team = clubTeamDao.getClubTeamById(teamId);
        }
        List<PublishableArticle> boardArticles = new ArrayList<>();
        PublishableArticle.addArticlesToContainer(boardArticles, getArticles(team, Location.BULLETIN_BOARD), ViewId.ARTICLE);
        createNewsLayoutList(boardArticles, vlLeftPanel, false, true, "layout-board", "label-h3", "label-h5");
        List<PublishableArticle> listArticles = new ArrayList<>();
        PublishableArticle.addArticlesToContainer(listArticles, getArticles(team, Location.NEWS), ViewId.ARTICLE);
        if (team != null) {
            PublishableArticle.addArticlesToContainer(listArticles, RepTeamMatch.selectPublishable(team.getId(), null),
                    ViewId.ARTICLE);
        } else {
//            List<ClubTeam> teams = RepClubTeam.select(true, new RepClubTeam.TableColumn[]{RepClubTeam.TableColumn.ID});
            List<ClubTeam> teams = clubTeamDao.getActiveClubTeams();
            for (ClubTeam item : teams) {
                PublishableArticle.addArticlesToContainer(listArticles, RepTeamMatch.selectPublishable(item.getId(), null),
                        ViewId.ARTICLE);
            }
        }
        createNewsLayoutList(listArticles, vlCenterPanel, true, false, "layout-list", "label-h2", "label-h5");
        vlTeamInfo.setVisible(team != null);
        vlClubInfo.setVisible(team == null);
        if (team != null) {

            // zobrazeni nejblizsiho treninku
            trainings = RepTeamTraining.selectByClubTeamId(teamId, null);
            if (trainings != null) {
                TeamTraining earliestTraining = DateTime.getEarliestEvent(trainings);
                if (earliestTraining != null) {
                    laEarliestTraining.setValue(Tools.Strings.getHtmlTraining(earliestTraining));
                } else {
                    laEarliestTraining.setValue(""); //$NON-NLS-1$
                }
            }

            // zobrazeni nejblizsiho zapasu
            games = RepTeamMatch.selectByTeamId(teamId, null);
            if (games != null) {
                TeamMatch earliestGame = DateTime.getEarliestEvent(games);
                if (earliestGame != null) {
                    laEarliestMatch.setValue(Tools.Strings.getHtmlGame(earliestGame));
                } else {
                    laEarliestMatch.setValue(""); //$NON-NLS-1$
                }
            }
        } else {
            // nacteni nejblizscih domacich zapasu z databaze
            List<TeamMatch> homeMatches = RepTeamMatch.selectHomeMatches(6, null);
            // vypis nejblizscih domacich zapasu z databaze
            String text = ""; //$NON-NLS-1$
            for (int i = 0; i < Math.min(8, homeMatches.size()); ++i) {
                text += "<br>" + Tools.Strings.getHtmlGame(homeMatches.get(i)) + "<br>"; //$NON-NLS-1$ //$NON-NLS-2$
                if (i > 5) {
                    break;
                }
            }
            laHomeMatches.setValue(text);
        }

        calendar.markAsDirty();
    }
}
