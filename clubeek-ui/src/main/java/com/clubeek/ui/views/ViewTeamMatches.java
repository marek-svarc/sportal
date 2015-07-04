package com.clubeek.ui.views;

import java.util.Date;
import java.util.List;

import com.clubeek.dao.TeamMatchDao;
import com.clubeek.dao.impl.ownframework.TeamMatchDaoImpl;
import com.clubeek.dao.impl.ownframework.rep.RepTeamMatch;
import com.clubeek.enums.UserRoleType;
import com.clubeek.model.TeamMatch;
import com.clubeek.service.SecurityService;
import com.clubeek.service.impl.SecurityServiceImpl;
import com.clubeek.ui.ModalDialog;
import com.clubeek.ui.ModalDialog.Mode;
import com.clubeek.ui.Tools;
import com.clubeek.ui.components.ActionTable;
import com.clubeek.ui.frames.FrameTeamMatch;
import com.clubeek.util.DateTime;
import com.clubeek.util.StyleToDateConverter;
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
public class ViewTeamMatches extends VerticalLayout implements View, ActionTable.OnActionListener {
	// TODO vitfo, created on 11. 6. 2015 
    @Autowired
	private SecurityService securityService;
	// TODO vitfo, created on 11. 6. 2015
    @Autowired
	private TeamMatchDao teamMatchDao;

    public enum Columns {

        MATCH, RESULT, DATE_TIME;
    }

    public ViewTeamMatches() {

        this.setCaption(Messages.getString("matches"));

        // columns definition
        ActionTable.UserColumnInfo[] columns = {
            new ActionTable.UserColumnInfo(Columns.MATCH, String.class, Messages.getString("match")),
            new ActionTable.UserColumnInfo(Columns.RESULT, String.class, Messages.getString("result")),
            new ActionTable.UserColumnInfo(Columns.DATE_TIME, Date.class, Messages.getString("dateTime"))
        };

        // vytvoreni tabulky a ovladacich tlacitek
        table = new ActionTable(ActionTable.Action.getStandardSet(true, true), columns, this);
        table.addToOwner(this);
        table.table.setConverter(Columns.DATE_TIME, new StyleToDateConverter(DateTime.DateStyle.DAY_AND_TIME));
    }

    @Override
    public void enter(ViewChangeEvent event) {

    	securityService.authorize(UserRoleType.SPORT_MANAGER);

        table.removeAllRows();

        if (event != null) {
            teamId = Tools.Strings.analyzeParameters(event);
        }

        if (teamId > 0) {
            // nacteni seznamu treninku z databaze
            games = null;
            games = RepTeamMatch.selectByTeamId(teamId, null);

            // inicializace tbulky
            if (games != null) {
                Container container = table.createDataContainer();
                for (int i = 0; i < games.size(); ++i) {
                    TeamMatch game = games.get(i);
                    table.addRow(container, new Object[]{Tools.Strings.getStrGameTeams(game),
                        game.getScoreAsStr(), game.getStart()}, i);
                }
                table.setDataContainer(container);
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
                Messages.getString("match"), new FrameTeamMatch(), data, teamMatchDao, null);
    }

    public void editMatch(int id) {
        if (id >= 0) {
            TeamMatch game = games.get(id);
            ModalDialog.show(this, Mode.EDIT, Messages.getString("match"), new FrameTeamMatch(),
                    game, teamMatchDao, null);
        }
    }

    public void deleteMatch(int id) {
        TeamMatch teamMatch = games.get(id);
        table.deleteRow(teamMatch.getId(), id, teamMatchDao, this, null, Columns.MATCH, Columns.DATE_TIME);
    }

    /* PRIVATE */
    /** Identifikator tymu pro ktery jsou zapasy zobrazeny */
    private int teamId = -1;

    /** Table component */
    private ActionTable table;

    /** Seznam vsech zapasu pro jeden tym */
    private List<TeamMatch> games = null;

}
