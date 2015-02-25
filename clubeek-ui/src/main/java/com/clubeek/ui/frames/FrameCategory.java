package com.clubeek.ui.frames;

import com.clubeek.model.Category;
import com.clubeek.ui.ModalInput;
import com.clubeek.ui.Tools;
import com.vaadin.annotations.Theme;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Trida komponenty pro zadavani parametru kategorie
 * 
 * @author Marek Svarc
 * 
 */
@SuppressWarnings("serial")
@Theme("baseTheme")
public class FrameCategory extends VerticalLayout implements ModalInput<Category> {

	/* PUBLIC */

	public FrameCategory() {
		this.setWidth(260, Unit.PIXELS);
		
		// zadavani priznaku zda je kategorie aktivni
		cbActive = Tools.Components.createCheckBox(Messages.getString("showCategoryInMenu")); //$NON-NLS-1$
		this.addComponent(cbActive);

		// formularovy kontejner
		FormLayout flInputs = new FormLayout();
		flInputs.addStyleName("topborder"); //$NON-NLS-1$
		this.addComponent(flInputs);

		// zadavani nazvu kategorie
		tfDescription = Tools.Components.createTextField(Messages.getString("categoryName"), true, null); //$NON-NLS-1$
		flInputs.addComponent(tfDescription);
	}

	// interface ModalInput<Category>

	@Override
	public void dataToInput(Category data) {
		cbActive.setValue(data.getActive());
		tfDescription.setValue(data.getDescription());
	}

	@Override
	public void inputToData(Category data) {
		tfDescription.validate();

		data.setActive(cbActive.getValue());
		data.setDescription(tfDescription.getValue());
	}

	/* PRIVATE */

	/** Prepinac zda je kategorie aktivni */
	private CheckBox cbActive;

	/** Textove pole pro zadavani popisu kategorie */
	private TextField tfDescription;
}
