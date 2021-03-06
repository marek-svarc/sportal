package com.clubeek.ui.views;

import java.util.ArrayList;
import java.util.List;

import com.clubeek.dao.ClubMemberDao;
import com.clubeek.dao.ContactDao;
import com.clubeek.dao.impl.ownframework.ClubMemberDaoImpl;
import com.clubeek.dao.impl.ownframework.ContactDaoImpl;
import com.clubeek.enums.UserRoleType;
import com.clubeek.model.ClubMember;
import com.clubeek.model.Contact;
import com.clubeek.service.SecurityService;
import com.clubeek.service.impl.SecurityServiceImpl;
import com.clubeek.ui.ModalDialog;
import com.clubeek.ui.ModalDialog.Mode;
import com.clubeek.ui.Tools;
import com.clubeek.ui.components.ActionTable;
import com.clubeek.ui.frames.FrameClubMember;
import com.vaadin.data.Container;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@SuppressWarnings("serial")
@Component
@Scope("prototype")
public final class ViewClubMembers extends VerticalLayout implements View, ActionTable.OnActionListener {
	// TODO vitfo, created on 11. 6. 2015 
    @Autowired
	private SecurityService securityService;
	// TODO vitfo, created on 11. 6. 2015 
    @Autowired
    private ClubMemberDao clubMemberDao;
    // TODO vitfo, created on 12. 6. 2015
    @Autowired
    private ContactDao contactDao;

    public enum Columns {

        NAME, SURNAME, REG_NO, DATE_OF_BIRTH, PERSONAL_NO, ADDRESS;
    }

    public ViewClubMembers() {
        this.setCaption(Messages.getString("members"));

        ActionTable.UserColumnInfo[] columns = {
            new ActionTable.UserColumnInfo(Columns.NAME, String.class, Messages.getString("name")),
            new ActionTable.UserColumnInfo(Columns.SURNAME, String.class, Messages.getString("surname")),
            new ActionTable.UserColumnInfo(Columns.REG_NO, String.class, Messages.getString("regNo")),
            new ActionTable.UserColumnInfo(Columns.DATE_OF_BIRTH, String.class, Messages.getString("dateOfBirth")),
            new ActionTable.UserColumnInfo(Columns.PERSONAL_NO, String.class, Messages.getString("personalNo")),
            new ActionTable.UserColumnInfo(Columns.ADDRESS, String.class, Messages.getString("address"))
        };

        table = new ActionTable(ActionTable.Action.getStandardSet(false, true), columns, this);
        table.addToOwner(this);
    }

    // ActionTable.OnActionListener
    @Override
    public boolean doAction(ActionTable.Action action, Object data) {
        switch (action) {
            case SINGLE_ADD:
                addMember();
                break;
            case SINGLE_EDIT:
                editMember((int) data);
                break;
            case SINGLE_DELETE:
                deleteMember((int) data);
                break;
        }
        return true;
    }

    // interface View
    @Override
    public void enter(ViewChangeEvent event) {

    	securityService.authorize(UserRoleType.CLUB_MANAGER);

//        members = RepClubMember.selectAll(null);
    	members = clubMemberDao.getAllClubMembers();

        if (members != null) {
            table.removeAllRows();
            Container container = table.createDataContainer();
            for (int i = 0; i < members.size(); ++i) {
                ClubMember member = members.get(i);

                String address = Tools.Strings.concatenateText(
                        new String[]{member.getStreet(), member.getCity(), member.getCode()}, ", ");

                table.addRow(container, new Object[]{member.getName(), member.getSurname(), member.getIdRegistration(),
                    member.getBirthdateAsString(), member.getIdPersonal(), address}, i);
            }
            table.setDataContainer(container);
        }
    }

    // operations
    public void addMember() {
        ModalDialog.show(this, Mode.ADD_ONCE, Messages.getString("newMember"), new FrameClubMember(), new ClubMember(), //$NON-NLS-1$
//                RepClubMember.getInstance(), null);
                clubMemberDao, null);
    }

    public void editMember(int id) {
        // data asociovana s vybranym radkem
        final ClubMember data = members.get(id);
        final ArrayList<Contact> oldContacts = new ArrayList<>(data.getContacts());
        ModalDialog<ClubMember> dlg = new ModalDialog<>(Mode.EDIT, Messages.getString("clubMemberProperties"), //$NON-NLS-1$
                new FrameClubMember(), data, new ClickListener() {

                    @Override
                    public void buttonClick(ClickEvent event) {
//                        RepClubMember.update(data);
                        clubMemberDao.updateClubMember(data);
//                        RepContact.update(oldContacts, data.getContacts());
                        contactDao.updateContacts(oldContacts, data.getContacts());
                        enter(null);
                    }
                });
        getUI().addWindow(dlg);
    }

    public void deleteMember(int id) {
//        table.deleteRow(members.get(id).getId(), id, RepClubMember.getInstance(), this, null, 
        table.deleteRow(members.get(id).getId(), id, clubMemberDao, this, null,
                Columns.NAME, Columns.SURNAME, Columns.DATE_OF_BIRTH);
    }

    /* PRIVATE */
    /** table component */
    private ActionTable table;

    /** List of all club members */
    private List<ClubMember> members = null;

}
