package com.clubeek.ui.views;

import com.clubeek.dao.ClubRivalDao;
import com.clubeek.ui.Tools;
import com.clubeek.ui.components.ClubRivalCard;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * View container for ClubRivalCard. Shows informations about club.
 *
 * @author Marek Svarc
 */
@Component
@Scope("prototype")
public class ViewClubRivalCard extends VerticalLayout implements View {

    /* PRIVATE */
    @Autowired
    private ClubRivalDao clubRivalDao;

    /** Component that shows club member informations */
    private final ClubRivalCard card;

    /** Application navigation provider */
    private Navigation navigation;

    /* PUBLIC */
    public ViewClubRivalCard() {
        card = new ClubRivalCard();
        card.setSizeFull();
        this.addComponent(card);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.navigation = (Navigation) getUI().getContent();

        int clubRivalId = 0;
        if (event != null) {
            clubRivalId = Tools.Strings.analyzeParameters(event);
        }

        if (clubRivalId > 0) {
            card.update(this.navigation, clubRivalDao.getClubRivalById(clubRivalId));
        } else {
            card.update(this.navigation, null);
        }
    }

}
