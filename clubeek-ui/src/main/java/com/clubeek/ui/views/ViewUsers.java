package com.clubeek.ui.views;

import java.sql.SQLException;
import java.util.List;

import com.clubeek.db.RepClubMember;
import com.clubeek.db.RepUser;
import com.clubeek.model.User;
import com.clubeek.model.User.Role;
import com.clubeek.ui.ModalDialog;
import com.clubeek.ui.Security;
import com.clubeek.ui.Tools;
import com.clubeek.ui.ModalDialog.Mode;
import com.clubeek.ui.components.TableWithButtons;
import com.clubeek.ui.frames.FrameUser;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ViewUsers extends VerticalLayout implements View {

	public ViewUsers() {

		this.setCaption(Messages.getString("userManagement")); //$NON-NLS-1$

		// vytvoreni tabulky a ovladacich tlacitek
		table = new TableWithButtons(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if (event.getButton() == table.buttonAdd)
					insertUser();
				else if (event.getButton() == table.buttonEdit)
					editUser();
				else if (event.getButton() == table.buttonDelete)
					deleteUser();
			}
		}, false);
		table.addToOwner(this);

		// vytvoreni sloupcu tabulky
		table.table.addContainerProperty(Messages.getString("userName"), String.class, null); //$NON-NLS-1$
		table.table.addContainerProperty(Messages.getString("authorization"), String.class, null); //$NON-NLS-1$
		table.table.addContainerProperty(Messages.getString("teamMember"), String.class, null); //$NON-NLS-1$
	}

	public void insertUser() {
		User data = new User();
		ModalDialog.show(this, Mode.ADD_ONCE, Messages.getString("newUser"), new FrameUser(), data, RepUser.getInstance(), null); //$NON-NLS-1$
	}

	public void editUser() {
		if (table.getSelectedRow() >= 0) {
			User user = users.get(table.getSelectedRow());
			ModalDialog.show(this, Mode.EDIT, String.format("%s '%s'", Messages.getString("userProperties"), user.getName()), new FrameUser(), //$NON-NLS-1$ //$NON-NLS-2$
					user, RepUser.getInstance(), null);
		}
	}

	public void deleteUser() {
		table.deleteSelectedRow(users, RepUser.getInstance(), this, null);
	}

	@Override
	public void enter(ViewChangeEvent event) {

		Security.authorize(Role.ADMINISTRATOR);
		
		int defaultRow = table.getSelectedRow();
		table.table.removeAllItems();

		// nacteni seznamu treninku z databaze
		users = null;
		try {
			users = RepUser.selectAll(new RepUser.TableColumn[] { RepUser.TableColumn.ID, RepUser.TableColumn.NAME,
					RepUser.TableColumn.PERMISSIONS, RepUser.TableColumn.CLUB_MEMBER_ID });
			for (User user : users)
				user.setClubMember(RepClubMember.selectById(user.getClubMemberId(), new RepClubMember.TableColumn[] {
						RepClubMember.TableColumn.ID, RepClubMember.TableColumn.NAME, RepClubMember.TableColumn.SURNAME,
						RepClubMember.TableColumn.BIRTHDATE }));

		} catch (SQLException e) {
			Tools.msgBoxSQLException(e);
		}

		// inicializace tbulky
		if (users != null) {
			for (int i = 0; i < users.size(); ++i) {
				User user = users.get(i);
				table.table.addItem(new Object[] { user.getName(), user.getRole().toString(),
						user.getClubMember() != null ? user.getClubMember().toString() : "" }, i); //$NON-NLS-1$
			}
		}
		table.updateSelection(defaultRow);

	}

	/* PRIVATE */

	/** Tabulka zobrazujici seznam uzivatelu */
	private TableWithButtons table;

	/** Seznam vsech uzivatelu */
	private List<User> users = null;

}
