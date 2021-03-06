package com.clubeek.ui.views;

import java.util.List;

import com.clubeek.dao.ClubTeamDao;
import com.clubeek.dao.impl.ownframework.ClubTeamDaoImpl;
import com.clubeek.enums.UserRoleType;
import com.clubeek.model.ClubTeam;
import com.clubeek.service.SecurityService;
import com.clubeek.service.impl.SecurityServiceImpl;
import com.clubeek.ui.ModalDialog;
import com.clubeek.ui.ModalDialog.Mode;
import com.clubeek.ui.Tools;
import com.clubeek.ui.components.ActionTable;
import com.clubeek.ui.frames.FrameTeam;
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
public class ViewClubTeams extends VerticalLayout implements View, ActionTable.OnActionListener {

    /* PRIVATE */
    @Autowired
    private SecurityService securityService;

    @Autowired
    private ClubTeamDao clubTeamDao;

    /** Application navigation provider */
    private Navigation navigation;

    /** Table component */
    private final ActionTable table;

    /** List of teams loaded from tha database */
    private List<ClubTeam> teams = null;

    /* PUBLIC */
    public enum Columns {

        CAPTION, CATEGORY;
    }

    public ViewClubTeams() {

        this.setCaption(Messages.getString("teams")); //$NON-NLS-1$

        ActionTable.UserColumnInfo[] columns = {new ActionTable.UserColumnInfo(Columns.CAPTION, String.class, Messages.getString("caption")),
            new ActionTable.UserColumnInfo(Columns.CATEGORY, String.class, Messages.getString("category"))};

        table = new ActionTable(ActionTable.Action.getMaximalSet(false, true), columns, this);
        table.addToOwner(this);
    }

    // interface View
    @Override
    public void enter(ViewChangeEvent event) {
        this.navigation = (Navigation) getUI().getContent();

        securityService.authorize(UserRoleType.SPORT_MANAGER);

        teams = clubTeamDao.getAllClubTeams();

        table.removeAllRows();
        if (teams != null) {
            Container container = table.createDataContainer();
            for (int i = 0; i < teams.size(); ++i) {
                ClubTeam team = teams.get(i);
                table.addRow(container,
                        new Object[]{String.format("%s - %s", Tools.Strings.getCheckString(team.getActive()), team.getName()),
                            team.getCategory() != null ? team.getCategory().toString() : Messages.getString("categoryNotAssigned")}, i);
            }
            table.setDataContainer(container);
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
            case SINGLE_MOVE_UP:
                exchangeTeams((int) data, true);
                break;
            case SINGLE_MOVE_DOWN:
                exchangeTeams((int) data, false);
                break;
        }
        return true;
    }

    // Operations
    public void addTeam() {
        ModalDialog.show(this, Mode.ADD_ONCE, Messages.getString("newTeam"), new FrameTeam(), new ClubTeam(), clubTeamDao, navigation);
    }

    public void editTeam(int id) {
        if (id >= 0) {
            ModalDialog.show(this, Mode.EDIT, Messages.getString("teamProperties"), new FrameTeam(), teams.get(id), clubTeamDao, navigation);
        }
    }

    public void deleteTeam(int id) {
        table.deleteRow(teams.get(id).getId(), id, clubTeamDao, this, navigation, Columns.CAPTION, Columns.CATEGORY);
    }

    public void exchangeTeams(int id, boolean moveUp) {
        table.exchangeRows(teams, id, moveUp, clubTeamDao, this, navigation);
    }
}
