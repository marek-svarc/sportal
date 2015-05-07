/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clubeek.ui.components;

import com.clubeek.model.Contact;
import com.clubeek.model.TeamMember;
import com.clubeek.ui.Tools;
import com.clubeek.util.DateTime.DateStyle;
import com.clubeek.util.DateTime;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @author elopin
 */
public class MemberCard extends VerticalLayout {
    
    private MemberCardListener listener;

    private BeanFieldGroup<TeamMember> bfg;

    private HorizontalLayout topLayout;
    private Label memberName;
    private Button edit;
    private Button sign;

    private HorizontalLayout detailLayout;
    private VerticalLayout photoLayout;
    private Image photo;
    
    private VerticalLayout detailReadLayout;
    private Label birthDate;
    private Label personalNumber;
    private Label registrationNumber;
    private Label description;
    private Label street;
    private Label city;
    private Label cityNumber;
    private Label contact;
    
    private VerticalLayout detailEditLayout;
    private FormLayout detailFormLayout;

    private TabSheet sheet;
    private VerticalLayout stats;
    private VerticalLayout actions;

    private Button changePhoto;
    private boolean editable;

    public MemberCard() {
        setSizeFull();
        bfg = new BeanFieldGroup<>(TeamMember.class);

        topLayout = new HorizontalLayout();
        addComponent(topLayout);
        topLayout.setWidth("100%");

        memberName = new Label();
        memberName.addStyleName(ValoTheme.LABEL_H3);
        memberName.addStyleName(ValoTheme.LABEL_BOLD);
        topLayout.addComponent(memberName);

        edit = new Button(null, new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (edit.getCaption() == null) {
                    setEditLayout();
                } else {
                    setReadLayout();
                }
            }
        });
        edit.setIcon(FontAwesome.PENCIL);
        sign = new Button(null, new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        sign.setIcon(FontAwesome.KEY);
        HorizontalLayout buttonLayout = new HorizontalLayout(edit, sign);
        topLayout.addComponent(buttonLayout);
        topLayout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_RIGHT);

        detailLayout = new HorizontalLayout();
        addComponent(detailLayout);
        detailLayout.setSpacing(true);
        buildDetailLayout();

        sheet = new TabSheet();
        sheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
        addComponent(sheet);

        sheet.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {

            @Override
            public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
                if (event.getSource().equals(stats)) {
                    showStats();
                } else if (event.getSource().equals(actions)) {
                    showActions();
                }
            }
        });

        stats = new MemberStatsTab();
        sheet.addTab(stats, "Statistiky");
        actions = new MemberActionsTab();
        sheet.addTab(actions, "Přihlášení na akce");
    }

    public void setTeamMember(TeamMember member) {
        bfg.setItemDataSource(member);
        
            refresh();
    }

    public TeamMember getDataSource() {
        return bfg.getItemDataSource().getBean();
    }

    private void refresh() {
        memberName.setValue(getDataSource().getClubMember().getMemberFullName());
        
        // detail info
        updateDetailReadLayout();
    }
    
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
    
    private void setEditLayout() {
        detailLayout.removeComponent(detailReadLayout);
        detailLayout.addComponent(detailFormLayout);
        edit.setCaption("Zrušit");
    }
    
    private void setReadLayout() {
        detailLayout.removeComponent(detailFormLayout);
        detailLayout.addComponent(detailReadLayout);
        edit.setCaption(null);
    }

    private void buildDetailLayout() {
        
        photoLayout = new VerticalLayout();
        photo = new Image();
        photo.setWidth(100, Unit.PIXELS);
        photo.setHeight(100, Unit.PIXELS);
        photoLayout.addComponent(photo);
        
        // build readLayout
        Label personalDataCaption = new Label("Osobní údaje");
        personalDataCaption.addStyleName(ValoTheme.LABEL_H3);
        personalDataCaption.addStyleName(ValoTheme.LABEL_BOLD);
        birthDate = new Label();
        personalNumber = new Label();
        registrationNumber = new Label();
        description = new Label();
        Label adresaLabel = new Label("Adresa");
        adresaLabel.addStyleName(ValoTheme.LABEL_H3);
        street = new Label();
        city = new Label();
        cityNumber = new Label();
        Label kontaktCaption = new Label("Kontakty");
        kontaktCaption.addStyleName(ValoTheme.LABEL_H3);
        contact = new Label();
        
        detailReadLayout = new VerticalLayout(personalDataCaption, birthDate, personalNumber, registrationNumber, description,
        adresaLabel, street, city, cityNumber, kontaktCaption, contact);
        detailReadLayout.setSpacing(true);
        // build editLayout
        PopupDateField tfBirthDate = (PopupDateField) bfg.buildAndBind("clubMember.birthdate");
        tfBirthDate.setCaption("Datum narození: ");
        TextField tfPersonalId = (TextField) bfg.buildAndBind("clubMember.idPersonal");
        tfPersonalId.setCaption("Rodné číslo: ");
        TextField tfRegNo = (TextField) bfg.buildAndBind("clubMember.idRegistration");
        tfRegNo.setCaption("Registrační číslo: ");
        
        Button save = new Button("Uložit", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                onSave();
            }
        });
        detailFormLayout = new FormLayout(tfBirthDate, tfPersonalId, tfRegNo, save);
        

    }
    
    private void onSave() {
        
    }
    
    private void updateDetailReadLayout() {
        detailLayout.removeAllComponents();
        
        // photo
        photoLayout.removeAllComponents();
        Tools.Components.fillImageByPortrait(photo, getDataSource().getClubMember().getPhoto(), Integer.toString(getDataSource().getClubMember().getId()));
        photoLayout.addComponent(photo);
        detailLayout.addComponent(photoLayout);
       
        // detail info
        birthDate.setValue("Datum narození: " + DateTime.dateToString(getDataSource().getClubMember().getBirthdate(), DateStyle.DAY));
        personalNumber.setValue("Rodné číslo: " + getDataSource().getClubMember().getIdPersonal());
        registrationNumber.setValue("Registrační číslo: " + getDataSource().getClubMember().getIdRegistration());
        description.setValue("Charakteristika: ?????");
        
        street.setValue("Ulice: " + getDataSource().getClubMember().getStreet());
        city.setValue("Obec: " + getDataSource().getClubMember().getCity());
        cityNumber.setValue("PSČ: " + getDataSource().getClubMember().getCode());
        StringBuilder sb = new StringBuilder();
        for (Contact c : getDataSource().getClubMember().getContacts()) {
            sb.append(c.getType().toString()).append(": ").append(c.getContact()).append(", ").append(c.getDescription()).append(", ").append(c.getNotification().toString()).append("<br/>");
        }
        
        detailLayout.addComponent(detailReadLayout);
    }

    private void showStats() {
        //throw new UnsupportedOperationException(java.util.ResourceBundle.getBundle("com/clubeek/ui/components/messages").getString("NOT SUPPORTED YET.")); //To change body of generated methods, choose Tools | Templates.
    }

    private void showActions() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public interface MemberCardListener {
        void onSave(TeamMember member);
    }
}
