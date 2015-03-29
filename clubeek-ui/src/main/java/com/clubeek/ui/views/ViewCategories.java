package com.clubeek.ui.views;

import java.util.List;

import com.clubeek.db.RepCategory;
import com.clubeek.model.Category;
import com.clubeek.model.User.Role;
import com.clubeek.ui.ModalDialog;
import com.clubeek.ui.Security;
import com.clubeek.ui.Tools;
import com.clubeek.ui.ModalDialog.Mode;
import com.clubeek.ui.components.ActionTable;
import com.clubeek.ui.frames.FrameCategory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ViewCategories extends VerticalLayout implements View, ActionTable.OnActionListener {

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

        table = new ActionTable(ActionTable.Action.getMaximalSet(), columns, this);
        table.addToOwner(this);
    }

    // interface View
    @Override
    public void enter(ViewChangeEvent event) {

        Security.authorize(Role.CLUB_MANAGER);

        categories = RepCategory.selectAll(null);
        table.removeAllRows();
        for (int i = 0; i < categories.size(); ++i) {
            Category category = categories.get(i);
            table.addRow(new Object[]{Tools.Strings.getCheckString(category.getActive())
                    + "  " + category.getDescription()}, i);
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
        table.deleteRow(categories.get(id).getId(), RepCategory.getInstance(), this, navigation);
    }

    /**
     * Posune vybranou radku nahoru nebo dolu
     *
     * @param moveUp smer posunu
     */
    public void exchangeCategories(boolean moveUp) {
//        int idA = table.getSelectedRow();
//        int idB = table.getMoveIndex(moveUp);
//        if ((idA >= 0) && (idB >= 0)) {
//            try {
//                RepCategory.exchange(categories.get(idA).getId(), categories.get(idB).getId());
//                //table.table.setValue(idB);
//                updateApp();
//            } catch (SQLException e) {
//                Tools.msgBoxSQLException(e);
//            }
//        }
    }

    /* PRIVATE */
    /** Aktualizaje tabulky i navigacni menu aplikace */
    private void updateApp() {
        // aktualizace tabulky
        enter(null);
        // aktualizace navigacniho menu
        if (navigation != null) {
            navigation.updateNavigationMenu();
        }
    }

    /** Komponenty tabulky */
    private final ActionTable table;

    /** Seznam aktualne zobrazenych kategorii */
    private List<Category> categories = null;

    /** Navigace v aplikaci */
    private final Navigation navigation;
}
