package com.clubeek.ui.components;

import com.clubeek.model.ClubMember;
import com.clubeek.model.Contact;
import com.clubeek.ui.Tools;
import com.clubeek.util.DateTime.DateStyle;
import com.clubeek.util.DateTime;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Shows personal and statistics informations about club member.
 *
 * @author Lukas Janacek
 * @author Marek Svarc
 */
public class ClubMemberCard extends VerticalLayout {

    private ClubMember clubMember;

    /** Shows club member name */
    private Label lblMemberName;

    /** Run dialog to edit user profile */
    private Button btnEditMemberProfile;

    /** Run dialog to edit user account parameters */
    private Button btnEditMemberAccount;

    /** Shows member photo */
    private Image imgPhoto;

    /** Prints personal informations */
    private FormatedLabel lblMemberDetail;

    /** Shows statistical informations about member */
    private ClubMemberStats ctrMemberStats;

    /** Enable to confirm participations on club actions */
    private ClubMemberActions ctrMemberActions;

    private HorizontalLayout createLayoutTop() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth(100, Unit.PERCENTAGE);
        layout.setSpacing(true);

        lblMemberName = new Label();
        lblMemberName.setWidth(100, Unit.PERCENTAGE);
        lblMemberName.addStyleName(ValoTheme.LABEL_H3);
        lblMemberName.addStyleName(ValoTheme.LABEL_BOLD);
        layout.addComponent(lblMemberName);
        layout.setExpandRatio(lblMemberName, 1);

        btnEditMemberProfile = new Button("Upravit");
        btnEditMemberProfile.setStyleName(ValoTheme.BUTTON_SMALL);
        layout.addComponent(btnEditMemberProfile);

        btnEditMemberAccount = new Button("Přihlašovací údaje");
        btnEditMemberAccount.setStyleName(ValoTheme.BUTTON_SMALL);
        layout.addComponent(btnEditMemberAccount);

        return layout;
    }

    private HorizontalLayout createLayoutMemberDetail() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth(100, Unit.PERCENTAGE);
        layout.setSpacing(true);

        imgPhoto = new Image();
        imgPhoto.setWidth(150, Unit.PIXELS);
        imgPhoto.setHeight(200, Unit.PIXELS);
        layout.addComponent(imgPhoto);

        lblMemberDetail = new FormatedLabel();
        layout.addComponent(lblMemberDetail);

        layout.setExpandRatio(lblMemberDetail, 1);

        return layout;
    }

    private TabSheet createSheetAdvanced() {
        TabSheet sheet = new TabSheet();
        sheet.setWidth(100, Unit.PERCENTAGE);
        sheet.addStyleName(ValoTheme.TABSHEET_FRAMED);

        ctrMemberStats = new ClubMemberStats();
        sheet.addTab(ctrMemberStats, "Statistiky");

        ctrMemberActions = new ClubMemberActions();
        sheet.addTab(ctrMemberActions, "Přihlášení na akce");

        return sheet;
    }

    private void refresh() {
        lblMemberName.setValue(clubMember.getMemberFullName());
        Tools.Components.fillImageByPortrait(imgPhoto, clubMember.getPhoto(), Integer.toString(clubMember.getId()));

        lblMemberDetail.beginWrite();
        try {
            lblMemberDetail.writeTableBegin("Osobní údaje");
            lblMemberDetail.writeTableRow("Datum narození", DateTime.dateToString(clubMember.getBirthdate(), DateStyle.SHORT_DAY));
            lblMemberDetail.writeTableRow("Rodné číslo", clubMember.getIdPersonal());
            lblMemberDetail.writeTableRow("Registrační číslo", clubMember.getIdRegistration());
            lblMemberDetail.writeTableRow("Charakteristika", "Tohle je fakt dlouhej popis fakt fantastickýho hráče, který hraje ve fakt fantastickým týmu. Umí kopnout do balónu a vypike 20 piv.");
            lblMemberDetail.writeTableEnd();

            lblMemberDetail.writeTableBegin("Adresa");
            lblMemberDetail.writeTableRow("Ulice", clubMember.getStreet());
            lblMemberDetail.writeTableRow("Obec", clubMember.getCity());
            lblMemberDetail.writeTableRow("PSČ", clubMember.getCode());
            lblMemberDetail.writeTableEnd();

            if (clubMember.getContacts().size() > 0) {
                lblMemberDetail.writeTableBegin("Kontakty");
                for (Contact c : clubMember.getContacts()) {
                    lblMemberDetail.writeTableRow(c.getType().toString(), c.getDescription(), c.getContact(), c.getNotification().toString());
                }
                lblMemberDetail.writeTableEnd();
            }

        } finally {
            lblMemberDetail.endWrite();
        }
    }

    public ClubMemberCard() {
        this.setWidth(100, Unit.PERCENTAGE);
        this.setSpacing(true);

        this.addComponent(createLayoutTop());
        this.addComponent(createLayoutMemberDetail());
        this.addComponent(createSheetAdvanced());
    }

    public void setClubMember(ClubMember clubMember) {
        this.clubMember = clubMember;
        refresh();
    }
}
