package com.clubeek.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.context.annotation.Scope;

@SuppressWarnings("serial")
@Theme("baseTheme")
@org.springframework.stereotype.Component
@Scope("prototype")
public class LayoutTabSheet extends VerticalLayout implements View {

    /**
     * Zalozky pro zobrazovani vlastnenych komponent
     */
    private TabSheet tabs;
    
    private ViewChangeEvent event;

    public LayoutTabSheet() {

    }

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
        this.event = event;

//		// distribuce udalosti vlastnenym komponentam
//		if (tabs != null) {
//			for (Component c : tabs)
//				if (c instanceof View)
//					((View) c).enter(event);
//		}
    }

    public void addViews(Component... components) {
// kontejner (TabSheet) pro zobrazovanych komponent
        tabs = new TabSheet() {
            
            @Override
            public void fireSelectedTabChange() {
                super.fireSelectedTabChange();
                ((View) getSelectedTab()).enter(event);
            }
        };
        tabs.addStyleName(ValoTheme.TABSHEET_FRAMED);
        tabs.addStyleName(ValoTheme.TABSHEET_COMPACT_TABBAR);
        this.addComponent(tabs);
        for (Component tab : components) {
            tabs.addTab(tab, tab.getCaption());
        }
    }
}
