package com.clubeek.ui.views;

import java.util.List;

import com.clubeek.dao.TeamMemberDao;
import com.clubeek.dao.impl.ownframework.TeamMemberDaoImpl;
import com.clubeek.enums.TeamFunction;
import com.clubeek.model.TeamMember;
import com.clubeek.ui.Tools;
import com.clubeek.ui.components.TeamMemberPanel;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.event.MouseEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * View for team members for tab panel named "Team".
 *
 * @author marek
 * @author lukas
 */
@SuppressWarnings("serial")
@PreserveOnRefresh
public class ViewTeamRoster extends VerticalLayout implements View {
    // TODO vitfo, created on 11. 6. 2015 
    private TeamMemberDao teamMemberDao = new TeamMemberDaoImpl();

    private final String cssTeamMembersList = "teamMembersList"; //$NON-NLS-1$

    /** Navigation administrator */
    private final Navigation navigation;

    // management upper layout
    private final CssLayout layoutManagement;

    // players bottom layout
    private final CssLayout layoutPlayers;

    /**
     * Adds caption label for team members layout.
     *
     * @param text text for label
     */
    private void addMembersLabel(String text) {
        Label label = new Label(text);
        label.setStyleName(ValoTheme.LABEL_H2);
        //label.addStyleName(cssTeamMembersList);
        this.addComponent(label);
    }

    /**
     * Adds team members into given layout.
     *
     * @param members list of team members for adding to layout
     * @param teamFunctions array of team positions specific for layout
     * @param layout layout for member info panels
     */
    private void addMembers(List<TeamMember> members, TeamFunction[] teamFunctions, CssLayout layout) {

        layout.removeAllComponents();
        for (TeamFunction f : teamFunctions) {
            for (final TeamMember m : members) {
                if (m.isFunction(f) && (m.getClubMember() != null)) {

                    TeamMemberPanel memberPanel = new TeamMemberPanel();
                    memberPanel.setTeamMember(m);
                    memberPanel.addClickListener(new MouseEvents.ClickListener() {

                        @Override
                        public void click(MouseEvents.ClickEvent event) {
                            navigation.navigateTo(Navigation.ViewId.CLUB_MEMBER_CARD, Integer.toString(m.getClubMemberId()));
                        }
                    });

                    layout.addComponent(memberPanel);
                }
            }
        }
    }

    public ViewTeamRoster(Navigation navigation) {
        this.navigation = navigation;
        this.setCaption(Messages.getString("team"));

        addMembersLabel(Messages.getString("SupportTeam"));
                
        layoutManagement = new CssLayout();
        layoutManagement.setStyleName(cssTeamMembersList);
        this.addComponent(layoutManagement);
        
        addMembersLabel(Messages.getString("players"));

        layoutPlayers = new CssLayout();
        layoutPlayers.setStyleName(cssTeamMembersList);
        this.addComponent(layoutPlayers);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        int teamId = Tools.Strings.analyzeParameters(event);

        layoutManagement.removeAllComponents();
        layoutPlayers.removeAllComponents();

        if (teamId >= 0) {
            // load all team members
//            List<TeamMember> members = RepTeamMember.selectByTeamId(teamId, null);
            List<TeamMember> members = teamMemberDao.getTeamMembersByTeamId(teamId);
            // divide members to groups for layouts
            if (members != null) {
                addMembers(members, new TeamFunction[]{TeamFunction.COACH, TeamFunction.COACH_ASSISTANT,
                    TeamFunction.TEAM_LEADERSHIP}, layoutManagement);
                addMembers(members, new TeamFunction[]{TeamFunction.PLAYER}, layoutPlayers);
            }
        }
    }

}
