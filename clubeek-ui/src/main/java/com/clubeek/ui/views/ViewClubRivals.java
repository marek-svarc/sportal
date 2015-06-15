package com.clubeek.ui.views;

import java.util.List;

import com.clubeek.dao.ClubRivalDao;
import com.clubeek.dao.impl.ownframework.ClubRivalDaoImpl;
import com.clubeek.dao.impl.ownframework.rep.RepClubRival;
import com.clubeek.model.ClubRival;
import com.clubeek.model.User.Role;
import com.clubeek.service.SecurityService;
import com.clubeek.service.impl.Security;
import com.clubeek.service.impl.SecurityServiceImpl;
import com.clubeek.ui.ModalDialog;
import com.clubeek.ui.Tools;
import com.clubeek.ui.ModalDialog.Mode;
import com.clubeek.ui.components.ActionTable;
import com.clubeek.ui.frames.FrameClubRival;
import com.vaadin.data.Container;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ViewClubRivals extends VerticalLayout implements View, ActionTable.OnActionListener {
    // TODO vitfo, created on 11. 6. 2015
    private SecurityService securityService = new SecurityServiceImpl();
    // TODO vitfo, created on 11. 6. 2015 
    private ClubRivalDao clubRivalDao = new ClubRivalDaoImpl();

    public enum Columns {

        NAME, WEB;
    }

    public ViewClubRivals() {

        this.setCaption(Messages.getString("ViewClubRivals.0")); //$NON-NLS-1$

        ActionTable.UserColumnInfo[] columns = {
                new ActionTable.UserColumnInfo(Columns.NAME, String.class, Messages.getString("name")),
                new ActionTable.UserColumnInfo(Columns.WEB, Link.class, Messages.getString("web")) };

        // vytvoreni tabulky a ovladacich tlacitek
        table = new ActionTable(ActionTable.Action.getStandardSet(false, true), columns, this);
        table.addToOwner(this);
    }

    // interface View
    @Override
    public void enter(ViewChangeEvent event) {

        securityService.authorize(Role.SPORT_MANAGER);

//        clubs = RepClubRival.selectAll(null);
        clubs = clubRivalDao.getAllClubRivals();

        table.removeAllRows();
        if (clubs != null) {
            Container container = table.createDataContainer();
            for (int i = 0; i < clubs.size(); ++i) {
                ClubRival club = clubs.get(i);

                // link to the club web pages
                Link webLink = null;
                if (club.getWeb() != null) {
                    webLink = new Link(club.getWeb(), new ExternalResource(club.getWeb()));
                    webLink.setTargetName("_blank"); //$NON-NLS-1$
                }

                table.addRow(container, new Object[] { club.getName(), webLink }, i);
            }
            table.setDataContainer(container);
        }
    }

    // interface ActionTable.OnActionListener
    @Override
    public boolean doAction(ActionTable.Action action, Object data) {
        switch (action) {
            case SINGLE_ADD:
                addClub();
                break;
            case SINGLE_EDIT:
                editClub((int) data);
                break;
            case SINGLE_DELETE:
                deleteClub((int) data);
                break;
        }
        return true;
    }

    public void addClub() {
        ModalDialog.show(this, Mode.ADD_ONCE, Messages.getString("ViewClubRivals.2"), new FrameClubRival(),
                new ClubRival(), clubRivalDao, null);
    }

    public void editClub(int id) {
        ClubRival club = clubs.get(id);
        ModalDialog.show(this, Mode.EDIT, Messages.getString("ViewClubRivals.3"), new FrameClubRival(), club,
                clubRivalDao, null);
    }

    public void deleteClub(int id) {
        table.deleteRow(clubs.get(id).getId(), id, clubRivalDao, this, null, Columns.NAME);
    }

    /* PRIVATE */
    /** Table component */
    private final ActionTable table;

    /** List of clubs loaded from the database */
    private List<ClubRival> clubs;
}
