package com.clubeek.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@Theme("baseTheme")
public class LayoutTabSheet extends VerticalLayout implements View {

	public LayoutTabSheet(Component... components) {

		// kontejner (TabSheet) pro zobrazovanych komponent
		tabs = new TabSheet(components);
		tabs.addStyleName(ValoTheme.TABSHEET_FRAMED);
		tabs.addStyleName(ValoTheme.TABSHEET_COMPACT_TABBAR);
		this.addComponent(tabs);
		for (Component tab : components) {
			tabs.addTab(tab, tab.getCaption());
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {

		// distribuce udalosti vlastnenym komponentam
		if (tabs != null) {
			for (Component c : tabs)
				if (c instanceof View)
					((View) c).enter(event);
		}
	}

	/** Zalozky pro zobrazovani vlastnenych komponent */
	private TabSheet tabs;
}
