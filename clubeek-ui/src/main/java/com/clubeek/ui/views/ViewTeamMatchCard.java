package com.clubeek.ui.views;

import com.clubeek.dao.impl.ownframework.rep.RepTeamMatch;
import com.clubeek.ui.Tools;
import com.clubeek.ui.components.TeamMatchCard;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * View container for TeamMatchCard. Shows informations about team match.
 *
 * @author Marek Svarc
 */
@Component
@Scope("prototype")
public class ViewTeamMatchCard extends VerticalLayout implements View {

    /* PRIVATE */
    /** Application navigation provider */
    private Navigation navigation;

    /** Component that shows club member informations */
    private final TeamMatchCard card;

    /* PUBLIC */
    public ViewTeamMatchCard() {
        card = new TeamMatchCard();
        card.setSizeFull();
        this.addComponent(card);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.navigation = (Navigation) getUI().getContent();

        int teamMatchId = 0;
        if (event != null) {
            teamMatchId = Tools.Strings.analyzeParameters(event);
        }

        if (teamMatchId > 0) {
            card.setTeamMatch(this.navigation, RepTeamMatch.selectById(teamMatchId, null));
        } else {
            card.setTeamMatch(this.navigation, null);
        }
    }

}
