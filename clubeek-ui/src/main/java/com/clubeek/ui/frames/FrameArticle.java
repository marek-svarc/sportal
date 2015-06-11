package com.clubeek.ui.frames;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.clubeek.dao.CategoryDao;
import com.clubeek.dao.impl.ownframework.CategoryDaoImpl;
import com.clubeek.dao.impl.ownframework.rep.RepCategory;
import com.clubeek.db.RepClubTeam;
import com.clubeek.model.Article;
import com.clubeek.model.Category;
import com.clubeek.model.ClubTeam;
import com.clubeek.model.Article.Location;
import com.clubeek.model.Article.Owner;
import com.clubeek.ui.ModalInput;
import com.clubeek.ui.Tools;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("baseTheme")
public class FrameArticle extends VerticalLayout implements ModalInput<Article> {
    // TODO vitfo, created on 11. 6. 2015 
    private CategoryDao categoryDao = new CategoryDaoImpl();

	/* PRIVATE */

	/** Textove pole pro zadavani nadpisu clanku */
	private TextField tfCaption;

	/** Textove pole pro zadavani zkraceneho popisu clanku */
	private TextArea taSummary;

	/** Textove pole (HTML) pro zadavani obsahu clanku */
	private RichTextArea taContent;

	/** Seznam pro výběr typu umisteni clanku na jedne strance */
	private NativeSelect nsLocation;

	/** Seznam pro výběr typu umisteni clanku na webu */
	private NativeSelect nsOwner;

	/** Seznam pro výběr kategorie u ktere se clanek zobrazi */
	private NativeSelect nsCategories;

	/** Seznam pro výběr tymu u ktereho se clanek zobrazi */
	private NativeSelect nsTeams;

	/** Zaskrtavaci pole pro zadavani priority */
	private CheckBox cbPriority;

	/** Pole pro zadavani datumu ukonceni platnosti clanku */
	private DateField dfExpiration;

	private void updateControls() {
		Article.Owner owner = (Article.Owner) nsOwner.getValue();
		nsCategories.setVisible(owner == Owner.CATEGORY);
		nsTeams.setVisible(owner == Owner.TEAM);
	}

	/** Vytvori a inicializuje layout pro vodorovne rozmisteni komponent */
	private static HorizontalLayout createHorizontalLayout(String caption, Component... children) {
		HorizontalLayout layout = new HorizontalLayout(children);
		layout.setCaption(caption);
		layout.setSpacing(false);
		layout.setMargin(false);
		return layout;
	}

	/* PUBLIC */

	public FrameArticle() {

		// nacteni dostupnych kategorii a tymu z databaze

		List<Category> categories = null;
		List<ClubTeam> teams = null;
//                categories = RepCategory.selectAll(null);
		        categories = categoryDao.getAllCategories();
                teams = RepClubTeam.select(false, null);

		// sestaveni seznamu moznych vlastniku clanku

		List<Article.Owner> owners = new ArrayList<>();
		owners.add(Article.Owner.CLUB_ALL);
		owners.add(Article.Owner.CLUB);
		if ((categories != null) && (categories.size() > 0))
			owners.add(Article.Owner.CATEGORY);
		if ((teams != null) && (teams.size() > 0))
			owners.add(Article.Owner.TEAM);

		// vytvoreni komponent

		tfCaption = Tools.Components.createTextField(Messages.getString("title"), true, null); //$NON-NLS-1$
		taSummary = Tools.Components.createTextArea(Messages.getString("summary")); //$NON-NLS-1$
		taSummary.setHeight(5, Unit.EM);
		taContent = new RichTextArea(Messages.getString("articleContent")); //$NON-NLS-1$
		taContent.setNullSettingAllowed(true);
		taContent.setWidth(700, Unit.PIXELS);
		nsLocation = Tools.Components.createNativeSelect(null, java.util.Arrays.asList(Article.Location.values()));
		nsOwner = Tools.Components.createNativeSelect(null, owners);
		nsOwner.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateControls();
			}
		});
		nsOwner.setNullSelectionAllowed(false);
		nsCategories = Tools.Components.createNativeSelect(null, categories, true);
		nsCategories.setNullSelectionAllowed(false);
		nsTeams = Tools.Components.createNativeSelect(null, teams, true);
		cbPriority = Tools.Components.createCheckBox(Messages.getString("articleWillBeMarkedAsSignificant")); //$NON-NLS-1$
		dfExpiration = Tools.Components.createDateField(null);

		// obsah clanku

		this.addComponent(tfCaption);
		this.addComponent(taSummary);
		this.addComponent(taContent);

		// umisteni clanku

		nsLocation.addStyleName("rowLowFirst"); //$NON-NLS-1$
		nsOwner.addStyleName("rowLow"); //$NON-NLS-1$
		nsCategories.addStyleName("rowLowLast"); //$NON-NLS-1$
		nsTeams.addStyleName("rowLowLast"); //$NON-NLS-1$

		FormLayout flLocation = new FormLayout(createHorizontalLayout(Messages.getString("placement"), nsLocation, nsOwner, nsCategories, nsTeams)); //$NON-NLS-1$
		this.addComponent(flLocation);

		// sprava clanku

		dfExpiration.addStyleName("rowLowFirst"); //$NON-NLS-1$

		FormLayout flSettings = new FormLayout(createHorizontalLayout(Messages.getString("validTo"), dfExpiration, cbPriority)); //$NON-NLS-1$
		flSettings.addStyleName("topborder"); //$NON-NLS-1$
		this.addComponent(flSettings);
	}

	// interface ModalInput<Article>

	@Override
	public void dataToInput(Article data) {
		tfCaption.setValue(data.getCaption());
		taSummary.setValue(data.getSummary());
		taContent.setValue(data.getContent());
		nsLocation.setValue(data.getLocation());
		nsOwner.setValue(data.getOwner());
		Tools.Components.initSelection(nsCategories, data.getCategoryId());
		Tools.Components.initSelection(nsTeams, data.getClubTeamId());
		cbPriority.setValue(data.getPriority());
		dfExpiration.setValue(data.getExpirationDate());
	}
	
	@Override
	public void inputToData(Article data) {
		tfCaption.validate();
		
		data.setCaption(tfCaption.getValue());
		data.setSummary(taSummary.getValue());
		if ((taContent.getValue() != null) && (!"<p><br></p>".equals(taContent.getValue()))) //$NON-NLS-1$
			data.setContent(taContent.getValue());
		else
			data.setContent(null);
		data.setLocation((Location) nsLocation.getValue());
		data.setOwner((Article.Owner) nsOwner.getValue());
		switch (data.getOwner()) {
		case CLUB:
		case CLUB_ALL:
			data.setCategoryId(0);
			data.setClubTeamId(0);
			break;
		case CATEGORY:
			data.setCategoryId((int) nsCategories.getValue());
			data.setClubTeamId(0);
			break;
		case TEAM:
			data.setCategoryId(0);
			data.setClubTeamId((int) nsTeams.getValue());
			break;
		}
		data.setPriority(cbPriority.getValue());
		data.setCreationDate(new Date());
		data.setExpirationDate(dfExpiration.getValue());
	}

}
