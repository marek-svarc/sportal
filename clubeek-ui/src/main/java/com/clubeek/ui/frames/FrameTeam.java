package com.clubeek.ui.frames;

import java.util.List;

import com.clubeek.db.RepCategory;
import com.clubeek.model.Category;
import com.clubeek.model.ClubTeam;
import com.clubeek.ui.ModalInput;
import com.clubeek.ui.Tools;
import com.vaadin.annotations.Theme;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Trida komponenty pro zadavani vlastnosti tymu
 * 
 * @author Marek Svarc
 * 
 */
@SuppressWarnings("serial")
@Theme("baseTheme")
public class FrameTeam extends VerticalLayout implements ModalInput<ClubTeam> {

	public FrameTeam() {
		
		this.setWidth(260, Unit.PIXELS);
		
		// zadavani priznaku zobrazeni tymu v nabidce
		cbActive = Tools.Components.createCheckBox(Messages.getString("teamIsActive")); //$NON-NLS-1$
		this.addComponent(cbActive);

		// Layout pro zadavani hodnot s popisem
		FormLayout flInputs = new FormLayout();
		flInputs.addStyleName("topborder"); //$NON-NLS-1$
		this.addComponent(flInputs);

		// zadavani nazvu tymu
		tfName = Tools.Components.createTextField(Messages.getString("teamName"), true, null); //$NON-NLS-1$
		flInputs.addComponent(tfName);

		// nacteni vsech dostupnych kategorii
		List<Category> categoryList = null;
                categoryList = RepCategory.selectAll(null);

		// zadavani kategorie tymu
		if ((categoryList != null) && (categoryList.size() > 0)) {
			nsCategory = Tools.Components.createNativeSelect(Messages.getString("category"), null); //$NON-NLS-1$
			nsCategory.setSizeFull();
			nsCategory.addItem(0);
			nsCategory.setItemCaption(0, Messages.getString("notAssigned")); //$NON-NLS-1$
			for (Category item : categoryList) {
				nsCategory.addItem(item.getId());
				nsCategory.setItemCaption(item.getId(), item.getDescription());
			}
			flInputs.addComponent(nsCategory);
		}
	}

	@Override
	public void dataToInput(ClubTeam data) {
		cbActive.setValue(data.getActive());
		tfName.setValue(data.getName());
		if (nsCategory != null) {
                    if (data.getCategory() != null)
                        nsCategory.setValue(data.getCategory().getId());
                    else
                        nsCategory.setValue(0);
		}
	}

	@Override
	public void inputToData(ClubTeam data) {
		tfName.validate();
		
		data.setActive(cbActive.getValue());
		data.setName(tfName.getValue());
		if ((nsCategory == null) || (nsCategory.getValue() == null))
			data.setCategoryId(0);
		else
			data.setCategoryId((int) nsCategory.getValue());
	}

	/** Prepinac zda je tym aktivni */
	private CheckBox cbActive;

	/** Textove pole pro zadavani nazvu tymu */
	private TextField tfName;

	/** Seznam pro vyber kategorie */
	private NativeSelect nsCategory;
}
