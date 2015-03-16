package com.clubeek.ui.views;

import java.sql.SQLException;
import java.util.List;

import com.clubeek.db.RepCategory;
import com.clubeek.model.Category;
import com.clubeek.model.User.Role;
import com.clubeek.ui.ModalDialog;
import com.clubeek.ui.Security;
import com.clubeek.ui.Tools;
import com.clubeek.ui.ModalDialog.Mode;
import com.clubeek.ui.components.TableWithButtons;
import com.clubeek.ui.frames.FrameCategory;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("baseTheme")
public class ViewCategories extends VerticalLayout implements View {

    /* PUBLIC */
    public enum Columns {

        CAPTION;
    }

    public ViewCategories(Navigation navigation) {

        this.setCaption(Messages.getString("category"));
        this.navigation = navigation;

        TableWithButtons.UserColumnInfo[] columns = {
            new TableWithButtons.UserColumnInfo(Columns.CAPTION, String.class, Messages.getString("caption"))
        };

        table = new TableWithButtons(TableWithButtons.CtrlColumn.getMaximalSet(), columns, null);
        table.addToOwner(this);
    }

    // interface View
    @Override
    public void enter(ViewChangeEvent event) {

        Security.authorize(Role.CLUB_MANAGER);

        try {
            categories = RepCategory.selectAll(null);

            table.removeAllRows();
            for (int i = 0; i < categories.size(); ++i) {
                Category category = categories.get(i);
                table.addRow(new Object[]{Tools.Strings.getCheckString(category.getActive())
                    + "  " + category.getDescription()}, new Integer(i));
            }
        } catch (SQLException e) {
            Tools.msgBoxSQLException(e);
        }
    }

    // operations
    /** Spusti modalni dialog pro pridani nove ktegorie */
    public void addCategory() {
        ModalDialog.show(this, Mode.ADD_ONCE, Messages.getString("categoryProperties"), new FrameCategory(), new Category(), //$NON-NLS-1$
                RepCategory.getInstance(), this.navigation);
    }

    /** Spusti modalni dialog pro zmenu vlastnosti vybrane ktegorie */
    public void editSelectedCategory() {
        if (table.getSelectedRow() >= 0) {
            Category category = categories.get(table.getSelectedRow());
            ModalDialog.show(this, Mode.EDIT, Messages.getString("categoryProperties"), new FrameCategory(), category, //$NON-NLS-1$
                    RepCategory.getInstance(), this.navigation);
        }
    }

    /** Zobrazi dotaz uzivateli a pripadne odstrani vybranou ktegorii */
    public void deleteSelectedCategory() {
        try {
            // vymazani prvku z databaze
            if ((table.getSelectedRow() >= 0) && (table.getSelectedRow() < categories.size())) {
                RepCategory.delete(categories.get(table.getSelectedRow()).getId());
            }
            updateApp();
        } catch (SQLException e) {
            Tools.msgBoxSQLException(e);
        }
    }

    /**
     * Posune vybranou radku nahoru nebo dolu
     *
     * @param moveUp smer posunu
     */
    public void exchangeCategories(boolean moveUp) {
        int idA = table.getSelectedRow();
        int idB = table.getMoveIndex(moveUp);
        if ((idA >= 0) && (idB >= 0)) {
            try {
                RepCategory.exchange(categories.get(idA).getId(), categories.get(idB).getId());
                //table.table.setValue(idB);
                updateApp();
            } catch (SQLException e) {
                Tools.msgBoxSQLException(e);
            }
        }
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
    private final TableWithButtons table;

    /** Seznam aktualne zobrazenych kategorii */
    private List<Category> categories = null;

    /** Navigace v aplikaci */
    private final Navigation navigation;
}
