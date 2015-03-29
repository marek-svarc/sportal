package com.clubeek.ui.views;

import java.util.List;

import com.clubeek.db.RepClubTeam;
import com.clubeek.model.ClubTeam;
import com.clubeek.model.User.Role;
import com.clubeek.ui.ModalDialog;
import com.clubeek.ui.Security;
import com.clubeek.ui.Tools;
import com.clubeek.ui.ModalDialog.Mode;
import com.clubeek.ui.components.ActionTable;
import com.clubeek.ui.frames.FrameTeam;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ViewClubTeams extends VerticalLayout implements View, ActionTable.OnActionListener {

    public enum Columns {

        CAPTION, CATEGORY;
    }

    public ViewClubTeams(Navigation navigation) {

        this.setCaption(Messages.getString("teams")); //$NON-NLS-1$
        this.navigation = navigation;

        ActionTable.UserColumnInfo[] columns = {
            new ActionTable.UserColumnInfo(Columns.CAPTION, String.class, Messages.getString("caption")),
            new ActionTable.UserColumnInfo(Columns.CATEGORY, String.class, Messages.getString("category"))
        };

        table = new ActionTable(ActionTable.Action.getMaximalSet(), columns, this);
        table.addToOwner(this);
    }

    // interface View
    @Override
    public void enter(ViewChangeEvent event) {

        Security.authorize(Role.SPORT_MANAGER);

        teams = RepClubTeam.select(false, null);
        table.removeAllRows();
        for (int i = 0; i < teams.size(); ++i) {
            ClubTeam team = teams.get(i);
            table.addRow(new Object[]{Tools.Strings.getCheckString(team.getActive()) + "  " + team.getName(),
                team.getCategory() != null ? team.getCategory().toString() : Messages.getString("categoryNotAssigned")}, i);
        }
    }

    // interface ActionTable.OnActionListener
    @Override
    public boolean doAction(ActionTable.Action action, Object data) {
        switch (action) {
            case SINGLE_ADD:
                addTeam();
                break;
            case SINGLE_EDIT:
                editTeam((int) data);
                break;
            case SINGLE_DELETE:
                deleteTeam((int) data);
                break;
        }
        return true;
    }
    
    // Operations
    public void addTeam() {
        ModalDialog.show(this, Mode.ADD_ONCE, Messages.getString("newTeam"),
                new FrameTeam(), new ClubTeam(), RepClubTeam.getInstance(), navigation);
    }

    public void editTeam(int id) {
        if (id >= 0) {
            ModalDialog.show(this, Mode.EDIT, Messages.getString("teamProperties"), new FrameTeam(),
                    teams.get(id), RepClubTeam.getInstance(), navigation);
        }
    }

    public void deleteTeam(int id) {
        table.deleteRow(teams.get(id).getId(), RepClubTeam.getInstance(), this, navigation);
    }

    /**
     * Posune vybranou radku nahoru nebo dolu
     *
     * @param moveUp směr posunu
     */
    public void exchangeCategories(boolean moveUp) {
//        int idA = table.getSelectedRow();
//        int idB = table.getMoveIndex(moveUp);
//        if ((idA >= 0) && (idB >= 0)) {
//            try {
//                // prohozen
//                RepClubTeam.exchange(teams.get(idA).getId(), teams.get(idB).getId());
//                // změna vybrané řádky na novou pozici
//                // table.table.setValue(idB);
//                // aktualizace aplikace
//                updateApp();
//            } catch (SQLException e) {
//                Tools.msgBoxSQLException(e);
//            }
//        }
    }

    /* PRIVATE */

    /** Navigation provider */
    private final Navigation navigation;

    /** Table component */
    private final ActionTable table;

    /** List of teams loaded from tha database */
    private List<ClubTeam> teams = null;
}
