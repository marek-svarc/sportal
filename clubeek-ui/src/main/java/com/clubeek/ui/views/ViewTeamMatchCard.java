package com.clubeek.ui.views;

import com.clubeek.db.RepTeamMatch;
import com.clubeek.ui.Tools;
import com.clubeek.ui.components.TeamMatchCard;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;

/**
 * View container for TeamMatchCard. Shows informations about team match.
 *
 * @author Marek Svarc
 */
public class ViewTeamMatchCard extends VerticalLayout implements View {

    /** Component that shows club member informations */
    private final TeamMatchCard card;

    public ViewTeamMatchCard(Navigation navigation) {

        card = new TeamMatchCard(navigation);
        card.setSizeFull();
        this.addComponent(card);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

        int teamMatchId = 0;
        if (event != null) {
            teamMatchId = Tools.Strings.analyzeParameters(event);
        }

        if (teamMatchId > 0) {
            card.setTeamMatch(RepTeamMatch.selectById(teamMatchId, null));
        }else{
            card.setTeamMatch(null);
        }
    }

}
