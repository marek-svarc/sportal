package com.clubeek.ui.components;

import java.sql.SQLException;
import java.util.List;

import com.clubeek.db.Repository;
import com.clubeek.model.Unique;
import com.clubeek.ui.Messages;
import com.clubeek.ui.Tools;
import com.clubeek.ui.views.Navigation;
import com.clubeek.util.SimpleEnumSet;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Trida zapouzdrujici skupinu komponent tvoricich tabulku a ovladaci tlacitka
 * (Pridat, Upravit, Smazat)
 *
 * @author Marek Svarc
 *
 */
public final class TableWithButtons {

    /* PUBLIC */
    public static enum CtrlColumn {

        CBX_SELECTION, BTN_EDIT, BTN_DELETE, BTN_SORT;

        public static SimpleEnumSet<CtrlColumn> getStandardSet() {
            return new SimpleEnumSet<>(CBX_SELECTION, BTN_EDIT, BTN_DELETE);
        }

        public static SimpleEnumSet<CtrlColumn> getMaximalSet() {
            return new SimpleEnumSet<>(CtrlColumn.values());
        }

    }

    /** Class that provides informations about one column in the table. */
    public static class UserColumnInfo {

        /** The Id of the property */
        public final Object propertyId;

        /** The class of the property */
        public final Class<?> propertyType;

        /** The Explicit header of the column */
        public final String caption;

        public UserColumnInfo(Object propertyId, Class<?> propertyType, String caption) {
            this.propertyId = propertyId;
            this.propertyType = propertyType;
            this.caption = caption;
        }
    }

    public TableWithButtons(SimpleEnumSet<CtrlColumn> ctrlColumns, UserColumnInfo[] userColumns, Button.ClickListener listener) {

        this.ctrlColumns = ctrlColumns;
        this.userColumns = userColumns;

        // layout to place buttons
        controlsLayout = new HorizontalLayout();
        controlsLayout.setSizeUndefined();

        // buttons
        buttonAdd = new Button(Messages.getString("add")); //, listener); //$NON-NLS-1$
        setButtonStyle(buttonAdd, ValoTheme.BUTTON_FRIENDLY); //$NON-NLS-1$
        controlsLayout.addComponent(buttonAdd);

        // create and initialize table
        table = new Table();
        table.addStyleName(ValoTheme.TABLE_SMALL);
        table.addStyleName(ValoTheme.TABLE_BORDERLESS);
        table.addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
        table.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        table.setSizeFull();
        table.setSelectable(false);
        table.setMultiSelect(false);
        table.setNullSelectionAllowed(true);
        table.setSortEnabled(!ctrlColumns.contains(CtrlColumn.BTN_SORT));
        table.setValue(null);

        // create controls column
        if (isChbSelection()) {
            table.addContainerProperty("chbSelection", CheckBox.class, null, "", null, null);
        }
        if (isBtnStandardLayout()) {
            table.addContainerProperty("btnStandard", HorizontalLayout.class, null, "", null, null);
        }
        if (isBtnSortLayout()) {
            table.addContainerProperty("btnSort", HorizontalLayout.class, null, "", null, null);
            table.setColumnWidth("btnSort", -1);
        }

        // crate user columns
        for (UserColumnInfo clm : this.userColumns) {
            table.addContainerProperty(clm.propertyId, clm.propertyType, null, clm.caption, null, null);
        }
        if (this.userColumns.length > 0) {
            table.setColumnExpandRatio(this.userColumns[this.userColumns.length - 1].propertyId, 1.0f);
        }
    }

    /**
     * Add component to the area above the table.
     *
     * @param component Component that will be placed above the table
     */
    public void addTableControl(Component component) {
        component.addStyleName("m-table-control");
        controlsLayout.addComponent(component);
    }

    /**
     * Add component at position to the area above the table.
     *
     * @param component Component that will be placed above the table
     * @param index Position of the component.
     */
    public void addTableControl(Component component, int index) {
        component.addStyleName("m-table-control");
        controlsLayout.addComponent(component, index);
    }

    public void removeAllRows() {
        this.table.removeAllItems();
    }

    public void addRow(Object[] userCells, Object itemId) {

        Object[] cells = new Object[userCells.length + getCtrlColumnsCount()];

        int column = 0;

        if (isChbSelection()) {
            cells[column] = new CheckBox();
            ++column;
        }

        if (isBtnStandardLayout()) {
            HorizontalLayout layout = new HorizontalLayout();
            if (ctrlColumns.contains(CtrlColumn.BTN_EDIT)) {
                layout.addComponent(createButton(FontAwesome.PENCIL, null, "m-important"));
            }
            if (ctrlColumns.contains(CtrlColumn.BTN_DELETE)) {
                layout.addComponent(createButton(FontAwesome.MINUS_CIRCLE, null, "m-caution"));
            }
            cells[column] = layout;
            ++column;
        }

        if (isBtnSortLayout()) {
            HorizontalLayout layout = new HorizontalLayout();
            layout.addComponent(createButton(null, "\u25B2", "m-helpful"));
            layout.addComponent(createButton(null, "\u25BC", "m-helpful"));
            cells[column] = layout;
            ++column;
        }

        for (Object value : userCells) {
            cells[column] = value;
            ++column;
        }

        table.addItem(cells, itemId);
    }

    public void addToOwner(AbstractOrderedLayout owner) {
        owner.addComponent(controlsLayout);
        owner.addComponent(table);
        owner.setExpandRatio(table, 1.0f);
        table.setPageLength(21);
    }

    /** Aktualizuje tlacitka dle vybrane radky tabulky */
    public void updateButtons() {
        boolean enable = table.size() > 0;
//        buttonEdit.setEnabled(enable);
//        buttonDelete.setEnabled(enable);
//        if (buttonMoveUp != null) {
//            buttonMoveUp.setEnabled(enable);
//        }
//        if (buttonMoveDown != null) {
//            buttonMoveDown.setEnabled(enable);
//        }
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
            if (parent instanceof View) {
                ((View) parent).enter(null);
            }
            // aktualizace navigace v aplikaci
            if (navigation != null) {
                navigation.updateNavigationMenu();
            }
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
                if (parent instanceof View) {
                    ((View) parent).enter(null);
                }
                // aktualizace navigace v aplikaci
                if (navigation != null) {
                    navigation.updateNavigationMenu();
                }
            }
        } catch (SQLException e) {
            Tools.msgBoxSQLException(e);
        }
    }

    /**
     * Testuje zda je roxIndex platne cislo radky tabulky
     *
     * @param rowIndex testovane cislo radky tabulky
     */
    public boolean checkRowIndex(int rowIndex) {
        return (rowIndex >= 0) && (rowIndex < table.size());
    }

    /**
     * Dopocte novy index pro funkce MoveUp a MoveDown
     *
     * @param moveUp rozlisi dopocet MoveUp a MoveDown
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

    /**
     * Calculate number of columns with controls.
     *
     * @return Number of columns with controls.
     */
    public int getCtrlColumnsCount() {
        int count = 0;
        if (isChbSelection()) {
            ++count;
        }
        if (isBtnStandardLayout()) {
            ++count;
        }
        if (isBtnSortLayout()) {
            ++count;
        }
        return count;
    }

    /* PRIVATE */
    private boolean isChbSelection() {
        return ctrlColumns.contains(CtrlColumn.CBX_SELECTION);
    }

    private boolean isBtnStandardLayout() {
        return ctrlColumns.containsSome(CtrlColumn.BTN_EDIT, CtrlColumn.BTN_DELETE);
    }

    private boolean isBtnSortLayout() {
        return ctrlColumns.contains(CtrlColumn.BTN_SORT);
    }

    private Button createButton(FontAwesome font, String caption, String iconStyle) {
        Button btn = new Button(caption, font);
        btn.addStyleName(ValoTheme.BUTTON_TINY);
        btn.addStyleName(ValoTheme.BUTTON_QUIET);
        btn.addStyleName("table-row");
        if (iconStyle != null) {
            btn.addStyleName(iconStyle);
        }
        return btn;
    }

    private void setButtonStyle(Button button, String style) {
        button.setStyleName(ValoTheme.BUTTON_TINY);
        button.addStyleName("table"); //$NON-NLS-1$
        if (style != null) {
            button.addStyleName(style);
        }
        button.setImmediate(true);
    }

    // Controls
    /** Layout for buttons. */
    private final HorizontalLayout controlsLayout;

    /** Buttons to add new row. */
    private final Button buttonAdd;

    /** Table */
    private final Table table;

    // Table columns definition
    /** Definition of system columns with functional controls. */
    private final SimpleEnumSet<CtrlColumn> ctrlColumns;

    /** Definition of the user columns. */
    private final UserColumnInfo[] userColumns;
}
