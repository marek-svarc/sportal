package com.clubeek.ui.views;

import java.util.Date;
import java.util.List;

import com.clubeek.dao.TeamTrainingDao;
import com.clubeek.dao.impl.ownframework.TeamTrainingDaoImpl;
import com.clubeek.dao.impl.ownframework.rep.RepTeamTraining;
import com.clubeek.enums.UserRoleType;
import com.clubeek.model.TeamTraining;
import com.clubeek.service.SecurityService;
import com.clubeek.service.impl.SecurityServiceImpl;
import com.clubeek.ui.ModalDialog;
import com.clubeek.ui.ModalDialog.Mode;
import com.clubeek.ui.Tools;
import com.clubeek.ui.components.ActionTable;
import com.clubeek.ui.frames.FrameTeamTraining;
import com.clubeek.util.DateTime;
import com.clubeek.util.StyleToDateConverter;
import com.vaadin.data.Container;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public final class ViewTeamTrainings extends VerticalLayout implements View, ActionTable.OnActionListener {
	// TODO vitfo, created on 11. 6. 2015 
	private SecurityService securityService = new SecurityServiceImpl();
	// TODO vitfo, created on 11. 6. 2015 
	private TeamTrainingDao teamTrainingDao = new TeamTrainingDaoImpl();

    public enum Columns {

        DATE_TIME, PLACE
    }

    public ViewTeamTrainings() {

        this.setCaption(Messages.getString("trainings"));

        ActionTable.UserColumnInfo[] columns = {
            new ActionTable.UserColumnInfo(Columns.PLACE, String.class, Messages.getString("place")),
            new ActionTable.UserColumnInfo(Columns.DATE_TIME, Date.class, Messages.getString("dateTime"))
        };

        // create table and buttons
        table = new ActionTable(ActionTable.Action.getStandardSet(true, true), columns, this);
        table.addToOwner(this);
        table.table.setConverter(Columns.DATE_TIME, new StyleToDateConverter(DateTime.DateStyle.DAY_AND_TIME));

        // custom style
        table.setCellStyleGenerator(new CustomTable.CellStyleGenerator() {

            @Override
            public String getStyle(CustomTable source, Object itemId, Object propertyId) {
                TeamTraining trainig = trainings.get((int) itemId);
                if (trainig.isGone()) {
                    return "disabled";
                } else {
                    return null;
                }
            }
        });

    }

    // interface View
    @Override
    public void enter(ViewChangeEvent event) {

    	securityService.authorize(UserRoleType.SPORT_MANAGER);

        table.removeAllRows();

        if (event != null) {
            teamId = Tools.Strings.analyzeParameters(event);
        }

        if (teamId > 0) {
            trainings = RepTeamTraining.selectByClubTeamId(teamId, null);
            if (trainings != null) {
                Container container = table.createDataContainer();
                for (int i = 0; i < trainings.size(); ++i) {
                    TeamTraining training = trainings.get(i);
                    table.addRow(container, new Object[]{training.getPlace(), training.getStart()}, i);
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
                addTraining();
                break;
            case SINGLE_EDIT:
                editTraining((int) data);
                break;
            case SINGLE_DELETE:
                deleteTraining((int) data);
                break;
        }
        return true;
    }

    public void addTraining() {
        TeamTraining data = new TeamTraining();
        data.setClubTeamId(teamId);

        ModalDialog.show(this, Mode.ADD_MULTI, Messages.getString("training"), new FrameTeamTraining(),
                data, teamTrainingDao, null);
    }

    public void editTraining(int id) {
        if (id >= 0) {
            TeamTraining training = trainings.get(id);
            if (training.isGone()) {
                Notification.show(Messages.getString("canNotModifyTreining"), Type.HUMANIZED_MESSAGE);
            } else {
                ModalDialog.show(this, Mode.EDIT, Messages.getString("training"), new FrameTeamTraining(),
                        training, teamTrainingDao, null);
            }
        }
    }

    public void deleteTraining(int id) {
        TeamTraining training = trainings.get(id);
        table.deleteRow(training.getId(), id, teamTrainingDao, this, null, Columns.DATE_TIME, Columns.PLACE);
    }

    /* PRIVATE */
    /** Unique id of the team for which are trainings displayed */
    private int teamId = -1;

    /** Table component */
    private final ActionTable table;

    /** List of trainings loaded from the database */
    private List<TeamTraining> trainings;

}
