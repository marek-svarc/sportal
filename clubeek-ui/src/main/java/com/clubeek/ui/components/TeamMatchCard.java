package com.clubeek.ui.components;

import com.clubeek.model.TeamMatch;
import com.clubeek.ui.views.Navigation;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Component that shows information about one match.
 *
 * @author Marek Svarc
 */
public class TeamMatchCard extends VerticalLayout {

    /** Informations about match */
    private TeamMatch teamMatch;

    /** Shows logo of the home team */
    private final Image imgTeamHomeLogo;

    /** Shows logo of the visiting team */
    private final Image imgTeamVisitingLogo;

    /** Shows title of the home team */
    private final Label lblTeamHomeTitle;

    /** Shows title of the visiting team */
    private final Label lblTeamVisitingTitle;

    /** Shows score of the match */
    private final Label lblMatchScore;

    /** Shows details about match */
    private final FormatedLabel lblMatchInfo;

    private void Refresh() {
        if ((teamMatch != null) && (teamMatch.getClubTeam() != null) && (teamMatch.getClubRival() != null)) {
            if (teamMatch.getHomeCourt()) {
                lblTeamHomeTitle.setValue(teamMatch.getClubTeam().getName());
                lblTeamVisitingTitle.setValue(teamMatch.getClubRivalTitle());
            } else {
                lblTeamHomeTitle.setValue(teamMatch.getClubRivalTitle());
                lblTeamVisitingTitle.setValue(teamMatch.getClubTeam().getName());
            }
        } else {
            lblTeamHomeTitle.setValue(null);
            lblTeamVisitingTitle.setValue(null);
        }
    }

    public TeamMatchCard(Navigation navigation) {

        HorizontalLayout layoutTop = new HorizontalLayout();
        this.addComponent(layoutTop);

        imgTeamHomeLogo = new Image();
        imgTeamVisitingLogo = new Image();
        lblTeamHomeTitle = new Label();
        lblTeamVisitingTitle = new Label();
        lblMatchScore = new Label();
        layoutTop.addComponents(imgTeamHomeLogo, lblTeamHomeTitle, lblMatchScore,
                lblTeamVisitingTitle, imgTeamVisitingLogo);

        lblMatchInfo = new FormatedLabel();
        this.addComponent(lblMatchInfo);
    }

    public void setTeamMatch(TeamMatch teamMatch) {
        if (this.teamMatch != teamMatch) {
            this.teamMatch = teamMatch;
            Refresh();
        }
    }

}
