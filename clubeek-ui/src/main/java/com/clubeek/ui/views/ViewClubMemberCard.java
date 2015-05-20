package com.clubeek.ui.views;

import com.clubeek.db.RepClubMember;
import com.clubeek.ui.Tools;
import com.clubeek.ui.components.ClubMemberCard;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;

/**
 * View that shows all informations about one member of the club
 *
 * @author Marek Svarc
 */
public class ViewClubMemberCard extends VerticalLayout implements View {

    /** Aministrator of the application navigation */
    private final Navigation navigation;

    /** Component that shows club member informations */
    private final ClubMemberCard info;

    public ViewClubMemberCard(Navigation navigation) {
        this.navigation = navigation;

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
            info.setClubMember(RepClubMember.selectById(clubMemberId, null));
        }
    }

}
