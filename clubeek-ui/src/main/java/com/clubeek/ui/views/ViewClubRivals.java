package com.clubeek.ui.views;

import java.sql.SQLException;
import java.util.List;

import com.clubeek.db.RepClubRival;
import com.clubeek.model.ClubRival;
import com.clubeek.model.User.Role;
import com.clubeek.ui.ModalDialog;
import com.clubeek.ui.Security;
import com.clubeek.ui.Tools;
import com.clubeek.ui.ModalDialog.Mode;
import com.clubeek.ui.components.TableWithButtons;
import com.clubeek.ui.frames.FrameClubRival;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("baseTheme")
public class ViewClubRivals extends VerticalLayout implements View {

	public ViewClubRivals() {

		this.setCaption(Messages.getString("ViewClubRivals.0")); //$NON-NLS-1$

		// vytvoreni tabulky a ovladacich tlacitek
		table = new TableWithButtons(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if (event.getButton() == table.buttonAdd)
					insertClub();
				else if (event.getButton() == table.buttonEdit)
					editClub();
				else if (event.getButton() == table.buttonDelete)
					deleteClub();
			}
		}, false);
		table.addToOwner(this);

		// vytvoreni sloupcu tabulky
		table.table.addContainerProperty(Messages.getString("name"), String.class, null); //$NON-NLS-1$
		table.table.addContainerProperty(Messages.getString("address"), String.class, null); //$NON-NLS-1$
		table.table.addContainerProperty(Messages.getString("web"), Link.class, null); //$NON-NLS-1$
		table.table.addContainerProperty(Messages.getString("gps"), String.class, null); //$NON-NLS-1$
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
		Security.authorize(Role.SPORT_MANAGER);
		
		try {
			clubs = RepClubRival.selectAll(null);

			ClubRival club;

			int defaultRow = table.getSelectedRow();
			table.table.removeAllItems();

			for (int i = 0; i < clubs.size(); ++i) {
				club = clubs.get(i);
				
				// sestaveni adresy klubu do jednoho retezce
				String address = Tools.Strings.concatenateText(
						new String[] { club.getStreet(), club.getCity(), club.getCode() }, ", "); //$NON-NLS-1$
				
				// objekt odkazu na webove stranky klubu
				Link webLink = new Link(club.getWeb(), new ExternalResource(club.getWeb()));
				webLink.setTargetName("_blank"); //$NON-NLS-1$
				
				// pridani radky tabulky
				table.table.addItem(new Object[] { club.getName(), address, webLink, club.getGPS() },
						new Integer(i));
			}
			table.updateSelection(defaultRow);

		} catch (SQLException e) {
			Tools.msgBoxSQLException(e);
		}
	}

	public void insertClub() {
		ModalDialog.show(this, Mode.ADD_ONCE, Messages.getString("ViewClubRivals.2"), new FrameClubRival(), new ClubRival(), RepClubRival.getInstance(), null); //$NON-NLS-1$
	}

	public void editClub() {
		if (table.getSelectedRow() >= 0) {
			ClubRival club = clubs.get(table.getSelectedRow());
			ModalDialog.show(this, Mode.EDIT, Messages.getString("ViewClubRivals.3"), new FrameClubRival(), club, RepClubRival.getInstance(), null); //$NON-NLS-1$
		}
	}

	public void deleteClub() {
		table.deleteSelectedRow(clubs, RepClubRival.getInstance(), this, null);
	}

	/* PRIVATE */

	/** Tabulka zobrazujici seznam klubu */
	private TableWithButtons table;

	/** Seznam klubu nacteni z databaze */
	private List<ClubRival> clubs;
}
