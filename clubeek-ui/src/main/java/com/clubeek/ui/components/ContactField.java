package com.clubeek.ui.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.clubeek.enums.ContactType;
import com.clubeek.model.ClubMember;
import com.clubeek.model.Contact;
import com.clubeek.ui.Tools;
import com.clubeek.ui.Tools.Validators;
import com.vaadin.data.Buffered;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Custom field for edit contact of club member.
 *
 * @author lukas
 */
public class ContactField extends CustomField<List<Contact>> {

    private VerticalLayout layout;

    private ClubMember clubMember;

    private List<BeanFieldGroup> bfgs;
    private Map<Contact, HorizontalLayout> rows;

    private Button addContact;

    public ContactField() {
        layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.setSizeFull();
        bfgs = new ArrayList<>();
        rows = new HashMap<>();

        addContact = new Button("Přidat kontakt", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                addEmptyRow();
            }
        });
        addContact.addStyleName(ValoTheme.BUTTON_TINY);
        layout.addComponent(addContact);
    }

    /**
     * Set club member.
     *
     * @param clubMember club member
     */
    public void setClubMember(ClubMember clubMember) {
        this.clubMember = clubMember;
    }

    @Override
    public Class getType() {
        return Collection.class;
    }

    @Override
    protected void setInternalValue(List<Contact> newValue) {
        super.setInternalValue(newValue);
        if (getValue() != null) {
            if (clubMember.getId() == 0) {
                addEmptyRow();
            }
            for (Contact c : getValue()) {
                addRowForContact(c);
            }
        }
    }

    @Override
    public void validate() throws InvalidValueException {
        
        //BeanFieldGroup.isValid() returns boolean value, we need InvalidValueException
        //hence we have to validate every field from group separately
        for (BeanFieldGroup bfg : bfgs) {
            for (Field<?> field : bfg.getFields()) {
                field.validate();
            }
        }
        super.validate();
    }

    @Override
    public void commit() throws Buffered.SourceException, InvalidValueException {
        getValue().clear();
        for (BeanFieldGroup bfg : bfgs) {
            try {
                bfg.commit();
                getValue().add((Contact) bfg.getItemDataSource().getBean());
            } catch (FieldGroup.CommitException ex) {
                Logger.getLogger(ContactField.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        super.commit();
    }

    @Override
    protected Component initContent() {
        return layout;
    }

    /**
     * Adds a row for new contact.
     */
    private void addEmptyRow() {
        Contact contact = new Contact();
        if (clubMember.getId() != 0) {
            contact.setClubMemberId(clubMember.getId());
        }
        addRowForContact(contact);
    }

    /**
     * Add row for contact.
     *
     * @param contact contact
     */
    private void addRowForContact(Contact contact) {

        ComboBox cbContactType = new ComboBox(null, Arrays.asList(ContactType.values()));
        cbContactType.addStyleName(ValoTheme.COMBOBOX_TINY);
        cbContactType.setNullSelectionAllowed(false);
        cbContactType.setInputPrompt("Typ");
        Validators.setRequired(cbContactType, true, null);

        TextField tfName = Tools.Components.createTextField(null);
        tfName.setInputPrompt("Popis");
        TextField tfContact = Tools.Components.createTextField(null, true, null);
        tfContact.setInputPrompt("Kontakt");

        ComboBox cbContactNotify = new ComboBox(null, Arrays.asList(Contact.NotificationType.values()));
        cbContactNotify.addStyleName(ValoTheme.COMBOBOX_TINY);
        cbContactNotify.setNullSelectionAllowed(false);
        cbContactNotify.setInputPrompt("Upozornění");
        Validators.setRequired(cbContactNotify, true, null);

        final BeanFieldGroup<Contact> cbfg = new BeanFieldGroup<>(Contact.class);
        cbfg.setItemDataSource(contact);
        cbfg.setBuffered(true);
        bfgs.add(cbfg);

        cbfg.bind(cbContactType, "type");
        cbfg.bind(tfName, "description");
        cbfg.bind(tfContact, "contact");
        cbfg.bind(cbContactNotify, "notification");

        Button remove = new Button();
        remove.setIcon(FontAwesome.TRASH_O);
        remove.addStyleName(ValoTheme.BUTTON_TINY);
        remove.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                removeContact(cbfg);
            }
        });

        HorizontalLayout row = new HorizontalLayout(cbContactType, tfName, tfContact, cbContactNotify, remove);
        row.setSpacing(true);

        if (contact.getId() != 0) {
            layout.addComponentAsFirst(row);
        } else {
            layout.addComponent(row, layout.getComponentIndex(addContact));
        }
        rows.put(contact, row);
    }

    /**
     * Remove contact.
     *
     * @param cbfg field group for contact
     */
    private void removeContact(BeanFieldGroup<Contact> cbfg) {
        bfgs.remove(cbfg);

        Contact bean = cbfg.getItemDataSource().getBean();
        layout.removeComponent(rows.get(bean));
        rows.remove(bean);
        getValue().remove(bean);
    }
}
