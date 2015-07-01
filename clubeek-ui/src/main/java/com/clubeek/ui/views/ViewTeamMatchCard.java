package com.clubeek.ui.views;

import com.clubeek.dao.impl.ownframework.rep.RepTeamMatch;
import com.clubeek.ui.Tools;
import com.clubeek.ui.components.TeamMatchCard;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
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

    /** Component that shows club member informations */
    private TeamMatchCard card;
    
    @Autowired
    private Navigation navigation;

    public ViewTeamMatchCard() {

    }
    
    @PostConstruct
    public void init() {
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
