package com.clubeek.ui.frames;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import com.clubeek.model.ClubMember;
import com.clubeek.ui.ModalInput;
import com.clubeek.ui.Tools;
import com.clubeek.ui.components.ContactField;
import com.clubeek.ui.components.ImageComponent;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.themes.ValoTheme;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("serial")
@Theme("baseTheme")
public class FrameClubMember extends GridLayout implements ModalInput<ClubMember> {

    /**
     * Group of fields.
     */
    private BeanFieldGroup<ClubMember> bfg;

    /**
     * Text field for personal number.
     */
    private TextField tfIdPersonal;
    /**
     * Textové pole pro zadávání FACR
     */
    private TextField tfIdFacr;
    /**
     * Textové pole pro zadávání jména
     */
    private TextField tfFirstName;
    /**
     * Textové pole pro zadávání příjmení
     */
    private TextField tfSurname;
    /**
     * Textové pole pro zadávání data narození
     */
    private PopupDateField tfDateOfBirth;
    /**
     * Textové pole pro zadávání ulice
     */
    private TextField tfStreet;
    /**
     * Textové pole pro zadávání města/obce
     */
    private TextField tfCity;
    /**
     * Textové pole pro zadávání poštovního směrovacího čísla
     */
    private TextField tfZipCode;
    
    private ImageComponent imageComponent;

    /**
     * Zobrazeni fotky
     */
    private Image imPhoto;
    /**
     * Nahrani fotky
     */
    private Upload ldPhoto;
    /**
     * Uzivatelsky nahraná fotkA
     */
    private ByteArrayOutputStream photoOutStream = null;
    /**
     * Název souboru uzivatelsky nahrané fotky
     */
    private String photoFile;

    /**
     * Contact rows.
     */
    private ContactField cfContact;

    public FrameClubMember() {
        setSpacing(true);
        this.setColumns(2);
        this.setRows(2);

        bfg = new BeanFieldGroup<>(ClubMember.class);
        bfg.setBuffered(true);

        // GENEROVANI KOMPONENT
        // osobni udaje
        tfFirstName = Tools.Components.createTextField(Messages.getString("name"), true, null); //$NON-NLS-1$
        tfSurname = Tools.Components.createTextField(Messages.getString("surname"), true, null); //$NON-NLS-1$
        tfIdFacr = Tools.Components.createTextField(Messages.getString("regNo")); //$NON-NLS-1$
        tfDateOfBirth = Tools.Components.createPopupDateField(Messages.getString("dateOfBirth")); //$NON-NLS-1$
        tfIdPersonal = Tools.Components.createTextField(Messages.getString("personalNo")); //$NON-NLS-1$
        cfContact = new ContactField();

        bfg.bind(tfFirstName, "name");
        bfg.bind(tfSurname, "surname");
        bfg.bind(tfIdFacr, "idRegistration");
        bfg.bind(tfDateOfBirth, "birthdate");
        bfg.bind(tfIdPersonal, "idPersonal");
        bfg.bind(cfContact, "contacts");

        // adresa
        tfStreet = Tools.Components.createTextField(Messages.getString("street")); //$NON-NLS-1$
        tfCity = Tools.Components.createTextField(Messages.getString("township")); //$NON-NLS-1$
        tfZipCode = Tools.Components.createTextField(Messages.getString("zipCode")); //$NON-NLS-1$

        bfg.bind(tfStreet, "street");
        bfg.bind(tfCity, "city");
        bfg.bind(tfZipCode, "code");

        // fotografie
        imageComponent = new ImageComponent();
        imageComponent.setImageSize(10240);
        imageComponent.setImageHeight(100);
        imageComponent.setImageWidth(100);
//        ldPhoto = new Upload();
//        ldPhoto.setReceiver(new Receiver() {
//
//            @Override
//            public OutputStream receiveUpload(String filename, String mimeType) {
//                photoOutStream = new ByteArrayOutputStream(10240);
//                photoFile = filename;
//                return photoOutStream;
//            }
//        });
//
//        ldPhoto.addSucceededListener(new SucceededListener() {
//
//            @Override
//            public void uploadSucceeded(SucceededEvent event) {
//                if (photoOutStream != null) {
//                    Tools.Components.fillImageByPortrait(imPhoto, photoOutStream.toByteArray(), photoFile);
//                }
//            }
//        });
//
//        ldPhoto.setImmediate(true);
//        ldPhoto.setButtonCaption(Messages.getString("loadPhoto")); //$NON-NLS-1$
//        imPhoto = new Image();
//        imPhoto.setImmediate(true);
//        imPhoto.setWidth(100, Unit.PIXELS);
//        imPhoto.setHeight(100, Unit.PIXELS);

        // osobni udaje
        Panel pnPersonal = new Panel(Messages.getString("personalData"), Tools.Components.createMultipleColumnsForm(2, //$NON-NLS-1$
                new Component[]{tfFirstName, tfSurname, tfIdFacr, tfDateOfBirth, tfIdPersonal, null}));
        pnPersonal.addStyleName(ValoTheme.PANEL_WELL);
        addComponent(pnPersonal);

        // adresa
        Panel pnAddress = new Panel(Messages.getString("address"), Tools.Components.createMultipleColumnsForm(1, //$NON-NLS-1$
                new Component[]{tfStreet, tfCity, tfZipCode}));
        pnAddress.addStyleName(ValoTheme.PANEL_WELL);
        addComponent(pnAddress);

        Panel pnContacts = new Panel(Messages.getString("contacts"), cfContact); //$NON-NLS-1$
        pnContacts.addStyleName(ValoTheme.PANEL_WELL);
        addComponent(pnContacts);

//        // fotografie
//        HorizontalLayout laPhoto = new HorizontalLayout();
//        laPhoto.setSizeFull();
//        laPhoto.setSpacing(true);
//        laPhoto.setMargin(true);
//        laPhoto.addComponents(ldPhoto, imPhoto);

        Panel pnPhoto = new Panel(Messages.getString("photos"), imageComponent); //$NON-NLS-1$
        pnPhoto.addStyleName(ValoTheme.PANEL_WELL);
        addComponent(pnPhoto);
    }

    // interface ModalInput<ClubMember>
    @Override
    public void dataToInput(ClubMember data) {
        cfContact.setClubMember(data);
        bfg.setItemDataSource(data);

        // fotografie
        //Tools.Components.fillImageByPortrait(imPhoto, data.getPhoto(), Integer.toString(data.getId())); //$NON-NLS-1$
        imageComponent.setPhotoFile(Integer.toString(data.getId()));
        imageComponent.setImage(data.getPhoto());
    }

    @Override
    public void inputToData(ClubMember data) {
        try {
            for (Field<?> field : bfg.getFields()) {
                field.validate();
            }
            bfg.commit();
            if (imageComponent.getImageByteArray() != null) {
                data.setPhoto(imageComponent.getImageByteArray());
            }
//            if (photoOutStream != null) {
//                    data.setPhoto(photoOutStream.toByteArray());
//                }

        } catch (FieldGroup.CommitException | Validator.InvalidValueException ex) {
            Logger.getLogger(FrameClubMember.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Formulář obsahuje chyby!");
        }
    }
}
