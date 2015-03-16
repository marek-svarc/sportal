package com.clubeek.ui.views;

import java.sql.SQLException;
import java.util.List;

import com.clubeek.db.RepClubTeam;
import com.clubeek.model.ClubTeam;
import com.clubeek.model.User.Role;
import com.clubeek.ui.ModalDialog;
import com.clubeek.ui.Security;
import com.clubeek.ui.Tools;
import com.clubeek.ui.ModalDialog.Mode;
import com.clubeek.ui.components.TableWithButtons;
import com.clubeek.ui.frames.FrameTeam;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ViewClubTeams extends VerticalLayout implements View {

    public enum Columns {

        CAPTION, CATEGORY
    }

    public ViewClubTeams(Navigation navigation) {

        this.setCaption(Messages.getString("teams")); //$NON-NLS-1$
        this.navigation = navigation;

        TableWithButtons.UserColumnInfo[] columns = {
            new TableWithButtons.UserColumnInfo(Columns.CAPTION, String.class, Messages.getString("caption")),
            new TableWithButtons.UserColumnInfo(Columns.CATEGORY, String.class, Messages.getString("category"))
        };

        table = new TableWithButtons(TableWithButtons.CtrlColumn.getMaximalSet(), columns, null);
        table.addToOwner(this);
    }

    // interface View
    @Override
    public void enter(ViewChangeEvent event) {

        Security.authorize(Role.SPORT_MANAGER);

        try {
            teams = RepClubTeam.select(false, null);

            table.removeAllRows();
            for (int i = 0; i < teams.size(); ++i) {
                ClubTeam team = teams.get(i);
                table.addRow(new Object[]{Tools.Strings.getCheckString(team.getActive()) + "  " + team.getName(),
                    team.getCategory() != null ? team.getCategory().toString() : Messages.getString("categoryNotAssigned")}, i);
            }
        } catch (SQLException e) {
            Tools.msgBoxSQLException(e);
        }
    }

    /** Spusti modalni dialog pro pridani noveho tymu */
    public void addTeam() {
        ModalDialog.show(this, Mode.ADD_ONCE, Messages.getString("newTeam"), new FrameTeam(), new ClubTeam(), RepClubTeam.getInstance(), //$NON-NLS-1$
                navigation);
    }

    /** Spusti modalni dialog pro zmenu vlastnosti tymu */
    public void editSelectedTeam() {
        if (table.getSelectedRow() >= 0) {
            ModalDialog.show(this, Mode.EDIT, Messages.getString("teamProperties"), new FrameTeam(), //$NON-NLS-1$
                    teams.get(table.getSelectedRow()), RepClubTeam.getInstance(), navigation);
        }
    }

    /** Zobrazi dotaz uzivateli a pripadne odstrani vybrany tym */
    public void deleteSelectedTeam() {
        try {
            // vymazani prvku z databaze
            if ((table.getSelectedRow() >= 0) && (table.getSelectedRow() < teams.size())) {
                RepClubTeam.delete(teams.get(table.getSelectedRow()).getId());
                updateApp();
            }
        } catch (SQLException e) {
            Tools.msgBoxSQLException(e);
        }
    }

    /**
     * Posune vybranou radku nahoru nebo dolu
     *
     * @param moveUp směr posunu
     */
    public void exchangeCategories(boolean moveUp) {
        int idA = table.getSelectedRow();
        int idB = table.getMoveIndex(moveUp);
        if ((idA >= 0) && (idB >= 0)) {
            try {
                // prohozen
                RepClubTeam.exchange(teams.get(idA).getId(), teams.get(idB).getId());
                // změna vybrané řádky na novou pozici
                // table.table.setValue(idB);
                // aktualizace aplikace
                updateApp();
            } catch (SQLException e) {
                Tools.msgBoxSQLException(e);
            }
        }
    }

    /* PRIVATE */
    private void updateApp() {
        // aktualizce tabulky
        enter(null);
        // aktualizace navigacniho menu
        if (navigation != null) {
            navigation.updateNavigationMenu();
        }
    }

    /** Komponenty tabulky */
    private TableWithButtons table;

    /** Seznam aktuálně zobrazených kategorií */
    private List<ClubTeam> teams = null;

    /** Navigace v aplikaci */
    private Navigation navigation;
}
