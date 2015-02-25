package com.clubeek.ui.views;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.clubeek.db.RepClubMember;
import com.clubeek.db.RepContact;
import com.clubeek.model.ClubMember;
import com.clubeek.model.Contact;
import com.clubeek.model.User.Role;
import com.clubeek.ui.ModalDialog;
import com.clubeek.ui.Security;
import com.clubeek.ui.Tools;
import com.clubeek.ui.ModalDialog.Mode;
import com.clubeek.ui.components.TableWithButtons;
import com.clubeek.ui.frames.FrameClubMember;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("baseTheme")
public final class ViewClubMembers extends VerticalLayout implements View, Button.ClickListener {

	public ViewClubMembers() {
		this.setCaption(Messages.getString("members")); //$NON-NLS-1$

		// vytvoreni tabulky a ovladacich tlacitek
		table = new TableWithButtons(this, false);
		table.addToOwner(this);

		// vytvoreni sloupcu tabulky
		table.table.addContainerProperty(Messages.getString("name"), String.class, null); //$NON-NLS-1$
		table.table.addContainerProperty(Messages.getString("surname"), String.class, null); //$NON-NLS-1$
		table.table.addContainerProperty(Messages.getString("regNo"), String.class, null); //$NON-NLS-1$
		table.table.addContainerProperty(Messages.getString("dateOfBirth"), String.class, null); //$NON-NLS-1$
		table.table.addContainerProperty(Messages.getString("personalNo"), String.class, null); //$NON-NLS-1$
		table.table.addContainerProperty(Messages.getString("address"), String.class, null); //$NON-NLS-1$
	}

	// interface View

	@Override
	public void enter(ViewChangeEvent event) {
		
		Security.authorize(Role.CLUB_MANAGER);
		
		try {
			members = RepClubMember.selectAll(null);

			ClubMember member;

			int defaultRow = table.getSelectedRow();
			table.table.removeAllItems();

			for (int i = 0; i < members.size(); ++i) {
				member = members.get(i);

				String address = Tools.Strings.concatenateText(
						new String[] { member.getStreet(), member.getCity(), member.getCode() }, ", "); //$NON-NLS-1$

				table.table.addItem(
						new Object[] { member.getName(), member.getSurname(), member.getIdRegistration(),
								member.getBirthdateAsString(), member.getIdPersonal(), address }, new Integer(i));

			}
			table.updateSelection(defaultRow);

		} catch (SQLException e) {
			Tools.msgBoxSQLException(e);
		}
	}

	// interface Button.ClickListener

	@Override
	public void buttonClick(ClickEvent event) {
		try {
			if (event.getButton() == table.buttonAdd)
				addMember();
			else if (event.getButton() == table.buttonEdit)
				editSelectedMember();
			else if (event.getButton() == table.buttonDelete)
				deleteSelectedMember();
		} catch (SQLException e) {
			Tools.msgBoxSQLException(e);
		}
	}

	/* PRIVATE */

	/** Spusti modalni dialog pro pridani noveho clena klubu */
	public void addMember() {
		ModalDialog.show(this, Mode.ADD_ONCE, Messages.getString("newMember"), new FrameClubMember(), new ClubMember(), //$NON-NLS-1$
				RepClubMember.getInstance(),  null);
	}

	/**
	 * Spusti modalni dialog pro zmenu vlastnosti vybraneho clena klubu
	 * 
	 * @throws SQLException
	 */
	public void editSelectedMember() throws SQLException {
		if (table.getSelectedRow() >= 0) {
			// data asociovana s vybranym radkem
			final ClubMember data = members.get(table.getSelectedRow());
			// kopie kontaktu
			final ArrayList<Contact> oldContacts = new ArrayList<>(data.getContacts());
			// vytvoreni dialogu
			ModalDialog<ClubMember> dlg = new ModalDialog<ClubMember>(Mode.EDIT, Messages.getString("clubMemberProperties"), //$NON-NLS-1$
					new FrameClubMember(), data, new ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
							try {
								// zmena vlastnosti clena v databazi
								RepClubMember.update(data);
								// zmena kontaktu v databazi
								RepContact.update(oldContacts, data.getContacts());
								// aktualizace stranky
								enter(null);
							} catch (SQLException e) {
								Tools.msgBoxSQLException(e);
							}
						}
					});
			// zobrazeni dialogu
			getUI().addWindow(dlg);
		}
	}

	/** Zobrazi dotaz uzivateli a pripadne odstrani vybranou ktegorii */
	public void deleteSelectedMember() {
		try {
			// vymazani prvku z databaze
			if ((table.getSelectedRow() >= 0) && (table.getSelectedRow() < members.size()))
				RepClubMember.delete(members.get(table.getSelectedRow()).getId());
			// aktualizace stranky
			enter(null);
		} catch (SQLException e) {
			Tools.msgBoxSQLException(e);
		}
	}

	/* PRIVATE */

	/** Komponenty tabulky */
	private TableWithButtons table;

	/** Seznam aktualne zobrazenych kategorii */
	private List<ClubMember> members = null;

}
