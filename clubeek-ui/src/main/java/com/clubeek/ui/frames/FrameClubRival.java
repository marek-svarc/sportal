package com.clubeek.ui.frames;

import com.clubeek.model.ClubRival;
import com.clubeek.ui.ModalInput;
import com.clubeek.ui.Tools;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class FrameClubRival extends CustomComponent implements ModalInput<ClubRival> {

	public FrameClubRival() {
		VerticalLayout frameLayout = new VerticalLayout();
		frameLayout.setWidth(500, Unit.PIXELS);

		FormLayout inputLayoutA = new FormLayout();
		frameLayout.addComponent(inputLayoutA);

		tfName = Tools.Components.createTextField(Messages.getString("name"), true, null); //$NON-NLS-1$
		inputLayoutA.addComponent(tfName);

		tfWeb = Tools.Components.createTextField(Messages.getString("web")); //$NON-NLS-1$
		tfGPS = Tools.Components.createTextField(Messages.getString("gps")); //$NON-NLS-1$
		tfCity = Tools.Components.createTextField(Messages.getString("city")); //$NON-NLS-1$
		tfStreet = Tools.Components.createTextField(Messages.getString("street")); //$NON-NLS-1$
		tfZipCode = Tools.Components.createTextField(Messages.getString("zipCode")); //$NON-NLS-1$
		HorizontalLayout columnsLayout = Tools.Components.createMultipleColumnsForm(2, new Component[] { tfCity, tfStreet,
				tfZipCode, tfWeb, tfGPS });
		columnsLayout.setStyleName("topborder"); //$NON-NLS-1$

		frameLayout.addComponent(columnsLayout);

		setCompositionRoot(frameLayout);
	}

	@Override
	public void dataToInput(ClubRival data) {
		tfName.setValue(data.getName());
		tfWeb.setValue(data.getWeb());
		tfGPS.setValue(data.getGPS());
		tfCity.setValue(data.getCity());
		tfStreet.setValue(data.getStreet());
		tfZipCode.setValue(data.getCode());
	}

	@Override
	public void inputToData(ClubRival data) {
		tfName.validate();
		
		data.setName(tfName.getValue());
		data.setWeb(tfWeb.getValue());
		data.setGPS(tfGPS.getValue());
		data.setCity(tfCity.getValue());
		data.setStreet(tfStreet.getValue());
		data.setCode(tfZipCode.getValue());
	}

	/** Textove pole pro zadani nazvu klubu */
	private final TextField tfName;

	/** Textove pole pro zadani internetove stranky klubu */
	private final TextField tfWeb;

	/** Textove pole pro zadani GPS souradnice klubu */
	private final TextField tfGPS;

	/** Textove pole pro zadani mista kde se klub nachazi */
	private final TextField tfCity;

	/** Textove pole pro zadani ulice kde se klub nachazi */
	private final TextField tfStreet;

	/** Textove pole pro zadani postovniho smerovaciho cisla klub */
	private final TextField tfZipCode;

}
