package com.clubeek.ui.views;

import java.sql.SQLException;
import java.util.List;

import com.clubeek.db.RepTeamMatch;
import com.clubeek.model.TeamMatch;
import com.clubeek.model.User.Role;
import com.clubeek.ui.ModalDialog;
import com.clubeek.ui.Security;
import com.clubeek.ui.Tools;
import com.clubeek.ui.ModalDialog.Mode;
import com.clubeek.ui.Tools.DateTime.DateStyle;
import com.clubeek.ui.components.TableWithButtons;
import com.clubeek.ui.frames.FrameTeamMatch;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ViewTeamMatches extends VerticalLayout implements View {

    public enum Columns {

        MATCH, DATE_TIME, RESULT;
    }

    public ViewTeamMatches() {

        this.setCaption(Messages.getString("matches")); //$NON-NLS-1$

        // columns definition
        TableWithButtons.UserColumnInfo[] columns = {
            new TableWithButtons.UserColumnInfo(Columns.MATCH, String.class, Messages.getString("match")),
            new TableWithButtons.UserColumnInfo(Columns.DATE_TIME, String.class, Messages.getString("dateTime")),
            new TableWithButtons.UserColumnInfo(Columns.RESULT, String.class, Messages.getString("result"))
        };

        // vytvoreni tabulky a ovladacich tlacitek
        table = new TableWithButtons(TableWithButtons.CtrlColumn.getStandardSet(), columns, null);
        table.addToOwner(this);
    }

    @Override
    public void enter(ViewChangeEvent event) {

        Security.authorize(Role.SPORT_MANAGER);

        if (event != null) {
            teamId = Tools.Strings.analyzeParameters(event);
        }

        table.removeAllRows();

        if (teamId > 0) {
            // nacteni seznamu treninku z databaze
            games = null;
            try {
                games = RepTeamMatch.selectByTeamId(teamId, null);
            } catch (SQLException e) {
                Tools.msgBoxSQLException(e);
            }

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

    public void insertMatch() {
        TeamMatch data = new TeamMatch();
        data.setClubTeamId(teamId);

        ModalDialog.show(this, Mode.ADD_MULTI,
                Messages.getString("match"), new FrameTeamMatch(), data, RepTeamMatch.getInstance(), null); //$NON-NLS-1$
    }

    public void editMatch() {
        if (table.getSelectedRow() >= 0) {
            TeamMatch game = games.get(table.getSelectedRow());
            ModalDialog.show(this, Mode.EDIT,
                    Messages.getString("match"), new FrameTeamMatch(), game, RepTeamMatch.getInstance(), null); //$NON-NLS-1$
        }
    }

    public void deleteMatch() {
        table.deleteSelectedRow(games, RepTeamMatch.getInstance(), this, null);
    }

    /* PRIVATE */
    /** Identifikator tymu pro ktery jsou zapasy zobrazeny */
    private int teamId = -1;

    /** Tabulka zobrazujici seznam zapasu */
    private TableWithButtons table;

    /** Seznam vsech zapasu pro jeden tym */
    private List<TeamMatch> games = null;

}
