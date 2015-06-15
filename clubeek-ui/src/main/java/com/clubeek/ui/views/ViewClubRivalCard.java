package com.clubeek.ui.views;

import com.clubeek.dao.ClubRivalDao;
import com.clubeek.dao.impl.ownframework.ClubRivalDaoImpl;
import com.clubeek.dao.impl.ownframework.rep.RepClubRival;
import com.clubeek.ui.Tools;
import com.clubeek.ui.components.ClubRivalCard;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;

/**
 * View container for ClubRivalCard. Shows informations about club.
 *
 * @author Marek Svarc
 */
public class ViewClubRivalCard extends VerticalLayout implements View {
    // TODO vitfo, created on 11. 6. 2015 
    private ClubRivalDao clubRivalDao = new ClubRivalDaoImpl();

    /** Component that shows club member informations */
    private final ClubRivalCard card;

    public ViewClubRivalCard(Navigation navigation) {

        card = new ClubRivalCard(navigation);
        card.setSizeFull();
        this.addComponent(card);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

        int clubRivalId = 0;
        if (event != null) {
            clubRivalId = Tools.Strings.analyzeParameters(event);
        }

        if (clubRivalId > 0) {
//            card.setClubRival(RepClubRival.selectById(clubRivalId, null));
            card.setClubRival(clubRivalDao.getClubRivalById(clubRivalId));
        }else{
            card.setClubRival(null);
        }
    }

}
