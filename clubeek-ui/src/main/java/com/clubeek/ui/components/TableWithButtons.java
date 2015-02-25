package com.clubeek.ui.components;

import java.sql.SQLException;
import java.util.List;

import com.clubeek.db.Repository;
import com.clubeek.model.Unique;
import com.clubeek.ui.Messages;
import com.clubeek.ui.Tools;
import com.clubeek.ui.views.Navigation;
import com.vaadin.navigator.View;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.Runo;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Trida zapouzdrujici skupinu komponent tvoricich tabulku a ovladaci tlacitka
 * (Pridat, Upravit, Smazat)
 * 
 * @author Marek Svarc
 * 
 */
public class TableWithButtons {

	/* PUBLIC */

	public final Table table;
	public final Button buttonAdd;
	public final Button buttonEdit;
	public final Button buttonDelete;
	public final Button buttonMoveUp;
	public final Button buttonMoveDown;
	public final HorizontalLayout buttonsLayout;

	public TableWithButtons(Button.ClickListener listener, boolean externalSort) {

		// prostor pro umisteni tlcitek
		buttonsLayout = new HorizontalLayout();
		buttonsLayout.setSizeUndefined();

		// tlacitka
		buttonAdd = new Button(Messages.getString("add"), listener); //$NON-NLS-1$
		setButtonStyle(buttonAdd, ValoTheme.BUTTON_FRIENDLY); //$NON-NLS-1$
		buttonsLayout.addComponent(buttonAdd);
		buttonEdit = new Button(Messages.getString("edit"), listener); //$NON-NLS-1$
		setButtonStyle(buttonEdit, ValoTheme.BUTTON_PRIMARY); //$NON-NLS-1$
		buttonsLayout.addComponent(buttonEdit);
		buttonDelete = new Button(Messages.getString("delete"), listener); //$NON-NLS-1$
		setButtonStyle(buttonDelete, ValoTheme.BUTTON_DANGER); //$NON-NLS-1$
		buttonsLayout.addComponent(buttonDelete);

		if (externalSort) {
			buttonMoveUp = new Button("▲ " + Messages.getString("moveUp"), listener); //$NON-NLS-1$ //$NON-NLS-2$
			setButtonStyle(buttonMoveUp, "horzSeparator"); //$NON-NLS-1$
			buttonsLayout.addComponent(buttonMoveUp);
			buttonMoveDown = new Button("▼ " + Messages.getString("moveDown"), listener); //$NON-NLS-1$ //$NON-NLS-2$
			setButtonStyle(buttonMoveDown, null); //$NON-NLS-1$
			buttonsLayout.addComponent(buttonMoveDown);
		} else {
			buttonMoveUp = null;
			buttonMoveDown = null;
		}

		// tabulka
		table = new Table();
		table.addStyleName(Runo.TABLE_SMALL);
		table.addStyleName(Runo.TABLE_BORDERLESS);
		table.setSizeFull();
		table.setSelectable(true);
		table.setNullSelectionAllowed(false);
		table.setSortEnabled(!externalSort);
	}

	public void addToOwner(AbstractOrderedLayout owner) {
		owner.addComponent(buttonsLayout);
		owner.addComponent(table);
		owner.setExpandRatio(table, 1.0f);
		table.setPageLength(21);
	}

	/** Aktualizuje vybranou radku */
	public void updateSelection(int defaultRow) {
		Tools.Components.initSelection(table, defaultRow);
		// aktualizace tlacitek
		updateButtons();
	}

	/** Aktualizuje tlacitka dle vybrane radky tabulky */
	public void updateButtons() {
		boolean enable = table.size() > 0;
		buttonEdit.setEnabled(enable);
		buttonDelete.setEnabled(enable);
		if (buttonMoveUp != null)
			buttonMoveUp.setEnabled(enable);
		if (buttonMoveDown != null)
			buttonMoveDown.setEnabled(enable);
	}

	/** Vraci index vybrane radky */
	public int getSelectedRow() {
		Object value = table.getValue();
		return value != null ? (int) value : -1;
	}

	/** Odstrani vybranou radku */
	public <T extends Unique> void deleteSelectedRow(final Repository<T> dataAdmin, final Component parent,
			final Navigation navigation) {
		int id = (int) table.getValue();
		if (id > 0) {
			// odstraneni z databaze
			try {
				dataAdmin.deleteRow(id);
			} catch (SQLException e) {
				Tools.msgBoxSQLException(e);
			}
			// aktualizace aplikace
			if (parent instanceof View)
				((View) parent).enter(null);
			// aktualizace navigace v aplikaci
			if (navigation != null)
				navigation.updateNavigationMenu();
		}
	}

	/** Odstrani vybranou radku */
	public <T extends Unique> void deleteSelectedRow(List<T> data, final Repository<T> dataAdmin, final Component parent,
			final Navigation navigation) {
		try {
			// vymazani prvku z databaze
			if ((this.getSelectedRow() >= 0) && (this.getSelectedRow() < table.size())) {
				// odstraneni z databaze
				dataAdmin.deleteRow(data.get(this.getSelectedRow()).getId());
				// aktualizace aplikace
				if (parent instanceof View)
					((View) parent).enter(null);
				// aktualizace navigace v aplikaci
				if (navigation != null)
					navigation.updateNavigationMenu();
			}
		} catch (SQLException e) {
			Tools.msgBoxSQLException(e);
		}
	}

	/**
	 * Testuje zda je roxIndex platne cislo radky tabulky
	 * 
	 * @param rowIndex
	 *            testovane cislo radky tabulky
	 */
	public boolean checkRowIndex(int rowIndex) {
		return (rowIndex >= 0) && (rowIndex < table.size());
	}

	/**
	 * Dopocte novy index pro funkce MoveUp a MoveDown
	 * 
	 * @param moveUp
	 *            rozlisi dopocet MoveUp a MoveDown
	 */
	public int getMoveIndex(boolean moveUp) {
		int selectedRow = getSelectedRow();
		if (checkRowIndex(selectedRow)) {
			int moveTo = moveUp ? selectedRow - 1 : selectedRow + 1;
			return checkRowIndex(moveTo) ? moveTo : -1;
		} else {
			return -1;
		}
	}

	/* PRIVATE */

	private void setButtonStyle(Button button, String style) {
		button.setStyleName(ValoTheme.BUTTON_TINY);
		button.addStyleName("table"); //$NON-NLS-1$
		if (style != null)
			button.addStyleName(style);
		button.setImmediate(true);
	}

	// private int selectedRow = -1;
}
