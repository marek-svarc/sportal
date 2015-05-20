package com.clubeek.ui.components;

import com.clubeek.model.ClubMember;
import com.clubeek.model.Contact;
import com.clubeek.ui.Tools;
import com.clubeek.util.DateTime.DateStyle;
import com.clubeek.util.DateTime;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
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
    private Label lblMemberDetail;

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

        lblMemberDetail = new Label();
        lblMemberDetail.setContentMode(ContentMode.HTML);
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

    private void addBeginTable(StringBuilder text, String title) {
        text.append(String.format("<span class='title'>%s</span><table>", title));
    }

    private void addValue(StringBuilder text, String title, String value) {
        if ((value != null) && !value.isEmpty()) {
            text.append(String.format("<tr><td><em>%s</em>:</td><td>%s</td></tr>", title, value));
        }
    }

    private void addEndTable(StringBuilder text) {
        text.append("</table>");
    }

    private void refresh() {
        lblMemberName.setValue(clubMember.getMemberFullName());
        Tools.Components.fillImageByPortrait(imgPhoto, clubMember.getPhoto(), Integer.toString(clubMember.getId()));

        StringBuilder text = new StringBuilder();

        text.append("<div class='clubMemberCard'>");
        
        addBeginTable(text, "Osobní údaje");
        addValue(text, "Datum narození", DateTime.dateToString(clubMember.getBirthdate(), DateStyle.SHORT_DAY));
        addValue(text, "Rodné číslo", clubMember.getIdPersonal());
        addValue(text, "Registrační číslo", clubMember.getIdRegistration());
        addValue(text, "Charakteristika", "???");
        addEndTable(text);

        addBeginTable(text, "Adresa");
        addValue(text, "Ulice", clubMember.getStreet());
        addValue(text, "Obec", clubMember.getCity());
        addValue(text, "PSČ", clubMember.getCode());
        addEndTable(text);

        if (clubMember.getContacts().size() > 0) {
            addBeginTable(text, "Kontakty");
            for (Contact c : clubMember.getContacts()) {
                text.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>",
                        c.getType(), c.getDescription(), c.getContact(), c.getNotification().name()));
            }
            addEndTable(text);
        }

        text.append("</div>");
        
        lblMemberDetail.setValue(text.toString());
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
