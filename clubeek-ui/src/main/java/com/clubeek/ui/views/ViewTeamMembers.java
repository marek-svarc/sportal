package com.clubeek.ui.views;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.clubeek.db.RepClubTeam;
import com.clubeek.db.RepTeamMember;
import com.clubeek.model.ClubMember;
import com.clubeek.model.ClubTeam;
import com.clubeek.model.ModelTools;
import com.clubeek.model.TeamMember;
import com.clubeek.ui.ModalDialog;
import com.clubeek.ui.Tools;
import com.clubeek.ui.ModalDialog.Mode;
import com.clubeek.ui.components.ActionTable;
import com.clubeek.ui.frames.FrameSelectMembers;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ViewTeamMembers extends VerticalLayout implements View, ActionTable.OnActionListener {

    public enum Columns {

        NAME, SURNAME, DATE_OF_BIRTH, POSITION_IN_TEAM;
    }

    public ViewTeamMembers() {

        this.setCaption(Messages.getString("assignmentToTeams"));

        // columns definition
        ActionTable.UserColumnInfo[] columns = {
            new ActionTable.UserColumnInfo(Columns.NAME, String.class, Messages.getString("name")),
            new ActionTable.UserColumnInfo(Columns.SURNAME, String.class, Messages.getString("caption")),
            new ActionTable.UserColumnInfo(Columns.DATE_OF_BIRTH, String.class, Messages.getString("dateOfBirth")),
            new ActionTable.UserColumnInfo(Columns.POSITION_IN_TEAM, String.class, Messages.getString("positionInTeam")),};

        // tabulka clenu tymu
        table = new ActionTable(ActionTable.Action.getStandardSet(), columns, this);
        table.addToOwner(this);

        // Seznam pro vyber editovaneho tymu
        nsTeams = new NativeSelect(null);
        nsTeams.setImmediate(true);
        nsTeams.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                updateTeamMembersTable();
            }
        });
        table.addTableControl(nsTeams, 0);

        Label laTeams = new Label(" - ");
        table.addTableControl(laTeams, 1);

    }

    // interface View
    @Override
    public void enter(ViewChangeEvent event) {
        List<ClubTeam> teams = null;
        ClubTeam selectedTeam = getSelectedTeam();

        // nplneni seznamu aktivnich tymu
        try {
            teams = RepClubTeam.select(true, null);
        } catch (SQLException e) {
            Tools.msgBoxSQLException(e);
        }
        Tools.Components.initNativeSelect(nsTeams, teams);

        // vyber defaultniho aktivniho tymu
        if (selectedTeam != null) {
            selectedTeam = ModelTools.listFindById(teams, selectedTeam.getId());
        }
        if ((selectedTeam == null) && (teams != null) && (teams.size() > 0)) {
            selectedTeam = teams.get(0);
        }

        // prirazeni defaultniho tymu
        nsTeams.setValue(selectedTeam);
    }

    // interface ActionTable.OnActionListener
    @Override
    public boolean doAction(ActionTable.Action action, Object data) {
        switch (action) {
            case SINGLE_ADD:
                selectTeamMembers();
                break;
            case SINGLE_DELETE:
                deleteTeamMember((int) data);
                break;
        }
        return true;
    }

    // properties
    public ClubTeam getSelectedTeam() {
        return (ClubTeam) nsTeams.getValue();
    }

    /* PRIVATE */
    private void updateTeamMembersTable() {
        table.removeAllRows();

        try {
            ClubTeam selectedTeam = getSelectedTeam();
            if (selectedTeam != null) {
                // read team members from database
                teamMembers = RepTeamMember.selectByTeamId(selectedTeam.getId(), null);

                // fill table
                for (int i = 0; i < teamMembers.size(); ++i) {
                    TeamMember teamMember = teamMembers.get(i);
                    ClubMember clubMember = teamMember.getClubMember();
                    table.addRow(new Object[]{clubMember.getName(), clubMember.getSurname(),
                        clubMember.getBirthdateAsString(), teamMember.getFunctionsAsList().toString()}, new Integer(i));
                }
            }
        } catch (SQLException e) {
            Tools.msgBoxSQLException(e);
        }
    }

    /** Spusti modalni okno pro vyber clena tymu */
    private void selectTeamMembers() {

        final ClubTeam selectedTeam = getSelectedTeam();

        if (selectedTeam != null) {

            try {
                // seznam clenu klubu asociovanych se zobrazenymi cleny tymu
                final List<ClubMember> clubMembers = new ArrayList<>(teamMembers.size());
                for (TeamMember teamMember : teamMembers) {
                    clubMembers.add(teamMember.getClubMember());
                }

                // vytvoreni dialogu
                ModalDialog<List<ClubMember>> dlg = new ModalDialog<>(Mode.EDIT, Messages.getString("selectTeamMembers"), //$NON-NLS-1$
                        new FrameSelectMembers(), clubMembers, new ClickListener() {

                            @Override
                            public void buttonClick(ClickEvent event) {
                                try {
                                    RepTeamMember.update(teamMembers,
                                            RepTeamMember.selectOrCreateByClubMembers(selectedTeam.getId(), clubMembers, null));
                                } catch (SQLException e) {
                                    Tools.msgBoxSQLException(e);
                                }
                                updateTeamMembersTable();
                            }
                        });

                getUI().addWindow(dlg);

            } catch (SQLException e) {
                Tools.msgBoxSQLException(e);
            }
        }
    }

    private void deleteTeamMember(int id) {
        table.deleteRow(teamMembers.get(id).getId(), RepTeamMember.getInstance(), this, null);
    }

//    private void selectTeamFunctions() {
//        final TeamMember selectedTeamMember = getSelectedTeamMember();
//
//        if (selectedTeamMember != null) {
//            ModalDialog.show(this, Mode.EDIT, Messages.getString("editTeamFunction"), new FrameTeamFunctions(), selectedTeamMember, //$NON-NLS-1$
//                    RepTeamMember.getInstance(), null);
//        }
//    }
    /** Teams selection component */
    private final NativeSelect nsTeams;

    /** Table component */
    private final ActionTable table;

    /** List of team members loaded from the database */
    private List<TeamMember> teamMembers;
}
