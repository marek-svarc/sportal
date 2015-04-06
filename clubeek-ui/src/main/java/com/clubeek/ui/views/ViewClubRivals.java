package com.clubeek.ui.views;

import java.util.List;

import com.clubeek.db.RepClubRival;
import com.clubeek.model.ClubRival;
import com.clubeek.model.User.Role;
import com.clubeek.ui.ModalDialog;
import com.clubeek.ui.Security;
import com.clubeek.ui.Tools;
import com.clubeek.ui.ModalDialog.Mode;
import com.clubeek.ui.components.ActionTable;
import com.clubeek.ui.frames.FrameClubRival;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ViewClubRivals extends VerticalLayout implements View, ActionTable.OnActionListener {

    public enum Columns {

        NAME, ADDRESS, WEB, GPS;
    }

    public ViewClubRivals() {

        this.setCaption(Messages.getString("ViewClubRivals.0")); //$NON-NLS-1$

        ActionTable.UserColumnInfo[] columns = {
            new ActionTable.UserColumnInfo(Columns.NAME, String.class, Messages.getString("name")),
            new ActionTable.UserColumnInfo(Columns.ADDRESS, String.class, Messages.getString("address")),
            new ActionTable.UserColumnInfo(Columns.WEB, Link.class, Messages.getString("web")),
            new ActionTable.UserColumnInfo(Columns.GPS, String.class, Messages.getString("gps"))
        };

        // vytvoreni tabulky a ovladacich tlacitek
        table = new ActionTable(ActionTable.Action.getStandardSet(false, true), columns, this);
        table.addToOwner(this);
    }

    // interface View
    @Override
    public void enter(ViewChangeEvent event) {

        Security.authorize(Role.SPORT_MANAGER);

        clubs = RepClubRival.selectAll(null);
        table.removeAllRows();
        for (int i = 0; i < clubs.size(); ++i) {
            ClubRival club = clubs.get(i);
            
            // sestaveni adresy klubu do jednoho retezce
            String address = Tools.Strings.concatenateText(
                    new String[]{club.getStreet(), club.getCity(), club.getCode()}, ", "); //$NON-NLS-1$
            
            // objekt odkazu na webove stranky klubu
            Link webLink = new Link(club.getWeb(), new ExternalResource(club.getWeb()));
            webLink.setTargetName("_blank"); //$NON-NLS-1$
            
            // pridani radky tabulky
            table.addRow(new Object[]{club.getName(), address, webLink, club.getGPS()}, i);
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
        ModalDialog.show(this, Mode.ADD_ONCE, Messages.getString("ViewClubRivals.2"),
                new FrameClubRival(), new ClubRival(), RepClubRival.getInstance(), null);
    }

    public void editClub(int id) {
        ClubRival club = clubs.get(id);
        ModalDialog.show(this, Mode.EDIT, Messages.getString("ViewClubRivals.3"),
                new FrameClubRival(), club, RepClubRival.getInstance(), null);
    }

    public void deleteClub(int id) {
        table.deleteRow(clubs.get(id).getId(), RepClubRival.getInstance(), this, null);
    }

    /* PRIVATE */
    /** Table component */
    private final ActionTable table;

    /** List of clubs loaded from the database */
    private List<ClubRival> clubs;
}
