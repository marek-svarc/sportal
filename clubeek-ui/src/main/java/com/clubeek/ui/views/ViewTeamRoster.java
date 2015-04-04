package com.clubeek.ui.views;

import java.util.List;

import com.clubeek.db.RepTeamMember;
import com.clubeek.model.ClubMember;
import com.clubeek.model.TeamMember;
import com.clubeek.model.TeamMember.TeamFunction;
import com.clubeek.ui.Tools;
import com.clubeek.ui.Tools.DateTime.DateStyle;
import com.clubeek.ui.components.ActionTable;
import com.clubeek.ui.components.ActionTable.OnActionListener;
import com.clubeek.ui.components.MemberCard;
import com.clubeek.util.SimpleEnumSet;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class ViewTeamRoster extends VerticalLayout implements View {

    private final String cssTeamMembersList = "teamMembersList"; //$NON-NLS-1$

    /**
     * Main layout divides tables and profile cards for member or couch.
     */
    private HorizontalLayout hlMainLayout;

    /**
     * Layout for table of members and table of trainers.
     */
    private VerticalLayout vlTablesLayout;
    private ActionTable tableMembers;
    private MemberCard memberCard;

    public ViewTeamRoster() {
        this.setCaption(Messages.getString("team")); //$NON-NLS-1$

        hlMainLayout = new HorizontalLayout();
        hlMainLayout.setSizeFull();
        hlMainLayout.setSpacing(true);
        hlMainLayout.setMargin(true);
        addComponent(hlMainLayout);

        vlTablesLayout = new VerticalLayout();
        vlTablesLayout.setSpacing(true);
        vlTablesLayout.setMargin(true);
        vlTablesLayout.setWidth(300, Unit.PIXELS);
        hlMainLayout.addComponent(vlTablesLayout);

        vlTablesLayout.addComponent(new Label(Messages.getString("SupportTeam"))); //$NON-NLS-1$
        tableMembers = new ActionTable(new SimpleEnumSet<ActionTable.Action>(), new ActionTable.UserColumnInfo[]{
            new ActionTable.UserColumnInfo("name", String.class, Messages.getString("name")),
            new ActionTable.UserColumnInfo("surname", String.class, Messages.getString("surname")),
            new ActionTable.UserColumnInfo("functions", String.class, Messages.getString("functions"))
        }, null);
        tableMembers.addToOwner(vlTablesLayout);
        
        memberCard = new MemberCard();
        hlMainLayout.addComponent(memberCard);
        memberCard.setVisible(false);

        //layoutManagement = new CssLayout();
        //layoutManagement.setStyleName(cssTeamMembersList);
        //this.addComponent(layoutManagement);
        //vlTablesLayout.addComponent(new Label(Messages.getString("players"))); //$NON-NLS-1$
        //layoutPlayers = new CssLayout();
        //layoutPlayers.setStyleName(cssTeamMembersList);
        //this.addComponent(layoutPlayers);
    }

    private void showProfile(TeamMember member) {
        memberCard.setTeamMember(member);
        memberCard.setVisible(true);
    }

    @Override
    public void enter(ViewChangeEvent event) {

        int teamId = Tools.Strings.analyzeParameters(event);

        if (teamId >= 0) {

            // nacteni vsech clenu tymu
            List<TeamMember> members = null;
            members = RepTeamMember.selectByTeamId(teamId, null);

            // vypis clenu
            if (members != null) {
                // realizacni tym
                addMembers(members, new TeamFunction[]{TeamFunction.COACH, TeamFunction.COACH_ASSISTANT,
                    TeamFunction.TEAM_LEADERSHIP}, layoutManagement);
                // hraci
                addMembers(members, new TeamFunction[]{TeamFunction.PLAYER}, layoutPlayers);
            }
        }
    }

    private void addMembers(List<TeamMember> members, TeamFunction[] teamFunctions, CssLayout layout) {

        tableMembers.removeAllRows();
        for (TeamMember member : members) {
            //        Panel infoPanel;
            //        Image photo;
            //        VerticalLayout infoLayout;
            //        // odstraneni komponent z layoutu
            //        layout.removeAllComponents();
            //        try {
            //            for (TeamFunction f : teamFunctions) {
            //                for (TeamMember m : members) {
            //                    if (m.isFunction(f) && (m.getClubMember() != null)) {
            //                        clubMember = m.getClubMember();
            //                        infoLayout = new VerticalLayout();
            //                        // fotka clena tymu
            //                        photo = new Image();
            //                        photo.setWidth(100, Unit.PIXELS);
            //                        photo.setHeight(100, Unit.PIXELS);
            //                        Tools.Components.fillImageByPortrait(photo, clubMember.getPhoto(), Integer.toString(clubMember.getId()));
            //                        infoLayout.addComponent(photo);
            //                        // popis
            //                        if (!m.isFunction(TeamFunction.PLAYER)) {
            //                            infoLayout.addComponent(new Label(m.getFunctionsAsList().toString()));
            //                        } else if (clubMember.getBirthdate() != null) {
            //                            infoLayout.addComponent(new Label(String.format("%s %s", Messages.getString("yearGroup"), //$NON-NLS-1$ //$NON-NLS-2$
            //                                    Tools.DateTime.dateToString(clubMember.getBirthdate(), DateStyle.YEAR))));
            //                        }
            //                        // panel pro zobrazeni fotky a detailu
            //                        infoPanel = new Panel(String.format("%s %s", clubMember.getName(), clubMember.getSurname()), infoLayout); //$NON-NLS-1$
            //                        infoPanel.setSizeUndefined();
            //                        infoPanel.setStyleName(Runo.PANEL_LIGHT);
            //                        infoPanel.addStyleName("detail"); //$NON-NLS-1$
            //                        layout.addComponent(infoPanel);
            //                    }
            //                }
            //            }
            //        } catch (SQLException e) {
            //            Tools.msgBoxSQLException(e);
            //        }
            
            StringBuilder sb = new StringBuilder();
            for (TeamFunction function : teamFunctions) {
                sb.append(function.toString()).append(" ");
            }
            tableMembers.addRow(new Object[]{member.getClubMember().getName(),
                member.getClubMember().getSurname(), sb.toString()}, member);
        }

    }

    private void addLabel(String text) {
        Label label = new Label(text);
        label.setStyleName(Runo.LABEL_H2);
        label.addStyleName(cssTeamMembersList);
        this.addComponent(label);
    }

    /**
     * Rozmisteni informacnich panelu pro realizacni tym
     */
    private CssLayout layoutManagement;

    /**
     * Rozmisteni informacnich panelu pro hrace
     */
    private CssLayout layoutPlayers;
}
