package com.clubeek.ui.frames;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.clubeek.enums.TeamFunctionType;
import com.clubeek.model.TeamMember;
import com.clubeek.ui.ModalInput;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class FrameTeamFunctions extends VerticalLayout implements ModalInput<TeamMember> {

	public FrameTeamFunctions() {
		
		this.addStyleName("rowlast"); //$NON-NLS-1$
		
		// seznam pro vybirani funkci v tymu
		ogTeamFunctions = new OptionGroup();
		ogTeamFunctions.setSizeFull();
		ogTeamFunctions.setMultiSelect(true);
		for (TeamFunctionType function : allTeamFunctions)
			ogTeamFunctions.addItem(function);
		this.addComponent(ogTeamFunctions);
	}

	@Override
	public void dataToInput(TeamMember data) {
		List<TeamFunctionType> teamFunctions = new ArrayList<>();

		for (TeamFunctionType teamFunction : allTeamFunctions)
			if (teamFunction.isFlag(data.getFunctions()))
				teamFunctions.add(teamFunction);

		ogTeamFunctions.setValue(teamFunctions);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void inputToData(TeamMember data) {
		Collection<TeamFunctionType> teamFunctions = (Collection<TeamFunctionType>) ogTeamFunctions.getValue();

		int teamFunctionsFlags = 0;
		for (TeamFunctionType teamFunction : teamFunctions)
			teamFunctionsFlags |= teamFunction.toFlag();

		data.setFunctions(teamFunctionsFlags);
	}

	private OptionGroup ogTeamFunctions;

	private TeamFunctionType[] allTeamFunctions = TeamFunctionType.values();
}
