package com.clubeek.ui.views;

import java.util.List;

import com.clubeek.dao.TeamMemberDao;
import com.clubeek.enums.TeamFunctionType;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * View for team members for tab panel named "Team".
 *
 * @author Marek Svarc
 * @author Lukas Janacek
 */
@SuppressWarnings("serial")
@PreserveOnRefresh
@Component
@Scope("prototype")
public class ViewTeamRoster extends VerticalLayout implements View {
    
    /* PRIVATE */
    @Autowired
    private TeamMemberDao teamMemberDao;

    /** Application navigation provider */
    private Navigation navigation;

    /** Management upper layout */
    private final CssLayout layoutManagement;

    /** Players bottom layout */
    private final CssLayout layoutPlayers;

    private final String cssTeamMembersList = "teamMembersList"; //$NON-NLS-1$
    
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
    private void addMembers(List<TeamMember> members, TeamFunctionType[] teamFunctions, CssLayout layout) {

        layout.removeAllComponents();
        for (TeamFunctionType f : teamFunctions) {
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

    public ViewTeamRoster() {
        setSizeFull();
        setSpacing(true);
        setMargin(true);
        
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
        this.navigation = (Navigation) getUI().getContent();
        
        int teamId = Tools.Strings.analyzeParameters(event);

        layoutManagement.removeAllComponents();
        layoutPlayers.removeAllComponents();

        if (teamId >= 0) {
            List<TeamMember> members = teamMemberDao.getTeamMembersByTeamId(teamId);
            // divide members to groups for layouts
            if (members != null) {
                addMembers(members, new TeamFunctionType[]{TeamFunctionType.COACH, TeamFunctionType.COACH_ASSISTANT,
                    TeamFunctionType.TEAM_LEADERSHIP}, layoutManagement);
                addMembers(members, new TeamFunctionType[]{TeamFunctionType.PLAYER}, layoutPlayers);
            }
        }
    }

}
