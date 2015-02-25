package com.clubeek.ui.frames;

import java.util.Calendar;

import com.clubeek.model.TeamTraining;
import com.clubeek.ui.ModalInput;
import com.clubeek.ui.Tools;
import com.clubeek.ui.components.EventTime;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class FrameTeamTraining extends VerticalLayout implements ModalInput<TeamTraining> {

	public FrameTeamTraining() {
		FormLayout laMain = new FormLayout();

		// zadavani datumu
		dfDatum = Tools.Components.createDateField(Messages.getString("date"), true, null); //$NON-NLS-1$
		laMain.addComponent(dfDatum);

		// zadavani casu zacatku
		String errTimeStart = Messages.getString("start"); //$NON-NLS-1$
		etTimeStart = new EventTime(errTimeStart, 0, 23, 5, true, Tools.Strings.getEmptyValueErrorString(errTimeStart));
		laMain.addComponent(etTimeStart);

		// zadavani casu konce
		String errTimeEnd = Messages.getString("end"); //$NON-NLS-1$
		etTimeEnd = new EventTime(errTimeEnd, 0, 23, 5, true, Tools.Strings.getEmptyValueErrorString(errTimeEnd));
		laMain.addComponent(etTimeEnd);

		// zadavani mista
		tfPlace = Tools.Components.createTextField(Messages.getString("place")); //$NON-NLS-1$
		tfPlace.setWidth(200, Unit.PIXELS);
		laMain.addComponent(tfPlace);

		this.addComponent(laMain);
	}

	// imlementace ModalInput<Training>

	@Override
	public void dataToInput(TeamTraining data) {
		Calendar cal = Calendar.getInstance(Tools.getLocale());

		dfDatum.setValue(data.getStart());

		// pocatek treninku
		if (data.getStart() != null) {
			cal.setTime(data.getStart());
			etTimeStart.setHour(cal.get(Calendar.HOUR_OF_DAY));
			etTimeStart.setMinute(cal.get(Calendar.MINUTE));
		}

		// konec treninku
		if (data.getEnd() != null) {
			cal.setTime(data.getEnd());
			etTimeEnd.setHour(cal.get(Calendar.HOUR_OF_DAY));
			etTimeEnd.setMinute(cal.get(Calendar.MINUTE));
		}

		// misto treninku
		tfPlace.setValue(data.getPlace());
	}

	@Override
	public void inputToData(TeamTraining data) {
		dfDatum.validate();
		etTimeStart.validate();
		etTimeEnd.validate();

		Calendar cal = Calendar.getInstance(Tools.getLocale());
		cal.setTime(dfDatum.getValue());

		// pocatek treninku
		cal.set(Calendar.HOUR_OF_DAY, etTimeStart.getHour());
		cal.set(Calendar.MINUTE, etTimeStart.getMinute());
		data.setStart(cal.getTime());

		// konec treninku
		cal.set(Calendar.HOUR_OF_DAY, etTimeEnd.getHour());
		cal.set(Calendar.MINUTE, etTimeEnd.getMinute());
		data.setEnd(cal.getTime());

		// misto konani treninku
		data.setPlace(tfPlace.getValue());
	}

	/** Zadavani datumu treninku */
	private DateField dfDatum;

	/** Zadavani casu zacatku treninku */
	private EventTime etTimeStart;

	/** Zadavani casu konce treninku */
	private EventTime etTimeEnd;

	/** Zadavani mista konani */
	private TextField tfPlace;
}
