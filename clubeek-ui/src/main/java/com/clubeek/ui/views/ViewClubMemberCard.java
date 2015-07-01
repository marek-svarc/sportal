package com.clubeek.ui.views;

import com.clubeek.dao.ClubMemberDao;
import com.clubeek.ui.Tools;
import com.clubeek.ui.components.ClubMemberCard;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * View that shows all informations about one member of the club
 *
 * @author Marek Svarc
 */
@Component
@Scope("prototype")
public class ViewClubMemberCard extends VerticalLayout implements View {
    // TODO vitfo, created on 11. 6. 2015
    @Autowired
    private ClubMemberDao clubMemberDao;

    /** Component that shows club member informations */
    private ClubMemberCard info;
    
    public ViewClubMemberCard() {
        info = new ClubMemberCard();
        info.setSizeFull();
        this.addComponent(info);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

        int clubMemberId = 0;
        if (event != null) {
            clubMemberId = Tools.Strings.analyzeParameters(event);
        }

        if (clubMemberId > 0) {
            info.setClubMember(clubMemberDao.getClubMember(clubMemberId));
        }
    }

}
