package com.clubeek.ui.frames;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import com.clubeek.model.ClubMember;
import com.clubeek.model.Contact;
import com.clubeek.model.Contact.ContactType;
import com.clubeek.model.Contact.NotificationType;
import com.clubeek.ui.ModalInput;
import com.clubeek.ui.Tools;
import com.vaadin.annotations.Theme;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;

@SuppressWarnings("serial")
@Theme("baseTheme")
public class FrameClubMember extends HorizontalLayout implements ModalInput<ClubMember> {

	public FrameClubMember() {
		super();

		// GENEROVANI KOMPONENT

		// osobni udaje
		tfFirstName = Tools.Components.createTextField(Messages.getString("name"), true, null); //$NON-NLS-1$
		tfSurname = Tools.Components.createTextField(Messages.getString("surname"), true, null); //$NON-NLS-1$
		tfIdFacr = Tools.Components.createTextField(Messages.getString("regNo")); //$NON-NLS-1$
		tfDateOfBirth = Tools.Components.createPopupDateField(Messages.getString("dateOfBirth")); //$NON-NLS-1$
		tfIdPersonal = Tools.Components.createTextField(Messages.getString("personalNo")); //$NON-NLS-1$

		// adresa
		tfStreet = Tools.Components.createTextField(Messages.getString("street")); //$NON-NLS-1$
		tfCity = Tools.Components.createTextField(Messages.getString("township")); //$NON-NLS-1$
		tfZipCode = Tools.Components.createTextField(Messages.getString("zipCode")); //$NON-NLS-1$

		// kontakty
		glContacts = new GridLayout(5, 1);
		glContacts.setWidth(100, Unit.PIXELS);
		glContacts.setStyleName("table"); //$NON-NLS-1$
		glContacts.addComponent(new Label(Messages.getString("type")), 0, 0); //$NON-NLS-1$
		glContacts.addComponent(new Label(Messages.getString("description")), 1, 0); //$NON-NLS-1$
		glContacts.addComponent(new Label(Messages.getString("contact")), 2, 0); //$NON-NLS-1$
		glContacts.addComponent(new Label(Messages.getString("message")), 3, 0); //$NON-NLS-1$
		glContacts.setColumnExpandRatio(2, 1f);
		glContacts.setColumnExpandRatio(3, 2f);

		Iterator<Component> iter = glContacts.iterator();
		while (iter.hasNext())
			iter.next().addStyleName("table"); //$NON-NLS-1$

		Button btContactAdd = new Button(Messages.getString("addNewContact"), new ClickListener() { //$NON-NLS-1$

					@Override
					public void buttonClick(ClickEvent event) {
						addContactComponent(-1, ContactType.EMAIL, "", "", NotificationType.ALWAYS); //$NON-NLS-1$ //$NON-NLS-2$
						setupContactComponents();
					}
				});
		btContactAdd.setStyleName(Runo.BUTTON_LINK);
		btContactAdd.addStyleName("table"); //$NON-NLS-1$

		// fotografie
		ldPhoto = new Upload();

		ldPhoto.setReceiver(new Receiver() {

			@Override
			public OutputStream receiveUpload(String filename, String mimeType) {
				photoOutStream = new ByteArrayOutputStream(10240);
				photoFile = filename;
				return photoOutStream;
			}
		});

		ldPhoto.addSucceededListener(new SucceededListener() {

			@Override
			public void uploadSucceeded(SucceededEvent event) {
				if (photoOutStream != null)
					Tools.Components.fillImageByPortrait(imPhoto, photoOutStream.toByteArray(), photoFile);
			}
		});

		// ldPhoto.setSizeFull();
		ldPhoto.setImmediate(true);
		ldPhoto.setButtonCaption(Messages.getString("loadPhoto")); //$NON-NLS-1$
		imPhoto = new Image();
		imPhoto.setImmediate(true);
		imPhoto.setWidth(100, Unit.PIXELS);
		imPhoto.setHeight(100, Unit.PIXELS);

		// ZAROVNANI KOMPONENT

		// layout pro levy sloupec

		VerticalLayout laColumnA = new VerticalLayout();
		laColumnA.setWidth(480, Unit.PIXELS);
		laColumnA.setStyleName("horzgap"); //$NON-NLS-1$
		this.addComponent(laColumnA);

		// layout pro pravy sloupec

		VerticalLayout laColumnB = new VerticalLayout();
		laColumnB.setWidth(220, Unit.PIXELS);
		this.addComponent(laColumnB);

		// osobni udaje

		Panel pnPersonal = new Panel(Messages.getString("personalData"), Tools.Components.createMultipleColumnsForm(2, //$NON-NLS-1$
				new Component[] { tfFirstName, tfSurname, tfIdFacr, tfDateOfBirth, tfIdPersonal, null }));
		pnPersonal.setStyleName(Runo.PANEL_LIGHT);
		pnPersonal.addStyleName("medium"); //$NON-NLS-1$
		pnPersonal.setSizeFull();
		laColumnA.addComponent(pnPersonal);

		// adresa

		Panel pnAddress = new Panel(Messages.getString("address"), Tools.Components.createMultipleColumnsForm(1, //$NON-NLS-1$
				new Component[] { tfStreet, tfCity, tfZipCode }));
		pnAddress.setStyleName(Runo.PANEL_LIGHT);
		pnAddress.addStyleName("medium"); //$NON-NLS-1$
		pnAddress.setSizeFull();
		laColumnB.addComponent(pnAddress);

		// kontakty

		VerticalLayout laContacts = new VerticalLayout();
		laContacts.setSizeFull();
		laContacts.setStyleName("table"); //$NON-NLS-1$
		laContacts.addStyleName("rowfirst"); //$NON-NLS-1$
		laContacts.addStyleName("rowlast"); //$NON-NLS-1$
		laContacts.addComponents(glContacts, btContactAdd);

		Panel pnContacts = new Panel(Messages.getString("contacts"), laContacts); //$NON-NLS-1$
		pnContacts.setStyleName(Runo.PANEL_LIGHT);
		pnContacts.addStyleName("medium"); //$NON-NLS-1$
		laColumnA.addComponent(pnContacts);

		// fotografie

		HorizontalLayout laPhoto = new HorizontalLayout();
		laPhoto.setSizeFull();
		laPhoto.addStyleName("rowfirst"); //$NON-NLS-1$
		laPhoto.addStyleName("rowlast"); //$NON-NLS-1$
		laPhoto.addComponents(ldPhoto, imPhoto);
		laPhoto.setComponentAlignment(imPhoto, Alignment.TOP_CENTER);
		laPhoto.setExpandRatio(imPhoto, 1.0f);

		Panel pnPhoto = new Panel(Messages.getString("photos"), laPhoto); //$NON-NLS-1$
		pnPhoto.setStyleName(Runo.PANEL_LIGHT);
		pnPhoto.addStyleName("medium"); //$NON-NLS-1$
		laColumnB.addComponent(pnPhoto);
	}

	// interface ModalInput<ClubMember>

	@Override
	public void dataToInput(ClubMember data) {
		// osobni udaje
		tfFirstName.setValue(data.getName());
		tfSurname.setValue(data.getSurname());
		tfDateOfBirth.setValue(data.getBirthdate());
		tfIdPersonal.setValue(data.getIdPersonal());
		tfIdFacr.setValue(data.getIdRegistration());
		// adresa
		tfStreet.setValue(data.getStreet());
		tfCity.setValue(data.getCity());
		tfZipCode.setValue(data.getCode());
		// kontakty
		Contact contact;
		try {
			if (data.getContacts() != null)
				for (int i = 0; i < data.getContacts().size(); ++i) {
					contact = data.getContacts().get(i);
					addContactComponent(contact.getId(), contact.getType(), contact.getDescription(), contact.getContact(),
							contact.getNotification());
				}
		} catch (SQLException e) {
			Tools.msgBoxSQLException(e);
		}
		setupContactComponents();
		// fotografie
		Tools.Components.fillImageByPortrait(imPhoto, data.getPhoto(), Integer.toString(data.getId())); //$NON-NLS-1$
	}

	@Override
	public void inputToData(ClubMember data) {
		tfFirstName.validate();
		tfSurname.validate();
		
		data.setName(tfFirstName.getValue());
		data.setSurname(tfSurname.getValue());
		data.setBirthdate(tfDateOfBirth.getValue());
		data.setIdPersonal(tfIdPersonal.getValue());
		data.setIdRegistration(tfIdFacr.getValue());
		data.setStreet(tfStreet.getValue());
		data.setCity(tfCity.getValue());
		data.setCode(tfZipCode.getValue());
		ArrayList<Contact> contacts = new ArrayList<>();
		Contact contact;
		for (ContactComponent item : alContactsComponents) {
			contact = new Contact();
			contact.setId(item.Id);
			contact.setClubMemberId(data.getId());
			contact.setType((ContactType) item.ContactType.getValue());
			contact.setDescription(item.ContactName.getValue());
			contact.setContact(item.Contact.getValue());
			contact.setNotification((NotificationType) item.NotifyType.getValue());
			contacts.add(contact);
		}
		data.setContacts(contacts);
		if (photoOutStream != null)
			data.setPhoto(photoOutStream.toByteArray());
	}

	/* PRIVATE */

	private class ContactComponent {

		public ContactComponent(int id, NativeSelect contactType, TextField contactName, TextField contact,
				NativeSelect notifyType, NativeButton delete) {
			this.Id = id;
			this.ContactType = contactType;
			this.ContactName = contactName;
			this.Contact = contact;
			this.NotifyType = notifyType;
			this.Delete = delete;
		}

		/** Unikátní identifikátor kontaktu */
		final public int Id;
		/** Seznam pro zadání popisu kontaktu */
		final public NativeSelect ContactType;
		/** Textové pole pro zadání popisu kontaktu */
		final public TextField ContactName;
		/** Textové pole pro zadávání kontaktu */
		final public TextField Contact;
		/** Seznam pro zadání způsobu zasílání upozornění */
		final public NativeSelect NotifyType;
		/** Tlačítko pro odmazávání kontaktů */
		final public NativeButton Delete;
	}

	/** Metoda přídá jednu řadu komponent pro zadávání kontaktu */
	private void addContactComponent(int id, ContactType contactType, String description, String contact,
			NotificationType notification) {
		NativeSelect nsContactType, nsContactNotify;
		TextField tfName, tfContact;
		NativeButton btDelete;

		// index nové řady
		int index = glContacts.getRows();
		glContacts.insertRow(index);

		// zadavani typu kontaktu
		nsContactType = Tools.Components.createNativeSelect(null, java.util.Arrays.asList(ContactType.values()));
		nsContactType.addStyleName("table"); //$NON-NLS-1$
		nsContactType.setSizeUndefined();
		nsContactType.setValue(contactType);
		glContacts.addComponent(nsContactType, 0, index);

		// zadavani popisu kontaktu
		tfName = new TextField();
		tfName.setStyleName(Runo.TEXTFIELD_SMALL);
		tfName.addStyleName("table"); //$NON-NLS-1$
		tfName.setSizeUndefined();;
		tfName.setValue(description);
		glContacts.addComponent(tfName, 1, index);

		// zadavani hodnoty kontakt
		tfContact = new TextField(null, contact);
		tfContact.setStyleName(Runo.TEXTFIELD_SMALL);
		tfContact.addStyleName("table"); //$NON-NLS-1$
		tfContact.setSizeUndefined();
		tfContact.setValue(contact);
		glContacts.addComponent(tfContact, 2, index);

		// zadavani zpusobu upozorneni
		nsContactNotify = Tools.Components.createNativeSelect(null, java.util.Arrays.asList(NotificationType.values()));
		nsContactNotify.addStyleName("table"); //$NON-NLS-1$
		nsContactNotify.setSizeUndefined();
		nsContactNotify.setValue(notification);
		glContacts.addComponent(nsContactNotify, 3, index);

		// tlacitko pro odstraneni kontaktu
		ThemeResource iconDel = Tools.IconId.ICO_7X7_DELETE_RED.image;
		btDelete = new NativeButton("", new ClickListener() { //$NON-NLS-1$

					@Override
					public void buttonClick(ClickEvent event) {
						int index = (Integer) event.getButton().getData();
						glContacts.removeRow(index);
						alContactsComponents.remove(index - 1);
						for (int i = index - 1; i < alContactsComponents.size(); ++i)
							alContactsComponents.get(i).Delete.setData((Integer) alContactsComponents.get(i).Delete.getData() - 1);
					}
				});
		btDelete.addStyleName("table"); //$NON-NLS-1$
		btDelete.setWidth(20, Unit.PIXELS);
		btDelete.setIcon(iconDel);
		btDelete.setDescription(Messages.getString("deleteContact")); //$NON-NLS-1$
		btDelete.setData(new Integer(index));
		glContacts.addComponent(btDelete, 4, index);

		// pridani komponent do seznamu
		alContactsComponents.add(new ContactComponent(id, nsContactType, tfName, tfContact, nsContactNotify, btDelete));
	}

	/**
	 * Metoda hromadně nastaví vizuální charakteristiky všem kompoentámpro
	 * zadávání kontaktů
	 */
	private void setupContactComponents() {
		Iterator<Component> components = glContacts.iterator();
		Component c;
		while (components.hasNext()) {
			c = components.next();
			c.setHeight(20, Unit.PIXELS);
		}
	}

	/** Textové pole pro zadávání rodného čísla */
	private TextField tfIdPersonal;
	/** Textové pole pro zadávání FACR */
	private TextField tfIdFacr;
	/** Textové pole pro zadávání jména */
	private TextField tfFirstName;
	/** Textové pole pro zadávání příjmení */
	private TextField tfSurname;
	/** Textové pole pro zadávání data narození */
	private PopupDateField tfDateOfBirth;
	/** Textové pole pro zadávání ulice */
	private TextField tfStreet;
	/** Textové pole pro zadávání města/obce */
	private TextField tfCity;
	/** Textové pole pro zadávání poštovního směrovacího čísla */
	private TextField tfZipCode;
	/** Tabulka kontaktů */
	private GridLayout glContacts;
	/** Seznam textových polí pro zadávání typu kontaktu kontaktu */
	private ArrayList<ContactComponent> alContactsComponents = new ArrayList<>();
	/** Zobrazeni fotky */
	private Image imPhoto;
	/** Nahrani fotky */
	private Upload ldPhoto;
	/** Uzivatelsky nahraná fotkA */
	private ByteArrayOutputStream photoOutStream = null;
	/** Název souboru uzivatelsky nahrané fotky */
	private String photoFile;
}
