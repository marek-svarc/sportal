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
 * Component that contains of table with buttons for single action and buttons
 * for group actions on selected items.
 *
 * @author Marek Svarc
 */
public final class ActionTable {

    /* PUBLIC */
    /** All actions provided by the table */
    public static enum Action {

        SINGLE_ADD, SINGLE_EDIT, SINGLE_DELETE, SINGLE_MOVE_UP, SINGLE_MOVE_DOWN, SELECTED_EDIT, SELECTED_DELETE;

        public static SimpleEnumSet<Action> getStandardSet(boolean useSelection) {
            if (useSelection) {
                return new SimpleEnumSet<>(SINGLE_ADD, SINGLE_EDIT, SINGLE_DELETE, SELECTED_EDIT, SELECTED_DELETE);
            } else {
                return new SimpleEnumSet<>(SINGLE_ADD, SINGLE_EDIT, SINGLE_DELETE);
            }
        }

        public static SimpleEnumSet<Action> getStandardSet() {
            return getStandardSet(false);
        }

        public static SimpleEnumSet<Action> getMaximalSet(boolean useSelection) {
            SimpleEnumSet<Action> flags = new SimpleEnumSet<>(Action.values());
            if (!useSelection) {
                flags.removeValues(SELECTED_EDIT, SELECTED_DELETE);
            }
            return flags;
        }

        public static SimpleEnumSet<Action> getMaximalSet() {
            return getMaximalSet(false);
        }
    }

    /** Enables to perform operation depending on the selected action. */
    public interface OnActionListener {

        /**
         * Perform user action.
         *
         * @param action Identifier of the action.
         * @param data Data needed for operation.
         * @return Returns true if actions is performed .
         */
        boolean doAction(Action action, Object data);
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

    public ActionTable(SimpleEnumSet<Action> ctrlColumns, UserColumnInfo[] userColumns, OnActionListener actionListener) {

        this.userActions = ctrlColumns;
        this.userColumns = userColumns;
        this.onActionListener = actionListener;

        // layout to place buttons
        controlsLayout = new HorizontalLayout();
        controlsLayout.setSizeUndefined();

        // buttons
        Button buttonAdd = new Button(Messages.getString("add"), new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                callOnActionListener(Action.SINGLE_ADD, null);
            }
        });
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
        table.setSortEnabled(!isSelectionColumn());
        table.setValue(null);

        // create controls column
        if (isSelectionColumn()) {
            table.addContainerProperty("selectionClm", CheckBox.class, null, "", null, null);
        }
        if (isEditColumn()) {
            table.addContainerProperty("editClm", HorizontalLayout.class, null, "", null, null);
        }
        if (isSortColumn()) {
            table.addContainerProperty("sortClm", HorizontalLayout.class, null, "", null, null);
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

    /**
     * Set cell style generator for Table.
     *
     * @param generator New cell style generator or null to remove generator.
     */
    public void setCellStyleGenerator(Table.CellStyleGenerator generator) {
        table.setCellStyleGenerator(generator);
    }

    /** Removes all rows from the table. */
    public void removeAllRows() {
        this.table.removeAllItems();
    }

    public void addRow(Object[] userCells, final Object data) {

        Object[] cells = new Object[userCells.length + getCtrlColumnsCount()];

        int column = 0;

        if (isSelectionColumn()) {
            cells[column] = new CheckBox();
            ++column;
        }

        if (isEditColumn()) {
            HorizontalLayout layout = new HorizontalLayout();
            if (userActions.contains(Action.SINGLE_EDIT)) {
                layout.addComponent(createInRowButton(FontAwesome.PENCIL, null, "m-important", new Button.ClickListener() {

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        callOnActionListener(Action.SINGLE_EDIT, data);
                    }
                }));
            }
            if (userActions.contains(Action.SINGLE_DELETE)) {
                layout.addComponent(createInRowButton(FontAwesome.MINUS_CIRCLE, null, "m-caution", new Button.ClickListener() {

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        callOnActionListener(Action.SINGLE_DELETE, data);
                    }
                }));
            }
            cells[column] = layout;
            ++column;
        }

        if (isSortColumn()) {
            HorizontalLayout layout = new HorizontalLayout();
            layout.addComponent(createInRowButton(null, "\u25B2", "m-helpful", new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    callOnActionListener(Action.SINGLE_MOVE_UP, data);
                }
            }));
            layout.addComponent(createInRowButton(null, "\u25BC", "m-helpful", new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    callOnActionListener(Action.SINGLE_MOVE_DOWN, data);
                }
            }));
            cells[column] = layout;
            ++column;
        }

        for (Object value : userCells) {
            cells[column] = value;
            ++column;
        }

        table.addItem(cells, data);
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

    /**
     * Delete one row from the table.
     *
     * @param <T>
     * @param id Unique id of deleted object
     * @param dataAdmin
     * @param parent
     * @param navigation
     */
    public <T extends Unique> void deleteRow(int id, final Repository<T> dataAdmin, final Component parent,
            final Navigation navigation) {
        if (id > 0) {
            // delete from the database
            try {
                dataAdmin.deleteRow(id);
            } catch (SQLException e) {
                Tools.msgBoxSQLException(e);
            }
            // update view
            if (parent instanceof View) {
                ((View) parent).enter(null);
            }
            // aupdate navigation menu
            if (navigation != null) {
                navigation.updateNavigationMenu();
            }
        }
    }

    /**
     * Calculate number of columns with controls.
     *
     * @return Number of columns with controls.
     */
    public int getCtrlColumnsCount() {
        int count = 0;
        if (isSelectionColumn()) {
            ++count;
        }
        if (isEditColumn()) {
            ++count;
        }
        if (isSortColumn()) {
            ++count;
        }
        return count;
    }

    /* PRIVATE */
    private boolean callOnActionListener(Action action, Object data) {
        if (onActionListener != null) {
            return onActionListener.doAction(action, data);
        } else {
            return false;
        }
    }

    private boolean isSelectionColumn() {
        return userActions.containsSome(Action.SELECTED_EDIT, Action.SELECTED_DELETE);
    }

    private boolean isEditColumn() {
        return userActions.containsSome(Action.SINGLE_EDIT, Action.SINGLE_EDIT);
    }

    private boolean isSortColumn() {
        return userActions.containsSome(Action.SINGLE_MOVE_UP, Action.SINGLE_MOVE_DOWN);
    }

    private Button createInRowButton(FontAwesome font, String caption, String iconStyle, Button.ClickListener listener) {
        Button btn = new Button(caption, font);
        btn.addClickListener(listener);
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

    /** Table */
    private final Table table;

    // Customizing
    /** Set of required actions. */
    private final SimpleEnumSet<Action> userActions;

    /** Set of the required columns. */
    private final UserColumnInfo[] userColumns;

    /** Calling when action is performed. */
    private final OnActionListener onActionListener;
}
