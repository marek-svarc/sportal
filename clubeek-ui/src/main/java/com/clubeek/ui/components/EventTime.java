package com.clubeek.ui.components;

import java.util.ArrayList;
import java.util.List;

import com.clubeek.ui.Messages;
import com.clubeek.ui.Tools;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;

/** Komponenta pro zadani casu. Hodiny 0-24, minuty */
@SuppressWarnings("serial")
public class EventTime extends CustomComponent {

	public EventTime(String caption, int hourMin, int hourMax, int minuteStep, boolean required, String requiredError) {

		if (hourMin >= hourMax)
			throw new IllegalArgumentException(Messages.getString("checkHourse")); //$NON-NLS-1$

		if ((minuteStep <= 0) || (minuteStep > 15))
			throw new IndexOutOfBoundsException(Messages.getString("checkMinutes")); //$NON-NLS-1$

		this.setCaption(caption);

		HorizontalLayout laMain = new HorizontalLayout();

		// select pro vyber hodiny
		List<TimeInt> hours = new ArrayList<>();
		hourMin = Math.max(0, hourMin);
		hourMax = Math.min(24, hourMax);
		for (int i = hourMin; i <= hourMax; ++i)
			hours.add(new TimeInt(i));
		nsHour = Tools.Components.createNativeSelect(null, hours);
		nsHour.setWidth(50, Unit.PIXELS);
		nsHour.setStyleName("rowLowFirst"); //$NON-NLS-1$
		Tools.Validators.setRequired(nsHour, required, requiredError);
		laMain.addComponent(nsHour);

		// oddelovac
		laMain.addComponent(new Label(":")); //$NON-NLS-1$

		// select pro vyber minuty
		List<TimeInt> minutes = new ArrayList<>();
		for (int i = 0; i < 60; i += minuteStep)
			minutes.add(new TimeInt(i));
		nsMinute = Tools.Components.createNativeSelect(null, minutes);
		nsMinute.setWidth(50, Unit.PIXELS);
		nsMinute.setStyleName("rowLowLast"); //$NON-NLS-1$
		Tools.Validators.setRequired(nsMinute, required, requiredError);
		laMain.addComponent(nsMinute);

		setCompositionRoot(laMain);
	}

	public void validate() throws InvalidValueException {
		nsHour.validate();
		nsMinute.validate();
	}
	
	/** Nastavi hodinu */
	public void setHour(int value) {
		for (Object item : nsHour.getItemIds()) {
			if (value == ((TimeInt) item).getValue())
				nsHour.setValue(item);
		}
	}

	/** Vraci aktualne zadanou hodinu */
	public int getHour() {
		return ((TimeInt) nsHour.getValue()).getValue();
	}

	/** Nastavi minutu */
	public void setMinute(int value) {
		for (Object item : nsMinute.getItemIds()) {
			if (value == ((TimeInt) item).getValue())
				nsMinute.setValue(item);
		}
	}

	/** Vraci aktualne zadanou minutu */
	public int getMinute() {
		return ((TimeInt) nsMinute.getValue()).getValue();
	}

	/* PRIVATE */

	private class TimeInt {

		public TimeInt(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		@Override
		public String toString() {
			return String.format("%02d", value); //$NON-NLS-1$
		}

		private int value;
	}

	/** Select pro vyber hodiny */
	private NativeSelect nsHour;

	/** Select pro vyber minuty */
	private NativeSelect nsMinute;
}
