package com.clubeek.ui.components;

import com.clubeek.model.ClubRival;
import com.clubeek.ui.views.Navigation;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Marek Svarc
 */
public class ClubRivalCard extends VerticalLayout {

    /** Club data */
    private ClubRival clubRival;

    /** Show club title */
    private final Label lblTitle;

    private void Refresh() {
        lblTitle.setValue(clubRival.getName());
    }

    public ClubRivalCard(Navigation navigation) {

        lblTitle = new Label();
        lblTitle.setContentMode(ContentMode.HTML);
        this.addComponent(lblTitle);

    }

    public void setClubRival(ClubRival clubRival) {
        if (this.clubRival != clubRival) {
            this.clubRival = clubRival;
            Refresh();
        }
    }

}
