package com.clubeek.ui.views;

import java.util.List;

import com.clubeek.db.RepCategory;
import com.clubeek.model.Category;
import com.clubeek.model.User.Role;
import com.clubeek.service.SecurityService;
import com.clubeek.service.impl.Security;
import com.clubeek.service.impl.SecurityServiceImpl;
import com.clubeek.ui.ModalDialog;
import com.clubeek.ui.Tools;
import com.clubeek.ui.ModalDialog.Mode;
import com.clubeek.ui.components.ActionTable;
import com.clubeek.ui.frames.FrameCategory;
import com.vaadin.data.Container;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ViewCategories extends VerticalLayout implements View, ActionTable.OnActionListener {
	// TODO vitfo, created on 11. 6. 2015 
	private SecurityService securityService = new SecurityServiceImpl();

    /* PUBLIC */
    public enum Columns {

        CAPTION;
    }

    public ViewCategories(Navigation navigation) {

        this.setCaption(Messages.getString("category"));
        this.navigation = navigation;

        ActionTable.UserColumnInfo[] columns = {
            new ActionTable.UserColumnInfo(Columns.CAPTION, String.class, Messages.getString("caption"))
        };

        table = new ActionTable(ActionTable.Action.getMaximalSet(false, true), columns, this);
        table.addToOwner(this);
    }

    // interface View
    @Override
    public void enter(ViewChangeEvent event) {

    	securityService.authorize(Role.CLUB_MANAGER);

        categories = RepCategory.selectAll(null);

        table.removeAllRows();
        if (categories != null) {
            Container container = table.createDataContainer();
            for (int i = 0; i < categories.size(); ++i) {
                Category category = categories.get(i);
                table.addRow(container, new Object[]{Tools.Strings.getCheckString(category.getActive())
                    + "  " + category.getDescription()}, i);
            }
            table.setDataContainer(container);
        }
    }

    // interface ActionTable.OnActionListener
    @Override
    public boolean doAction(ActionTable.Action action, Object data) {
        switch (action) {
            case SINGLE_ADD:
                addCategory();
                break;
            case SINGLE_EDIT:
                editCategory((int) data);
                break;
            case SINGLE_DELETE:
                deleteCategory((int) data);
                break;
            case SINGLE_MOVE_UP:
                exchangeCategories((int) data, true);
                break;
            case SINGLE_MOVE_DOWN:
                exchangeCategories((int) data, false);
                break;
        }
        return true;
    }

    // operations
    public void addCategory() {
        ModalDialog.show(this, Mode.ADD_ONCE, Messages.getString("categoryProperties"), new FrameCategory(), new Category(), //$NON-NLS-1$
                RepCategory.getInstance(), this.navigation);
    }

    public void editCategory(int id) {
        Category category = categories.get(id);
        ModalDialog.show(this, Mode.EDIT, Messages.getString("categoryProperties"), new FrameCategory(), category, //$NON-NLS-1$
                RepCategory.getInstance(), this.navigation);
    }

    public void deleteCategory(int id) {
        Category category = categories.get(id);
        table.deleteRow(category.getId(), id, RepCategory.getInstance(), this, navigation, Columns.CAPTION);
    }

    public void exchangeCategories(int id, boolean moveUp) {
        table.exchangeRows(categories, id, moveUp, RepCategory.getInstance(), this, navigation);
    }

    /* PRIVATE */
    /** Komponenty tabulky */
    private final ActionTable table;

    /** Seznam aktualne zobrazenych kategorii */
    private List<Category> categories = null;

    /** Navigace v aplikaci */
    private final Navigation navigation;
}
