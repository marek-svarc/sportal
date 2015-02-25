package com.clubeek.ui.views;

import java.sql.SQLException;
import java.util.List;

import com.clubeek.db.RepTeamMember;
import com.clubeek.model.ClubMember;
import com.clubeek.model.TeamMember;
import com.clubeek.model.TeamMember.TeamFunction;
import com.clubeek.ui.Tools;
import com.clubeek.ui.Tools.DateTime.DateStyle;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;

@SuppressWarnings("serial")
public class ViewTeamRoster extends VerticalLayout implements View {

	private final String cssTeamMembersList = "teamMembersList"; //$NON-NLS-1$

	public ViewTeamRoster() {
		this.setCaption(Messages.getString("team")); //$NON-NLS-1$

		addLabel(Messages.getString("SupportTeam")); //$NON-NLS-1$
		layoutManagement = new CssLayout();
		layoutManagement.setStyleName(cssTeamMembersList);
		this.addComponent(layoutManagement);

		addLabel(Messages.getString("players")); //$NON-NLS-1$
		layoutPlayers = new CssLayout();
		layoutPlayers.setStyleName(cssTeamMembersList);
		this.addComponent(layoutPlayers);
	}

	@Override
	public void enter(ViewChangeEvent event) {

		int teamId = Tools.Strings.analyzeParameters(event);

		if (teamId >= 0) {

			// nacteni vsech clenu tymu
			List<TeamMember> members = null;
			try {
				members = RepTeamMember.selectByTeamId(teamId, null);
			} catch (SQLException e) {
				Tools.msgBoxSQLException(e);
			}

			// vypis clenu
			if (members != null) {
				// realizacni tym
				addMembers(members, new TeamFunction[] { TeamFunction.COACH, TeamFunction.COACH_ASSISTANT,
						TeamFunction.TEAM_LEADERSHIP }, layoutManagement);
				// hraci
				addMembers(members, new TeamFunction[] { TeamFunction.PLAYER }, layoutPlayers);
			}
		}
	}

	private void addMembers(List<TeamMember> members, TeamFunction[] teamFunctions, CssLayout layout) {
		ClubMember clubMember;
		Panel infoPanel;
		Image photo;
		VerticalLayout infoLayout;
		// odstraneni komponent z layoutu
		layout.removeAllComponents();
		try {
			for (TeamFunction f : teamFunctions) {
				for (TeamMember m : members)
					if (m.isFunction(f) && (m.getClubMember() != null)) {
						clubMember = m.getClubMember();
						infoLayout = new VerticalLayout();
						// fotka clena tymu
						photo = new Image();
						photo.setWidth(100, Unit.PIXELS);
						photo.setHeight(100, Unit.PIXELS);
						Tools.Components.fillImageByPortrait(photo, clubMember.getPhoto(), Integer.toString(clubMember.getId()));
						infoLayout.addComponent(photo);
						// popis
						if (!m.isFunction(TeamFunction.PLAYER))
							infoLayout.addComponent(new Label(m.getFunctionsAsList().toString()));
						else if (clubMember.getBirthdate() != null)
							infoLayout.addComponent(new Label(String.format("%s %s", Messages.getString("yearGroup"), //$NON-NLS-1$ //$NON-NLS-2$
									Tools.DateTime.dateToString(clubMember.getBirthdate(), DateStyle.YEAR))));
						// panel pro zobrazeni fotky a detailu
						infoPanel = new Panel(String.format("%s %s", clubMember.getName(), clubMember.getSurname()), infoLayout); //$NON-NLS-1$
						infoPanel.setSizeUndefined();
						infoPanel.setStyleName(Runo.PANEL_LIGHT);
						infoPanel.addStyleName("detail"); //$NON-NLS-1$
						layout.addComponent(infoPanel);
					}
			}
		} catch (SQLException e) {
			Tools.msgBoxSQLException(e);
		}
	}

	private void addLabel(String text) {
		Label label = new Label(text);
		label.setStyleName(Runo.LABEL_H2);
		label.addStyleName(cssTeamMembersList);
		this.addComponent(label);
	}

	/** Rozmisteni informacnich panelu pro realizacni tym */
	private CssLayout layoutManagement;

	/** Rozmisteni informacnich panelu pro hrace */
	private CssLayout layoutPlayers;
}
