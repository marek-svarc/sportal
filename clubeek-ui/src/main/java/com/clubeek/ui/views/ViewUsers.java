package com.clubeek.ui.views;

import java.util.List;

import com.clubeek.dao.ClubMemberDao;
import com.clubeek.dao.UserDao;
import com.clubeek.dao.impl.ownframework.ClubMemberDaoImpl;
import com.clubeek.dao.impl.ownframework.UserDaoImpl;
import com.clubeek.dao.impl.ownframework.rep.RepClubMember;
import com.clubeek.dao.impl.ownframework.rep.RepUser;
import com.clubeek.model.User;
import com.clubeek.model.User.Role;
import com.clubeek.service.SecurityService;
import com.clubeek.service.impl.Security;
import com.clubeek.service.impl.SecurityServiceImpl;
import com.clubeek.ui.ModalDialog;
import com.clubeek.ui.ModalDialog.Mode;
import com.clubeek.ui.components.ActionTable;
import com.clubeek.ui.frames.FrameUser;
import com.vaadin.data.Container;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ViewUsers extends VerticalLayout implements View, ActionTable.OnActionListener {
	// TODO vitfo, created on 11. 6. 2015 
	private SecurityService securityService = new SecurityServiceImpl();
	// TODO vitfo, created on 11. 6. 2015 
	private UserDao userDao = new UserDaoImpl();
	// TODO vitfo, created on 11. 6. 2015 
    private ClubMemberDao clubMemberDao = new ClubMemberDaoImpl();

    public enum Columns {

        USER_NAME, AUTHORIZATION, TEAM_MEMBER;
    }

    public ViewUsers() {
        this.setCaption(Messages.getString("userManagement")); //$NON-NLS-1$

        ActionTable.UserColumnInfo[] columns = {
            new ActionTable.UserColumnInfo(Columns.USER_NAME, String.class, Messages.getString("userName")),
            new ActionTable.UserColumnInfo(Columns.AUTHORIZATION, String.class, Messages.getString("authorization")),
            new ActionTable.UserColumnInfo(Columns.TEAM_MEMBER, String.class, Messages.getString("teamMember"))
        };

        table = new ActionTable(ActionTable.Action.getStandardSet(false, true), columns, this);
        table.addToOwner(this);
    }

    // interface View
    @Override
    public void enter(ViewChangeEvent event) {

    	securityService.authorize(Role.ADMINISTRATOR);

        // read training list from the database
//        users = RepUser.selectAll(new RepUser.TableColumn[]{RepUser.TableColumn.ID, RepUser.TableColumn.NAME,
//            RepUser.TableColumn.PERMISSIONS, RepUser.TableColumn.CLUB_MEMBER_ID});
    	users = userDao.getAllUsers();
        for (User user : users) {
//            user.setClubMember(RepClubMember.selectById(user.getClubMemberId(), new RepClubMember.TableColumn[]{
//                RepClubMember.TableColumn.ID, RepClubMember.TableColumn.NAME, RepClubMember.TableColumn.SURNAME,
//                RepClubMember.TableColumn.BIRTHDATE}));
            user.setClubMember(clubMemberDao.getClubMember(user.getClubMemberId()));
        }

        // add table rows
        table.removeAllRows();
        if (users != null) {
            Container container = table.createDataContainer();
            for (int i = 0; i < users.size(); ++i) {
                User user = users.get(i);
                table.addRow(container, new Object[]{user.getName(), user.getRole().toString(),
                    user.getClubMember() != null ? user.getClubMember().toString() : ""}, i);
            }
            table.setDataContainer(container);
        }
    }

    // interface ActionTable.OnActionListener
    @Override
    public boolean doAction(ActionTable.Action action, Object data) {
        switch (action) {
            case SINGLE_ADD:
                addUser();
                break;
            case SINGLE_EDIT:
                editUser((int) data);
                break;
            case SINGLE_DELETE:
                deleteUser((int) data);
                break;
        }
        return true;
    }

    public void addUser() {
        User data = new User();
        ModalDialog.show(this, Mode.ADD_ONCE, Messages.getString("newUser"), new FrameUser(),
//                data, RepUser.getInstance(), null);
        		// TODO vitfo, created on 11. 6. 2015 
        		data, userDao, null);
    }

    public void editUser(int id) {
        if (id >= 0) {
            User user = users.get(id);
            ModalDialog.show(this, Mode.EDIT, String.format("%s '%s'", Messages.getString("userProperties"),
//                    user.getName()), new FrameUser(), user, RepUser.getInstance(), null);
            		user.getName()), new FrameUser(), user, userDao, null);
        }
    }

    public void deleteUser(int id) {
        User user = users.get(id);
//        table.deleteRow(user.getId(), id, RepUser.getInstance(), this, null, Columns.USER_NAME, Columns.TEAM_MEMBER);
        table.deleteRow(user.getId(), id, userDao, this, null, Columns.USER_NAME, Columns.TEAM_MEMBER);
    }

    /* PRIVATE */
    /** Table component */
    private final ActionTable table;

    /** List of users loaded from the database */
    private List<User> users = null;
}
