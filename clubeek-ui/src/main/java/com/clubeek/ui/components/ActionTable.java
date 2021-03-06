package com.clubeek.ui.components;

import java.util.List;
import java.util.Locale;

import org.tepi.filtertable.FilterDecorator;
import org.tepi.filtertable.FilterGenerator;
import org.tepi.filtertable.FilterTable;
import org.tepi.filtertable.numberfilter.NumberFilterPopupConfig;

import com.clubeek.dao.Dao;
import com.clubeek.model.IUnique;
import com.clubeek.ui.Messages;
import com.clubeek.ui.Tools;
import com.clubeek.ui.views.Navigation;
import com.clubeek.util.DateTime;
import com.clubeek.util.SimpleEnumSet;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Component that contains of table with buttons for single action and buttons
 * for group actions on selected items.
 *
 * @author Marek Svarc
 */
public final class ActionTable {

    /* PUBLIC */
    /** Table */
    public final FilterTable table;

    /** All actions provided by the table */
    public static enum Action {

        SINGLE_ADD, SINGLE_EDIT, SINGLE_DELETE, SINGLE_MOVE_UP, SINGLE_MOVE_DOWN, SELECTED_EDIT, SELECTED_DELETE;

        public static SimpleEnumSet<Action> getStandardSet(boolean useSelectedEdit, boolean useSelectedDelete) {
            SimpleEnumSet<Action> flags = new SimpleEnumSet<>(SINGLE_ADD, SINGLE_EDIT, SINGLE_DELETE);

            if (useSelectedEdit) {
                flags.addValue(SELECTED_EDIT);
            }
            if (useSelectedDelete) {
                flags.addValue(SELECTED_DELETE);
            }

            return flags;
        }

        public static SimpleEnumSet<Action> getStandardSet() {
            return getStandardSet(false, false);
        }

        public static SimpleEnumSet<Action> getMaximalSet(boolean useSelectedEdit, boolean useSelectedDelete) {
            SimpleEnumSet<Action> flags = new SimpleEnumSet<>(Action.values());

            if (!useSelectedEdit) {
                flags.removeValues(SELECTED_EDIT);
            }
            if (!useSelectedDelete) {
                flags.removeValues(SELECTED_DELETE);
            }

            return flags;
        }

        public static SimpleEnumSet<Action> getMaximalSet() {
            return getMaximalSet(false, false);
        }
    }

    /** Identifiers of columns which provides some action. */
    public enum ActionColumns {

        SELECTION, EDIT, SORT;
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

    // Classes
    /** Basic class for filters decoration */
    public static class FilterDecoratorBase implements FilterDecorator {

        @Override
        public String getEnumFilterDisplayName(Object propertyId, Object value) {
            return value.toString();
        }

        @Override
        public Resource getEnumFilterIcon(Object propertyId, Object value) {
            return null;
        }

        @Override
        public String getBooleanFilterDisplayName(Object propertyId, boolean value) {
            return value ? "ano" : "ne";
        }

        @Override
        public Resource getBooleanFilterIcon(Object propertyId, boolean value) {
            return value ? FontAwesome.CHECK : FontAwesome.TIMES;
        }

        @Override
        public boolean isTextFilterImmediate(Object propertyId) {
            return true;
        }

        @Override
        public int getTextChangeTimeout(Object propertyId) {
            return 500;
        }

        @Override
        public String getFromCaption() {
            return "Od";
        }

        @Override
        public String getToCaption() {
            return "Do";
        }

        @Override
        public String getSetCaption() {
            return null;
        }

        @Override
        public String getClearCaption() {
            return null;
        }

        @Override
        public Resolution getDateFieldResolution(Object propertyId) {
            return Resolution.DAY;
        }

        @Override
        public String getDateFormatPattern(Object propertyId) {
            return DateTime.getDateFormatString(DateTime.DateStyle.DAY);
        }

        @Override
        public Locale getLocale() {
            return Tools.getLocale();
        }

        @Override
        public String getAllItemsVisibleString() {
            return "Vše";
        }

        @Override
        public NumberFilterPopupConfig getNumberFilterPopupConfig() {
            return null;
        }

        @Override
        public boolean usePopupForNumericProperty(Object propertyId) {
            return true;
        }
    }

    public static class FilterGeneratorBase implements FilterGenerator {

        @Override
        public Container.Filter generateFilter(Object propertyId, Object value) {
            return null;
        }

        @Override
        public Container.Filter generateFilter(Object propertyId, Field<?> originatingField) {
            return null;
        }

        @Override
        public AbstractField<?> getCustomFilterComponent(Object propertyId) {
            return null;
        }

        @Override
        public void filterRemoved(Object propertyId) {
            //
        }

        @Override
        public void filterAdded(Object propertyId, Class<? extends Container.Filter> filterType, Object value) {
            //
        }

        @Override
        public Container.Filter filterGeneratorFailed(Exception reason, Object propertyId, Object value) {
            return null;
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

    public ActionTable(SimpleEnumSet<Action> ctrlColumns, UserColumnInfo[] userColumns, OnActionListener actionListener) {
        this(ctrlColumns, userColumns, actionListener, new ActionTable.FilterDecoratorBase(), new ActionTable.FilterGeneratorBase());
    }

    public ActionTable(SimpleEnumSet<Action> ctrlColumns, UserColumnInfo[] userColumns, OnActionListener actionListener,
            FilterDecorator filterDecorator, FilterGenerator filterGenerator) {

        this.userActions = ctrlColumns;
        this.userColumns = userColumns;
        this.onActionListener = actionListener;

        // layout to place buttons
        controlsLayout = new HorizontalLayout();
        controlsLayout.setSizeUndefined();

        // header buttons
        if (ctrlColumns.contains(Action.SINGLE_ADD)) {
            controlsLayout.addComponent(createHeaderButton(Action.SINGLE_ADD, FontAwesome.PLUS_CIRCLE,
                    Messages.getString("add"), "m-helpful"));
        }

        if (ctrlColumns.contains(Action.SELECTED_EDIT)) {
            btnSelectedEdit = createHeaderButton(Action.SELECTED_EDIT, FontAwesome.PENCIL, "Upravit vybrané",
                    "m-important");
            controlsLayout.addComponent(btnSelectedEdit);
        } else {
            btnSelectedEdit = null;
        }

        if (ctrlColumns.contains(Action.SELECTED_DELETE)) {
            btnSelectedDelete = createHeaderButton(Action.SELECTED_DELETE, FontAwesome.MINUS_CIRCLE, "Odstranit vybrané",
                    "m-caution");
            controlsLayout.addComponent(btnSelectedDelete);
        } else {
            btnSelectedDelete = null;
        }

        if ((btnSelectedEdit != null) && (btnSelectedDelete != null)) {
            btnSelectedEdit.addStyleName("hgroup-left");
            btnSelectedDelete.addStyleName("hgroup-right");
        }

        // create and initialize table
        table = new FilterTable();
        table.addStyleName(ValoTheme.TABLE_COMPACT);
        table.addStyleName(ValoTheme.TABLE_SMALL);
        table.addStyleName(ValoTheme.TABLE_BORDERLESS);
        table.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        table.setSizeFull();
        table.setValue(null);

        if (!isSortColumn() && (filterDecorator != null) && (filterGenerator != null)) {
            table.setFilterDecorator(filterDecorator);
            table.setFilterGenerator(filterGenerator);
            table.setFilterBarVisible(true);
        }

        table.setSelectable(false);
        table.setMultiSelect(false);
        table.setNullSelectionAllowed(true);
        table.setSortEnabled(!isSortColumn());

        // create controls column
        if (isSelectionColumn()) {
            table.addContainerProperty(ActionColumns.SELECTION, CheckBox.class, null, "", null, null);
        }
        if (isEditColumn()) {
            table.addContainerProperty(ActionColumns.EDIT, HorizontalLayout.class, null, "Úpravy", null, null);
        }
        if (isSortColumn()) {
            table.addContainerProperty(ActionColumns.SORT, HorizontalLayout.class, null, "Pořadí", null, null);
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
    public void setCellStyleGenerator(CustomTable.CellStyleGenerator generator) {
        table.setCellStyleGenerator(generator);
    }

    /** Removes all rows from the table. */
    public void removeAllRows() {
        this.table.removeAllItems();
    }

    public Container createDataContainer() {
        IndexedContainer cont = new IndexedContainer();

        // create controls column
        if (isSelectionColumn()) {
            cont.addContainerProperty(ActionColumns.SELECTION, CheckBox.class, null);
        }
        if (isEditColumn()) {
            cont.addContainerProperty(ActionColumns.EDIT, HorizontalLayout.class, null);
        }
        if (isSortColumn()) {
            cont.addContainerProperty(ActionColumns.SORT, HorizontalLayout.class, null);
        }

        // crate user columns
        for (UserColumnInfo clm : this.userColumns) {
            cont.addContainerProperty(clm.propertyId, clm.propertyType, null);
        }

        return cont;
    }

    public void setDataContainer(Container container) {
        table.setContainerDataSource(container);
        if (isSelectionColumn()) {
            table.setFilterFieldVisible(ActionTable.ActionColumns.SELECTION, false);
        }
        if (isEditColumn()) {
            table.setFilterFieldVisible(ActionTable.ActionColumns.EDIT, false);
        }
        if (isSortColumn()) {
            table.setFilterFieldVisible(ActionTable.ActionColumns.SORT, false);
        }
    }

    public void addRow(Container container, Object[] userCells, final Object data) {
        container.addItem(data);

        // Column to select row
        if (isSelectionColumn()) {
            CheckBox cbx = new CheckBox();
            container.getContainerProperty(data, ActionColumns.SELECTION).setValue(cbx);
        }

        // Column for actions SINGLE_EDIT and SINGLE_DELETE
        if (isEditColumn()) {
            HorizontalLayout layout = new HorizontalLayout();
            if (userActions.contains(Action.SINGLE_EDIT)) {
                layout.addComponent(createTableButton(Action.SINGLE_EDIT, data, FontAwesome.PENCIL, null, "m-important"));
            }
            if (userActions.contains(Action.SINGLE_DELETE)) {
                layout.addComponent(createTableButton(Action.SINGLE_DELETE, data, FontAwesome.MINUS_CIRCLE, null, "m-caution"));
            }

            container.getContainerProperty(data, ActionColumns.EDIT).setValue(layout);
        }

        // Column for actions SINGLE_MOVE_UP and SINGLE_MOVE_DOWN
        if (isSortColumn()) {
            HorizontalLayout layout = new HorizontalLayout();
            layout.addComponent(createTableButton(Action.SINGLE_MOVE_UP, data, null, "\u25B2", "m-helpful"));
            layout.addComponent(createTableButton(Action.SINGLE_MOVE_DOWN, data, null, "\u25BC", "m-helpful"));

            container.getContainerProperty(data, ActionColumns.SORT).setValue(layout);
        }

        // Custom columns
        for (int i = 0; i < userCells.length; ++i) {
            container.getContainerProperty(data, userColumns[i].propertyId).setValue(userCells[i]);
        }
    }

    public void addToOwner(AbstractOrderedLayout owner) {
        owner.addComponent(controlsLayout);
        owner.addComponent(table);
        owner.setExpandRatio(table, 1.0f);
        table.setPageLength(21);
    }

    /**
     * Delete one row from the table.
     *
     * @param <T>
     * @param dataId Unique id of deleted object
     * @param rowId Unique id of the table row
     * @param dataAdmin DB manager providing delete operation
     * @param parent Parent control used to update application
     * @param navigation Navigation to update navigation in application
     * @param columns Columns used to create information string about the deleting item
     */
    public <T extends IUnique> void deleteRow(final int dataId, int rowId, final Dao<T> dataAdmin, final Component parent,
            final Navigation navigation, Object... columns) {
        if (dataId > 0) {
            ConfirmDialog.addConfirmDialog(parent.getCaption(), String.format("<strong>%s</strong><br><em>( %s )</em><br>",
                    "Opravdu chcete smazat vybranou položku?", getTableRowInfo(rowId, columns)),
                    new ConfirmDialog.Confirmation() {

                        @Override
                        public void onConfirm() {
                            dataAdmin.deleteRow(dataId);
                            updateApplication(parent, navigation);
                        }
                    });
        }
    }

    public <T extends IUnique> void exchangeRows(List<T> data, int id, boolean moveUp,
            final Dao<T> dataAdmin, final Component parent, final Navigation navigation) {
        int idNext = id + (moveUp ? -1 : 1);
        if ((id >= 0) && (id < data.size()) && (idNext >= 0) && (idNext < data.size())) {
            dataAdmin.exchangeRows(data.get(id).getId(), data.get(idNext).getId());
            updateApplication(parent, navigation);
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

    public String getTableRowInfo(int rowId, Object... columns) {
        Item item = table.getItem(rowId);

        String info = "";
        if (item != null) {
            for (Object id : columns) {
                String value = (String) item.getItemProperty(id).getValue();
                if ((value != null) && (!value.isEmpty())) {
                    if (!info.isEmpty()) {
                        info += ", ";
                    }
                    info += value;
                }
            }
        }

        return info;
    }

    public void styleTableButton(Button button, String... customStyles) {
        button.addStyleName(ValoTheme.BUTTON_TINY);
        button.addStyleName(ValoTheme.BUTTON_QUIET);
        button.addStyleName("table-row");

        for (String style : customStyles) {
            button.addStyleName(style);
        }
    }

    public void styleHeaderButton(Button button, String... customStyles) {
        button.addStyleName(ValoTheme.BUTTON_TINY);
        button.addStyleName("table");

        for (String style : customStyles) {
            button.addStyleName(style);
        }
    }

    /* PRIVATE */
    private void updateApplication(final Component parent, final Navigation navigation) {
        // update view
        if (parent instanceof View) {
            ((View) parent).enter(null);
        }
        // aupdate navigation menu
        if (navigation != null) {
            navigation.updateNavigationMenu();
        }
    }

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

    private Button createTableButton(final Action action, final Object data, FontAwesome font, String caption, String... customStyles) {
        Button btn = new Button(caption, font);
        btn.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                callOnActionListener(action, data);
            }
        });

        styleTableButton(btn, customStyles);

        return btn;
    }

    public Button createHeaderButton(final Action action, FontAwesome font, String caption, String... customStyles) {
        Button btn = new Button(caption, font);

        btn.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                callOnActionListener(action, null);
            }
        });

        styleHeaderButton(btn, customStyles);

        btn.setImmediate(true);
        return btn;
    }

    // Controls
    /** Layout for buttons. */
    private final HorizontalLayout controlsLayout;

    /** Button to edit selected rows */
    private final Button btnSelectedEdit;

    /** Button to delete selected rows */
    private final Button btnSelectedDelete;

    // Customizing
    /** Set of required actions. */
    private final SimpleEnumSet<Action> userActions;

    /** Set of the required columns. */
    private final UserColumnInfo[] userColumns;

    /** Calling when action is performed. */
    private final OnActionListener onActionListener;
}
