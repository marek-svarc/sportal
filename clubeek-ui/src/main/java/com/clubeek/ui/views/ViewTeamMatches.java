package com.clubeek.ui.views;

import java.util.List;

import com.clubeek.db.RepTeamMatch;
import com.clubeek.model.TeamMatch;
import com.clubeek.model.User.Role;
import com.clubeek.ui.ModalDialog;
import com.clubeek.ui.Security;
import com.clubeek.ui.Tools;
import com.clubeek.ui.ModalDialog.Mode;
import com.clubeek.ui.Tools.DateTime.DateStyle;
import com.clubeek.ui.components.ActionTable;
import com.clubeek.ui.frames.FrameTeamMatch;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ViewTeamMatches extends VerticalLayout implements View, ActionTable.OnActionListener {

    public enum Columns {

        MATCH, DATE_TIME, RESULT;
    }

    public ViewTeamMatches() {

        this.setCaption(Messages.getString("matches")); //$NON-NLS-1$

        // columns definition
        ActionTable.UserColumnInfo[] columns = {
            new ActionTable.UserColumnInfo(Columns.MATCH, String.class, Messages.getString("match")),
            new ActionTable.UserColumnInfo(Columns.DATE_TIME, String.class, Messages.getString("dateTime")),
            new ActionTable.UserColumnInfo(Columns.RESULT, String.class, Messages.getString("result"))
        };

        // vytvoreni tabulky a ovladacich tlacitek
        table = new ActionTable(ActionTable.Action.getStandardSet(true, true), columns, this);
        table.addToOwner(this);
    }

    @Override
    public void enter(ViewChangeEvent event) {

        Security.authorize(Role.SPORT_MANAGER);

        table.removeAllRows();

        if (event != null) {
            teamId = Tools.Strings.analyzeParameters(event);
            if (teamId > 0) {
                // nacteni seznamu treninku z databaze
                games = null;
                games = RepTeamMatch.selectByTeamId(teamId, null);

                // inicializace tbulky
                if (games != null) {
                    TeamMatch game;
                    for (int i = 0; i < games.size(); ++i) {
                        game = games.get(i);
                        table.addRow(new Object[]{Tools.Strings.getStrGameTeams(game),
                            Tools.DateTime.dateToString(game.getStart(), DateStyle.DAY_AND_TIME), game.getScoreAsStr()}, i);
                    }
                }
            }
        }
    }

    // interface ActionTable.OnActionListener
    @Override
    public boolean doAction(ActionTable.Action action, Object data) {
        switch (action) {
            case SINGLE_ADD:
                addMatch();
                break;
            case SINGLE_EDIT:
                editMatch((int) data);
                break;
            case SINGLE_DELETE:
                deleteMatch((int) data);
                break;
        }
        return true;
    }

    // operations
    public void addMatch() {
        TeamMatch data = new TeamMatch();
        data.setClubTeamId(teamId);

        ModalDialog.show(this, Mode.ADD_MULTI,
                Messages.getString("match"), new FrameTeamMatch(), data, RepTeamMatch.getInstance(), null); //$NON-NLS-1$
    }

    public void editMatch(int id) {
        if (id >= 0) {
            TeamMatch game = games.get(id);
            ModalDialog.show(this, Mode.EDIT, Messages.getString("match"), new FrameTeamMatch(),
                    game, RepTeamMatch.getInstance(), null);
        }
    }

    public void deleteMatch(int id) {
        table.deleteRow(games.get(id).getId(), RepTeamMatch.getInstance(), this, null);
    }

    /* PRIVATE */
    /** Identifikator tymu pro ktery jsou zapasy zobrazeny */
    private int teamId = -1;

    /** Table component */
    private final ActionTable table;

    /** Seznam vsech zapasu pro jeden tym */
    private List<TeamMatch> games = null;

}
