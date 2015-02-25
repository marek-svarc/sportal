package com.clubeek.ui.views;

import java.sql.SQLException;
import java.util.List;

import com.clubeek.db.RepTeamMatch;
import com.clubeek.model.TeamMatch;
import com.clubeek.model.User.Role;
import com.clubeek.ui.ModalDialog;
import com.clubeek.ui.Security;
import com.clubeek.ui.Tools;
import com.clubeek.ui.ModalDialog.Mode;
import com.clubeek.ui.Tools.DateTime.DateStyle;
import com.clubeek.ui.components.TableWithButtons;
import com.clubeek.ui.frames.FrameTeamMatch;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ViewTeamMatches extends VerticalLayout implements View {

	public ViewTeamMatches() {

		this.setCaption(Messages.getString("matches")); //$NON-NLS-1$

		// vytvoreni tabulky a ovladacich tlacitek
		table = new TableWithButtons(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if (event.getButton() == table.buttonAdd)
					insertMatch();
				else if (event.getButton() == table.buttonEdit)
					editMatch();
				else if (event.getButton() == table.buttonDelete)
					deleteMatch();
			}
		}, false);
		table.addToOwner(this);

		// vytvoreni sloupcu tabulky
		table.table.addContainerProperty(Messages.getString("match"), String.class, null); //$NON-NLS-1$
		table.table.addContainerProperty(Messages.getString("dateTime"), String.class, null); //$NON-NLS-1$
		table.table.addContainerProperty(Messages.getString("result"), String.class, null); //$NON-NLS-1$
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
		Security.authorize(Role.SPORT_MANAGER);

		if (event != null)
			teamId = Tools.Strings.analyzeParameters(event);

		int defaultRow = table.getSelectedRow();
		table.table.removeAllItems();

		if (teamId > 0) {
			// nacteni seznamu treninku z databaze
			games = null;
			try {
				games = RepTeamMatch.selectByTeamId(teamId, null);
			} catch (SQLException e) {
				Tools.msgBoxSQLException(e);
			}

			// inicializace tbulky
			if (games != null) {
				TeamMatch game;
				for (int i = 0; i < games.size(); ++i) {
					game = games.get(i);
					table.table.addItem(
							new Object[] { Tools.Strings.getStrGameTeams(game),
									Tools.DateTime.dateToString(game.getStart(), DateStyle.DAY_AND_TIME), game.getScoreAsStr() },
							i);
				}
			}
			table.updateSelection(defaultRow);
		}
	}

	public void insertMatch() {
		TeamMatch data = new TeamMatch();
		data.setClubTeamId(teamId);

		ModalDialog.show(this, Mode.ADD_MULTI,
				Messages.getString("match"), new FrameTeamMatch(), data, RepTeamMatch.getInstance(), null); //$NON-NLS-1$
	}

	public void editMatch() {
		if (table.getSelectedRow() >= 0) {
			TeamMatch game = games.get(table.getSelectedRow());
			ModalDialog.show(this, Mode.EDIT,
					Messages.getString("match"), new FrameTeamMatch(), game, RepTeamMatch.getInstance(), null); //$NON-NLS-1$
		}
	}

	public void deleteMatch() {
		table.deleteSelectedRow(games, RepTeamMatch.getInstance(), this, null);
	}

	/* PRIVATE */

	/** Identifikator tymu pro ktery jsou zapasy zobrazeny */
	private int teamId = -1;

	/** Tabulka zobrazujici seznam zapasu */
	private TableWithButtons table;

	/** Seznam vsech zapasu pro jeden tym */
	private List<TeamMatch> games = null;

}
