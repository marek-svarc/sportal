package com.clubeek.ui.views;

import java.util.List;

import com.clubeek.dao.CategoryDao;
import com.clubeek.dao.impl.ownframework.CategoryDaoImpl;
import com.clubeek.enums.UserRoleType;
import com.clubeek.model.Category;
import com.clubeek.service.SecurityService;
import com.clubeek.service.impl.SecurityServiceImpl;
import com.clubeek.ui.ModalDialog;
import com.clubeek.ui.ModalDialog.Mode;
import com.clubeek.ui.Tools;
import com.clubeek.ui.components.ActionTable;
import com.clubeek.ui.frames.FrameCategory;
import com.vaadin.data.Container;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@SuppressWarnings("serial")
@Component
@Scope("prototype")
public class ViewCategories extends VerticalLayout implements View, ActionTable.OnActionListener {

    /* PRIVATE */

    @Autowired
    private SecurityService securityService;

    @Autowired
    private CategoryDao categoryDao;

    /** Application navigation provider */
    private Navigation navigation;
    
    /** Categories table */
    private ActionTable table;

    /** List of categories */
    private List<Category> categories = null;

    /* PUBLIC */
    public enum Columns {

        CAPTION;
    }

    public ViewCategories() {

        this.setCaption(Messages.getString("category"));

        ActionTable.UserColumnInfo[] columns = {
            new ActionTable.UserColumnInfo(Columns.CAPTION, String.class, Messages.getString("caption"))
        };

        table = new ActionTable(ActionTable.Action.getMaximalSet(false, true), columns, this);
        table.addToOwner(this);
    }

    // interface View
    @Override
    public void enter(ViewChangeEvent event) {
        this.navigation = (Navigation) getUI().getContent();

        securityService.authorize(UserRoleType.CLUB_MANAGER);

        categories = categoryDao.getAllCategories();

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
                categoryDao, this.navigation);
    }

    public void editCategory(int id) {
        Category category = categories.get(id);
        ModalDialog.show(this, Mode.EDIT, Messages.getString("categoryProperties"), new FrameCategory(), category, //$NON-NLS-1$
                categoryDao, this.navigation);
    }

    public void deleteCategory(int id) {
        Category category = categories.get(id);
        table.deleteRow(category.getId(), id, categoryDao, this, navigation, Columns.CAPTION);
    }

    public void exchangeCategories(int id, boolean moveUp) {
        table.exchangeRows(categories, id, moveUp, categoryDao, this, navigation);
    }
}
