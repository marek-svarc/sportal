package com.clubeek.ui.frames;

import java.util.Arrays;
import java.util.List;

import com.clubeek.dao.UserDao;
import com.clubeek.dao.impl.ownframework.UserDaoImpl;
import com.clubeek.db.RepClubMember;
import com.clubeek.db.RepUser;
import com.clubeek.model.ClubMember;
import com.clubeek.model.User;
import com.clubeek.ui.ModalInput;
import com.clubeek.ui.Tools;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;

@SuppressWarnings("serial")
public class FrameUser extends VerticalLayout implements ModalInput<User> {
	// TODO vitfo, created on 11. 6. 2015 
	private UserDao userDao = new UserDaoImpl();

	public FrameUser() {
		this.setWidth(300, Unit.PIXELS);
		
		tfName = Tools.Components.createTextField("Uživatelské jméno", true, "Je třeba zadata uživatelské jméno.");
		nsClubMember = Tools.Components.createNativeSelect("Člen klubu", Arrays.asList(User.Role.values()));
		nsRole = Tools.Components.createNativeSelect("Oprávnění", Arrays.asList(User.Role.values()));
		
		cbChangePassword = Tools.Components.createCheckBox("Změnit heslo");
		cbChangePassword.setValue(false);
		cbChangePassword.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				updatePasswordComponents();
			}
		});
		
		tfNewPassword1 = Tools.Components.createPasswordField("Heslo");
		tfNewPassword2 = Tools.Components.createPasswordField("Heslo znovu");
	}

	@Override
	public void dataToInput(User data) {
		boolean insert = data.getId() < 1;

		if (insert) {
			List<ClubMember> clubMembers = null;
                        clubMembers = RepClubMember.selectAll(new RepClubMember.TableColumn[] { RepClubMember.TableColumn.ID,
                            RepClubMember.TableColumn.NAME, RepClubMember.TableColumn.SURNAME, RepClubMember.TableColumn.BIRTHDATE });
			Tools.Components.initNativeSelect(nsClubMember, clubMembers, true);

			nsRole.setValue(data.getRole());
			tfNewPassword1.setRequired(true);
			tfNewPassword2.setRequired(true);

			this.addComponents(createComponentsPanel("Vlastnosti uživatele", nsRole, nsClubMember),
					createComponentsPanel("Přihlašovací údaje", tfName, tfNewPassword1, tfNewPassword2));
		} else {

			nsRole.setValue(data.getRole());
			this.addComponent(new FormLayout(nsRole));
			this.addComponent(cbChangePassword);
			FormLayout flPassword = new FormLayout(tfNewPassword1, tfNewPassword2);
			flPassword.addStyleName("topborder"); //$NON-NLS-1$
			this.addComponents(flPassword);
			updatePasswordComponents();
		}
	}

	@Override
	public void inputToData(User data) {
		boolean insertMode = data.getId() < 1;

		if (insertMode) {

			// zakladni kontroly vstupu
			tfName.validate();
			tfNewPassword1.validate();
			tfNewPassword2.validate();

			// pokrocilejsi kontroly vstupu
			testNewPassword();
			testUserName();
			testClubMember();

			// prirazeni dat
			data.setRole((User.Role) nsRole.getValue());
			Object clubMemberId = nsClubMember.getValue();
			if (clubMemberId != null) {
				data.setClubMemberId((int) clubMemberId);
			}
			data.setName(tfName.getValue());
			data.setPassword(tfNewPassword1.getValue());

		} else {
			data.setRole((User.Role) nsRole.getValue());
			if (cbChangePassword.getValue()) {
				
				// kontroly vstupu
				tfNewPassword1.validate();
				tfNewPassword2.validate();
				testNewPassword();
				
				data.setPassword(tfNewPassword1.getValue());
			}else{
				data.setPassword(null);
			}
		}
	}

	/* PRIVATE */

	// testy platnosti
	
	/** testuje spravne zadani noveho pristupoveho hesla */
	private void testNewPassword() {
		if (!tfNewPassword1.getValue().equals(tfNewPassword2.getValue()))
			throw new Validator.EmptyValueException("Jsou zadaná různá hesla.");
	}

	/** testuje zda zadane uzivatelske jmeno jiz neexistuje */
	private void testUserName() {
//            if (RepUser.selectByName(tfName.getValue(), false, null) != null)
			if (userDao.getUserByName(tfName.getValue(), false) != null)
                throw new Validator.EmptyValueException(String.format("Uživatel %s již existuje.", tfName.getValue()));
	}

	/** Testuje zda clen klubu jiz nema vytvoreny ucet */
	private void testClubMember() {
		if (nsClubMember.getValue() != null) {
			User user = null;
//                        user = RepUser.selectByClubMemberId((int) nsClubMember.getValue(), new RepUser.TableColumn[] {
//                            RepUser.TableColumn.ID, RepUser.TableColumn.CLUB_MEMBER_ID });
						user = userDao.getUserByClubMemberId((int) nsClubMember.getValue());
                        if (user != null){
                            user.setClubMember(RepClubMember.selectById(user.getClubMemberId(), null));
                            throw new Validator.EmptyValueException(String.format("Člen klubu '%s' již má přiřazen účet.", user
                                    .getClubMember().toString()));
				}
		}

	}

	// aktualizace komponent

	/** Aktualizuje komponenty pro zadavani hesla */
	private void updatePasswordComponents() {
		boolean enabled = cbChangePassword.getValue();
		tfNewPassword1.setEnabled(enabled);
		tfNewPassword1.setRequired(enabled);
		tfNewPassword2.setEnabled(enabled);
		tfNewPassword2.setRequired(enabled);
	}

	/** Vytvori panel pro zadavani skupiny polozek */
	private Panel createComponentsPanel(String caption, Component... components) {
		Panel pn = new Panel(caption, new FormLayout(components));
		pn.setStyleName(Runo.PANEL_LIGHT);
		pn.addStyleName("medium");
		return pn;
	}

	/** Textove pole pro zadavani uzivatelskeho jmena */
	private TextField tfName;

	/** Prepinac pro povoleni zmeny hesla */
	private CheckBox cbChangePassword;

	/** Textove pole pro zadani hesla */
	private PasswordField tfNewPassword1;

	/** Textove pole pro overeni zadaneho hesla */
	private PasswordField tfNewPassword2;

	/** Seznam pro vyber role */
	private NativeSelect nsRole;

	/** Seznam pro vyber asociovaneho uzivatele */
	private NativeSelect nsClubMember;

}
