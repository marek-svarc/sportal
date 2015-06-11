package com.clubeek.ui.frames;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.clubeek.dao.ClubMemberDao;
import com.clubeek.dao.impl.ownframework.ClubMemberDaoImpl;
import com.clubeek.dao.impl.ownframework.rep.RepClubMember;
import com.clubeek.db.RepClubTeam;
import com.clubeek.model.ClubMember;
import com.clubeek.model.ClubTeam;
import com.clubeek.model.ModelTools;
import com.clubeek.model.Unique;
import com.clubeek.ui.ModalInput;
import com.clubeek.ui.Tools;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.Runo;

@SuppressWarnings("serial")
public class FrameSelectMembers extends VerticalLayout implements ModalInput<List<ClubMember>> {
    // TODO vitfo, created on 11. 6. 2015 
    private ClubMemberDao clubMemberDao = new ClubMemberDaoImpl();

	public static enum FilterType {
		NONE(Messages.getString("doNotFilter")), TEAM(Messages.getString("filterByTeam")), YEAR_OF_BIRTH(Messages.getString("filterByBirthDate")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		public String getText() {
			return text;
		}

		@Override
		public String toString() {
			return text;
		}

		private FilterType(String text) {
			this.text = text;
		}

		private String text;
	}

	public FrameSelectMembers() {

		this.setWidth(400, Unit.POINTS);
		this.addStyleName("rowlast"); //$NON-NLS-1$

		HorizontalLayout horzLayoutFiltr = new HorizontalLayout();
		horzLayoutFiltr.setSizeUndefined();

		// seznam vsech typu filtrovani

		nsFilter = Tools.Components.createNativeSelect(null, Arrays.asList(FilterType.values()));
		nsFilter.addStyleName("inline"); //$NON-NLS-1$
		nsFilter.setImmediate(true);
		nsFilter.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				nsFilterParams.removeAllItems();
				switch (getSelectedFilterType()) {
				// sestaveni seznamu tymu pro filtrovani dle prirazeni k tymu
				case TEAM:
					nsFilterParams.setVisible(true);
                                        List<ClubTeam> teams = RepClubTeam.select(true, null);
                                        Tools.Components.initNativeSelect(nsFilterParams, teams);
                                        if (teams.size() > 0)
                                            nsFilterParams.setValue(teams.get(0));
					break;

				// sestavani seznamu rocniku pro filtrovani dle roku narozeni
				case YEAR_OF_BIRTH:
					nsFilterParams.setVisible(true);
					// aktualni rok
					Calendar today = Calendar.getInstance();
					int todayYear = today.get(Calendar.YEAR);

					// generovani seznamu rocniku narozeni (5 - 20 let)
					List<ClubMemberYear> years = new ArrayList<>();
					years.add(new ClubMemberYear(todayYear - 5, Integer.MAX_VALUE));
					for (int i = 6; i < 20; ++i)
						years.add(new ClubMemberYear(todayYear - i, todayYear - i));
					years.add(new ClubMemberYear(Integer.MIN_VALUE, todayYear - 20));

					// pridani seznamu do komponenty
					Tools.Components.initNativeSelect(nsFilterParams, years);
					if (years.size() > 0)
						nsFilterParams.setValue(years.get(0));
					break;
				default:
					nsFilterParams.setVisible(false);
					break;
				}
			}
		});
		horzLayoutFiltr.addComponent(nsFilter);

		// seznam pro vyber parametru filtru

		nsFilterParams = Tools.Components.createNativeSelect(null, null);
		nsFilterParams.addStyleName("inline"); //$NON-NLS-1$
		horzLayoutFiltr.addComponent(nsFilterParams);

		Button btnFilter = new Button();
		btnFilter.setCaption(Messages.getString("doFilter")); //$NON-NLS-1$
		btnFilter.setStyleName(Runo.BUTTON_SMALL);
		btnFilter.addStyleName("inline"); //$NON-NLS-1$
		btnFilter.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				updateSourceItems(getSelectedItems());
			}
		});
		
		horzLayoutFiltr.addComponent(btnFilter);
		
		
		this.addComponent(horzLayoutFiltr);

		// seznamy pro vyber clenu

		tcSelectMembers = new TwinColSelect(""); //$NON-NLS-1$
		tcSelectMembers.setCaption(null);
		tcSelectMembers.setLeftColumnCaption(null); //$NON-NLS-1$
		tcSelectMembers.setRightColumnCaption(null); //$NON-NLS-1$
		tcSelectMembers.setMultiSelect(true);
		tcSelectMembers.setSizeFull();
		this.addComponent(tcSelectMembers);

		// implicitni hodnoty

		nsFilter.setValue(FilterType.NONE);
	}

	// Vlastnosti

	/** Vraci aktualni typ filtrovani */
	public FilterType getSelectedFilterType() {
		return (FilterType) nsFilter.getValue();
	}

	/** Vraci seznam vybranych clenu tymu */
	@SuppressWarnings("unchecked")
	private Iterable<ClubMemberWrapper> getSelectedItems() {
		return (Iterable<ClubMemberWrapper>) tcSelectMembers.getValue();
	}

	// Rozhrani ModalInput<List<ClubMember>>

	@Override
	public void dataToInput(List<ClubMember> data) {

		// sestaveni seznamu vybranych clenu
		ArrayList<ClubMemberWrapper> items = new ArrayList<>();
		for (ClubMember clubMember : data)
			items.add(new ClubMemberWrapper(clubMember));

		// sestaveni seznamu clenu pro vyber
		updateSourceItems(items);
	}

	@Override
	public void inputToData(List<ClubMember> data) {
		data.clear();
		for (ClubMemberWrapper item : getSelectedItems())
			data.add(item.clubMember);
	}

	/* PRIVATE */

	private final class ClubMemberWrapper implements Unique {

		public ClubMemberWrapper(ClubMember clubMember) {
			this.clubMember = clubMember;
		}

		@Override
		public String toString() {
			return String.format("%s %s", clubMember.getName(), clubMember.getSurname()); //$NON-NLS-1$
		}

		@Override
		public int getId() {
			return clubMember.getId();
		}

		private ClubMember clubMember;
	}

	/** Trida zapouzdrujici informace rpo vyber dle roku narozeni */
	private class ClubMemberYear {

		/** Maximalni pozadovany rocnik */
		public final int yearMin;

		/** Minimalni pozadovany rocnik */
		public final int yearMax;

		public ClubMemberYear(int yearMin, int yearMax) {
			this.yearMin = yearMin;
			this.yearMax = yearMax;
		}

		@Override
		public String toString() {
			if (this.yearMin == Integer.MIN_VALUE)
				return String.format(Messages.getString("yearAndOlder"), this.yearMax); //$NON-NLS-1$
			else if (this.yearMax == Integer.MAX_VALUE)
				return String.format(Messages.getString("yearAndYounger"), this.yearMin); //$NON-NLS-1$
			else if (this.yearMin == this.yearMax)
				return String.format(Messages.getString("yearOnly"), this.yearMin); //$NON-NLS-1$
			else
				return String.format(Messages.getString("yearFromTo"), this.yearMin, this.yearMax); //$NON-NLS-1$
		}
	}

	/** Aktulizuje seznam clenu dle aktualniho filtru */
	private void updateSourceItems(Iterable<ClubMemberWrapper> selectedItems) {
		ArrayList<ClubMemberWrapper> sourceItems = new ArrayList<>();

                List<ClubMember> sourceMembers = null;
                switch (getSelectedFilterType()) {
                    case TEAM:
                        if (nsFilterParams.getValue() instanceof ClubTeam)
//                            sourceMembers = RepClubMember.selectByTeamId(((ClubTeam) nsFilterParams.getValue()).getId(), null);
                            sourceMembers = clubMemberDao.getClubMembersByTeamId(((ClubTeam) nsFilterParams.getValue()).getId());
                        break;
                    case YEAR_OF_BIRTH:
                        if (nsFilterParams.getValue() instanceof ClubMemberYear) {
                            ClubMemberYear clubMemberYear = (ClubMemberYear) nsFilterParams.getValue();
//                            sourceMembers = RepClubMember.dbSelectByYearOfBirth(clubMemberYear.yearMin, clubMemberYear.yearMax, null);
                            sourceMembers = clubMemberDao.getClubMembersByDateOfBirth(clubMemberYear.yearMin, clubMemberYear.yearMax);
                        }
                        break;
                    default:
//                        sourceMembers = RepClubMember.selectAll(null);
                        sourceMembers = clubMemberDao.getAllClubMembers();
                        break;
			}
            if (sourceMembers != null)
				for (ClubMember sourceMember : sourceMembers)
					sourceItems.add(new ClubMemberWrapper(sourceMember));

		// odmazat vybrane rvky ze seznamu
		ModelTools.listRemoveDuplicates(sourceItems, selectedItems);

		// pridani prvku
		tcSelectMembers.removeAllItems();
		for (ClubMemberWrapper sourceItem : sourceItems)
			tcSelectMembers.addItem(sourceItem);

		// oznaceni vybranych prvku
		for (ClubMemberWrapper selectedItem : selectedItems) {
			tcSelectMembers.addItem(selectedItem);
			tcSelectMembers.select(selectedItem);
		}
	}

	/** Seznam pro volbu typu filtrovani */
	private NativeSelect nsFilter;

	/** Seznam tymu pro filtrovani podle tymu */
	private NativeSelect nsFilterParams;

	/** Seznamy pro vyber clenu tymu */
	private TwinColSelect tcSelectMembers;
}
