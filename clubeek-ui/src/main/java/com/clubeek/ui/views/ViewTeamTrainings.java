package com.clubeek.ui.views;

import java.sql.SQLException;
import java.util.List;

import com.clubeek.db.RepTeamTraining;
import com.clubeek.model.TeamTraining;
import com.clubeek.model.User.Role;
import com.clubeek.ui.ModalDialog;
import com.clubeek.ui.Security;
import com.clubeek.ui.Tools;
import com.clubeek.ui.ModalDialog.Mode;
import com.clubeek.ui.components.TableWithButtons;
import com.clubeek.ui.frames.FrameTeamTraining;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.CellStyleGenerator;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ViewTeamTrainings extends VerticalLayout implements View {

	public ViewTeamTrainings() {

		this.setCaption(Messages.getString("trainings")); //$NON-NLS-1$

		// vytvoreni tabulky a ovladacich tlacitek
		table = new TableWithButtons(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if (event.getButton() == table.buttonAdd)
					insertTraining();
				else if (event.getButton() == table.buttonEdit)
					editTraining();
				else if (event.getButton() == table.buttonDelete)
					deleteTraining();
			}
		}, false);
		table.addToOwner(this);

		// vytvoreni sloupcu tabulky
		table.table.addContainerProperty(Messages.getString("dateTime"), String.class, null); //$NON-NLS-1$
		table.table.addContainerProperty(Messages.getString("place"), String.class, null); //$NON-NLS-1$

		// vlastni stylovani radek tabulky
		table.table.setCellStyleGenerator(new CellStyleGenerator() {

			@Override
			public String getStyle(Table source, Object itemId, Object propertyId) {
				TeamTraining trainig = trainings.get((int) itemId);
				if (trainig.isGone())
					return "disabled"; //$NON-NLS-1$
				else
					return null;
			}
		});
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
			trainings = null;
			try {
				trainings = RepTeamTraining.selectByClubTeamId(teamId, null);
			} catch (SQLException e) {
				Tools.msgBoxSQLException(e);
			}

			// inicializace tbulky
			if (trainings != null) {
				TeamTraining training;
				for (int i = 0; i < trainings.size(); ++i) {
					training = trainings.get(i);
					table.table.addItem(
							new Object[] {
									Tools.DateTime.eventToString(training, Tools.DateTime.DateStyle.DAY_AND_TIME,
											Tools.DateTime.DateStyle.DAY_AND_TIME), training.getPlace() }, i);
				}
			}
			table.updateSelection(defaultRow);
		}
	}

	public void insertTraining() {
		TeamTraining data = new TeamTraining();
		data.setClubTeamId(teamId);

		ModalDialog.show(this, Mode.ADD_MULTI, Messages.getString("training"), new FrameTeamTraining(), data, RepTeamTraining.getInstance(), null); //$NON-NLS-1$
	}

	public void editTraining() {
		if (table.getSelectedRow() >= 0) {
			TeamTraining training = trainings.get(table.getSelectedRow());
			if (training.isGone())
				Notification.show(Messages.getString("canNotModifyTreining"), Type.HUMANIZED_MESSAGE); //$NON-NLS-1$
			else
				ModalDialog.show(this, Mode.EDIT, Messages.getString("training"), new FrameTeamTraining(), training, RepTeamTraining.getInstance(), null); //$NON-NLS-1$
		}
	}

	public void deleteTraining() {
		table.deleteSelectedRow(trainings, RepTeamTraining.getInstance(), this, null);
	}

	/* PRIVATE */

	/** Identifikator tymu pro ktery jsou treninky zobrazeny */
	private int teamId = -1;

	/** Tabulka zobrazujici seznam klubu */
	private TableWithButtons table;

	/** Seznam treninku nactenych z databaze */
	private List<TeamTraining> trainings;

}
