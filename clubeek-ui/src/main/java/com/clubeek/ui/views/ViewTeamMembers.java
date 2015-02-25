package com.clubeek.ui.views;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.clubeek.db.RepClubTeam;
import com.clubeek.db.RepTeamMember;
import com.clubeek.model.ClubMember;
import com.clubeek.model.ClubTeam;
import com.clubeek.model.ModelTools;
import com.clubeek.model.TeamMember;
import com.clubeek.ui.ModalDialog;
import com.clubeek.ui.Tools;
import com.clubeek.ui.ModalDialog.Mode;
import com.clubeek.ui.components.TableWithButtons;
import com.clubeek.ui.frames.FrameSelectMembers;
import com.clubeek.ui.frames.FrameTeamFunctions;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ViewTeamMembers extends VerticalLayout implements View {

	public ViewTeamMembers() {

		this.setCaption(Messages.getString("assignmentToTeams")); //$NON-NLS-1$

		// tabulka clenu tymu

		tbTeamMembers = new TableWithButtons(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if (event.getButton() == tbTeamMembers.buttonAdd)
					selectTeamMembers();
				else if (event.getButton() == tbTeamMembers.buttonEdit)
					selectTeamFunctions();
				else if (event.getButton() == tbTeamMembers.buttonDelete)
					deleteTeamMember();
			}
		}, false);
		tbTeamMembers.addToOwner(this);

		tbTeamMembers.table.addContainerProperty(Messages.getString("name"), String.class, null); //$NON-NLS-1$
		tbTeamMembers.table.addContainerProperty(Messages.getString("surname"), String.class, null); //$NON-NLS-1$
		tbTeamMembers.table.addContainerProperty(Messages.getString("dateOfBirth"), String.class, null); //$NON-NLS-1$
		tbTeamMembers.table.addContainerProperty(Messages.getString("positionInTeam"), String.class, null); //$NON-NLS-1$
		tbTeamMembers.buttonAdd.setCaption(Messages.getString("selectTeamMembers")); //$NON-NLS-1$
		tbTeamMembers.buttonEdit.setCaption(Messages.getString("editTeamFunction")); //$NON-NLS-1$

		// Seznam pro vyber editovaneho tymu

		nsTeams = new NativeSelect(null);
		nsTeams.addStyleName("inline-first"); //$NON-NLS-1$
		nsTeams.setImmediate(true);
		nsTeams.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateTeamMembersTable();
			}
		});
		tbTeamMembers.buttonsLayout.addComponent(nsTeams, 0);

		Label laTeams = new Label(" - "); //$NON-NLS-1$
		laTeams.addStyleName("inline"); //$NON-NLS-1$
		tbTeamMembers.buttonsLayout.addComponent(laTeams, 1);

	}

	/** Implementace rozhrani View */
	@Override
	public void enter(ViewChangeEvent event) {
		List<ClubTeam> teams = null;
		ClubTeam selectedTeam = getSelectedTeam();

		// nplneni seznamu aktivnich tymu
		try {
			teams = RepClubTeam.select(true, null);
		} catch (SQLException e) {
			Tools.msgBoxSQLException(e);
		}
		Tools.Components.initNativeSelect(nsTeams, teams);

		// vyber defaultniho aktivniho tymu
		if (selectedTeam != null)
			selectedTeam = ModelTools.listFindById(teams, selectedTeam.getId());
		if ((selectedTeam == null) && (teams != null) && (teams.size() > 0))
			selectedTeam = teams.get(0);

		// prirazeni defaultniho tymu
		nsTeams.setValue(selectedTeam);
	}

	/** Vraci aktualne vybrany tym */
	public ClubTeam getSelectedTeam() {
		return (ClubTeam) nsTeams.getValue();
	}

	/** Vraci aktualne vybranneho clena tymu */
	public TeamMember getSelectedTeamMember() {
		if (tbTeamMembers.getSelectedRow() >= 0)
			return dataTeamMembers.get(tbTeamMembers.getSelectedRow());
		else
			return null;
	}

	/* PRIVATE */

	/** Aktualizuje radky v tabulce clena aktualne vybraneho tymu. */
	private void updateTeamMembersTable() {
		int defaultRow = tbTeamMembers.getSelectedRow();
		tbTeamMembers.table.removeAllItems();

		try {
			ClubTeam selectedTeam = getSelectedTeam();
			if (selectedTeam != null) {
				// na�ten� clenu Trida pro vybran� t�m
				dataTeamMembers = RepTeamMember.selectByTeamId(selectedTeam.getId(), null);

				// napln�n� tabulky
				ClubMember clubMember;
				TeamMember teamMember;
				for (int i = 0; i < dataTeamMembers.size(); ++i) {
					teamMember = dataTeamMembers.get(i);
					clubMember = teamMember.getClubMember();
					tbTeamMembers.table.addItem(
							new Object[] { clubMember.getName(), clubMember.getSurname(), clubMember.getBirthdateAsString(),
									teamMember.getFunctionsAsList().toString() }, new Integer(i));
				}

				// aktualizace v�b�ru radky
				tbTeamMembers.updateSelection(defaultRow);
			}
		} catch (SQLException e) {
			Tools.msgBoxSQLException(e);
		}
	}

	/** Spusti modalni okno pro vyber clena tymu */
	private void selectTeamMembers() {

		final ClubTeam selectedTeam = getSelectedTeam();

		if (selectedTeam != null) {

			try {
				// seznam clenu klubu asociovanych se zobrazenymi cleny tymu
				final List<ClubMember> clubMembers = new ArrayList<>(dataTeamMembers.size());
				for (TeamMember teamMember : dataTeamMembers)
					clubMembers.add(teamMember.getClubMember());

				// vytvoreni dialogu
				ModalDialog<List<ClubMember>> dlg = new ModalDialog<List<ClubMember>>(Mode.EDIT, Messages.getString("selectTeamMembers"), //$NON-NLS-1$
						new FrameSelectMembers(), clubMembers, new ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								try {
									RepTeamMember.update(dataTeamMembers,
											RepTeamMember.selectOrCreateByClubMembers(selectedTeam.getId(), clubMembers, null));
								} catch (SQLException e) {
									Tools.msgBoxSQLException(e);
								}
								updateTeamMembersTable();
							}
						});

				getUI().addWindow(dlg);

			} catch (SQLException e) {
				Tools.msgBoxSQLException(e);
			}
		}
	}

	/** Spusti modalni okno pro vyber funkci v tymu */
	private void selectTeamFunctions() {
		final TeamMember selectedTeamMember = getSelectedTeamMember();

		if (selectedTeamMember != null)
			ModalDialog.show(this, Mode.EDIT, Messages.getString("editTeamFunction"), new FrameTeamFunctions(), selectedTeamMember, //$NON-NLS-1$
					RepTeamMember.getInstance(), null);
	}

	private void deleteTeamMember() {
		try {
			// vymazani prvku z databaze
			if ((tbTeamMembers.getSelectedRow() >= 0) && (tbTeamMembers.getSelectedRow() < dataTeamMembers.size()))
				RepTeamMember.delete(dataTeamMembers.get(tbTeamMembers.getSelectedRow()).getId());
			// aktualizace stranky
			enter(null);
		} catch (SQLException e) {
			Tools.msgBoxSQLException(e);
		}
	}

	/** Seznam pro vyber tymu */
	private NativeSelect nsTeams;

	/** Tabulka clenu asociovanych s tymem */
	private TableWithButtons tbTeamMembers;

	/** Seznam clenu tymu zobrazenych v tabulce */
	private List<TeamMember> dataTeamMembers;
}
