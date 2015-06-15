package com.clubeek.ui.frames;

import java.util.Calendar;
import java.util.List;

import com.clubeek.dao.ClubRivalDao;
import com.clubeek.dao.impl.ownframework.ClubRivalDaoImpl;
import com.clubeek.dao.impl.ownframework.rep.RepClubRival;
import com.clubeek.model.ClubRival;
import com.clubeek.model.TeamMatch;
import com.clubeek.ui.ModalInput;
import com.clubeek.ui.Tools;
import com.clubeek.ui.components.EventTime;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class FrameTeamMatch extends VerticalLayout implements ModalInput<TeamMatch> {
    // TODO vitfo, created on 11. 6. 2015 
    private ClubRivalDao clubRivalDao = new ClubRivalDaoImpl();

	public FrameTeamMatch() {

		FormLayout laResults = new FormLayout();

		String requiredErrorTerm = Tools.Strings.getEmptyValueErrorString(Messages.getString("term")); //$NON-NLS-1$

		// 1. radka - souper
		nsRival = Tools.Components.createNativeSelect(null, null); //$NON-NLS-1$
		tfRivalComment = Tools.Components.createTextField(null); //$NON-NLS-1$
		laResults.addComponent(Tools.Components.createHorizontalLayout(Messages.getString("rival"), nsRival, tfRivalComment)); //$NON-NLS-1$

		// 2.radka - datum a cas zapasu
		dfDatum = Tools.Components.createDateField(null, true, requiredErrorTerm);
		etTimeStart = new EventTime(null, 0, 23, 5, true, requiredErrorTerm);
		laResults.addComponent(Tools.Components.createHorizontalLayout(Messages.getString("term"), dfDatum, etTimeStart)); //$NON-NLS-1$

		// 3.radka - misto zapasu
		ogHomeGame = Tools.Components.createOptionGroup("", //$NON-NLS-1$
				new String[] { Messages.getString("home"), Messages.getString("foreign") }); //$NON-NLS-1$ //$NON-NLS-2$
		ogHomeGame.setStyleName("horizontal"); //$NON-NLS-1$
		laResults.addComponent(ogHomeGame);

		// 4.radek (skore, priznak zverejneni)
		tfScoreHomeTeam = Tools.Components.createTextField(null);
		tfScoreHomeTeam.setWidth(50, Unit.PIXELS);
		Label laScoreDivider = new Label(":"); //$NON-NLS-1$
		tfScoreVisitingTeam = Tools.Components.createTextField(null);
		tfScoreVisitingTeam.setWidth(50, Unit.PIXELS);
		cbPublish = Tools.Components.createCheckBox(Messages.getString("FrameTeamMatch.3")); //$NON-NLS-1$
		laResults.addComponent(Tools.Components.createHorizontalLayout(Messages.getString("matchResult"), tfScoreHomeTeam, //$NON-NLS-1$
				laScoreDivider, tfScoreVisitingTeam, cbPublish));

		// 5. radek (komentar)
		taComment = new TextArea();
		taComment.setSizeFull();
		taComment.setHeight(5, Unit.EM);
		laResults.addComponent(taComment);

		this.addComponent(laResults);
	}

	@Override
	public void dataToInput(TeamMatch data) {

		Calendar cal = Calendar.getInstance(Tools.getLocale());

		// souper

		clubs = null;
//                clubs = RepClubRival.selectAll(null);
		clubs = clubRivalDao.getAllClubRivals();

		nsRival.addItem(-1);
		nsRival.setItemCaption(-1, Messages.getString("notAssigned")); //$NON-NLS-1$
		nsRival.setValue(-1);
		if (clubs != null) {
			for (int i = 0; i < clubs.size(); ++i) {
				nsRival.addItem(i);
				nsRival.setItemCaption(i, clubs.get(i).getName());
				if (data.getClubRivalId() == clubs.get(i).getId())
					nsRival.setValue(i);
			}
		}

		tfRivalComment.setValue(data.getClubRivalComment());

		// termin

		if (data.getStart() != null) {
			dfDatum.setValue(data.getStart());
			cal.setTime(data.getStart());
			etTimeStart.setHour(cal.get(Calendar.HOUR_OF_DAY));
			etTimeStart.setMinute(cal.get(Calendar.MINUTE));
		}

		// misto zapasu

		ogHomeGame.setValue(data.getHomeCourt() ? 0 : 1);

		// vysledek

		Tools.Components.textFieldSetInt(tfScoreHomeTeam, data.getScoreHomeTeam());
		Tools.Components.textFieldSetInt(tfScoreVisitingTeam, data.getScoreVisitingTeam());
		cbPublish.setValue(data.getPublish());
		taComment.setValue(data.getComment());
	}

	@Override
	public void inputToData(TeamMatch data) {
		dfDatum.validate();
		etTimeStart.validate();

		// souper
		data.setClubRival(null);
		int clubId = (int) nsRival.getValue();
		if (clubId >= 0)
			data.setClubRivalId(clubs.get(clubId).getId());
		data.setClubRivalComment(tfRivalComment.getValue());

		// termin

		Calendar cal = Calendar.getInstance(Tools.getLocale());
		cal.setTime(dfDatum.getValue());
		cal.set(Calendar.HOUR_OF_DAY, etTimeStart.getHour());
		cal.set(Calendar.MINUTE, etTimeStart.getMinute());
		data.setStart(cal.getTime());

		// misto zapasu

		data.setHomeCourt(((int) ogHomeGame.getValue()) == 0);

		// vysledek

		data.setScoreHomeTeam(Tools.Components.textFieldGetInt(tfScoreHomeTeam));
		data.setScoreVisitingTeam(Tools.Components.textFieldGetInt(tfScoreVisitingTeam));
		data.setPublish(cbPublish.getValue());
		data.setComment(taComment.getValue());
	}

	/** Seznam vsech souperu */
	private List<ClubRival> clubs;

	/** Zadavani datumu zapasu */
	private DateField dfDatum;

	/** Zadavani casu zacatku zapasu */
	private EventTime etTimeStart;

	/** Seznam pro vyber soupere */
	private NativeSelect nsRival;

	/** Uzivatelsky popis soupere */
	private TextField tfRivalComment;

	/** Vyber mista zapasu */
	private OptionGroup ogHomeGame;

	/** Pocet vstrelenych branek domaciho muzstva */
	private TextField tfScoreHomeTeam;

	/** Pocet vstrelenych branek hostujicicho muzstva */
	private TextField tfScoreVisitingTeam;

	/** Priznak zda se ma vysledek zapasu publikovat */
	private CheckBox cbPublish;

	/** Komentar k zapasu */
	private TextArea taComment;
}
