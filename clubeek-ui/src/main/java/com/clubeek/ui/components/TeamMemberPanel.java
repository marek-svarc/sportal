package com.clubeek.ui.components;

import com.clubeek.enums.TeamFunctionType;
import com.clubeek.model.ClubMember;
import com.clubeek.model.TeamMember;
import com.clubeek.ui.Tools;
import com.clubeek.ui.views.Messages;
import com.clubeek.util.DateTime;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Panel with name and photo of club member. Click on panel can fire event.
 *
 * @author lukas
 */
public class TeamMemberPanel extends Panel {

    /** Main layout */
    private final VerticalLayout vlaMain;

    /** Shows member photo */
    private final Image imgPhoto;

    /** Shows informations about member */
    private final Label lblInfo;

    public TeamMemberPanel() {
        this.setStyleName(ValoTheme.PANEL_WELL);
        this.addStyleName("detail");

        vlaMain = new VerticalLayout();
        vlaMain.setSizeFull();

        imgPhoto = new Image();
        imgPhoto.setWidth(100, Unit.PIXELS);
        imgPhoto.setHeight(100, Unit.PIXELS);
        vlaMain.addComponent(imgPhoto);

        lblInfo = new Label();
        vlaMain.addComponent(lblInfo);
        setContent(vlaMain);
    }

    /**
     * Sets club member into panel.
     *
     * @param teamMember team member
     */
    public void setTeamMember(TeamMember teamMember) {
        ClubMember clubMember = teamMember.getClubMember();

        setCaption(String.format("%s %s", clubMember.getName(), clubMember.getSurname()));
        if (clubMember.getPhoto() != null) {
            Tools.Components.fillImageByPortrait(imgPhoto, clubMember.getPhoto(),
                    Integer.toString(clubMember.getId()));
        }
        if (!teamMember.isFunction(TeamFunctionType.PLAYER)) {
            lblInfo.setValue(teamMember.getFunctionsAsList().toString());
        } else if (clubMember.getBirthdate() != null) {
            lblInfo.setValue(String.format("%s %s", Messages.getString("yearGroup"),
                    DateTime.dateToString(clubMember.getBirthdate(), DateTime.DateStyle.YEAR)));
        }
    }
}
