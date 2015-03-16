package com.clubeek.ui.views;

import java.sql.SQLException;
import java.util.List;

import com.clubeek.db.RepClubMember;
import com.clubeek.db.RepUser;
import com.clubeek.model.User;
import com.clubeek.model.User.Role;
import com.clubeek.ui.ModalDialog;
import com.clubeek.ui.Security;
import com.clubeek.ui.Tools;
import com.clubeek.ui.ModalDialog.Mode;
import com.clubeek.ui.components.TableWithButtons;
import com.clubeek.ui.frames.FrameUser;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ViewUsers extends VerticalLayout implements View {

    public enum Columns {

        USER_NAME, AUTHORIZATION, TEAM_MEMBER;
    }

    public ViewUsers() {
        this.setCaption(Messages.getString("userManagement")); //$NON-NLS-1$

        TableWithButtons.UserColumnInfo[] columns = {
            new TableWithButtons.UserColumnInfo(Columns.USER_NAME, String.class, Messages.getString("userName")),
            new TableWithButtons.UserColumnInfo(Columns.AUTHORIZATION, String.class, Messages.getString("authorization")),
            new TableWithButtons.UserColumnInfo(Columns.TEAM_MEMBER, String.class, Messages.getString("teamMember"))
        };

        table = new TableWithButtons(TableWithButtons.CtrlColumn.getStandardSet(), columns, null);
        table.addToOwner(this);
    }

    public void insertUser() {
        User data = new User();
        ModalDialog.show(this, Mode.ADD_ONCE, Messages.getString("newUser"), new FrameUser(), data, RepUser.getInstance(), null); //$NON-NLS-1$
    }

    public void editUser() {
        if (table.getSelectedRow() >= 0) {
            User user = users.get(table.getSelectedRow());
            ModalDialog.show(this, Mode.EDIT, String.format("%s '%s'", Messages.getString("userProperties"), user.getName()), new FrameUser(), //$NON-NLS-1$ //$NON-NLS-2$
                    user, RepUser.getInstance(), null);
        }
    }

    public void deleteUser() {
        table.deleteSelectedRow(users, RepUser.getInstance(), this, null);
    }

    @Override
    public void enter(ViewChangeEvent event) {

        Security.authorize(Role.ADMINISTRATOR);

        // read training list from the database
        users = null;
        try {
            users = RepUser.selectAll(new RepUser.TableColumn[]{RepUser.TableColumn.ID, RepUser.TableColumn.NAME,
                RepUser.TableColumn.PERMISSIONS, RepUser.TableColumn.CLUB_MEMBER_ID});
            for (User user : users) {
                user.setClubMember(RepClubMember.selectById(user.getClubMemberId(), new RepClubMember.TableColumn[]{
                    RepClubMember.TableColumn.ID, RepClubMember.TableColumn.NAME, RepClubMember.TableColumn.SURNAME,
                    RepClubMember.TableColumn.BIRTHDATE}));
            }
        } catch (SQLException e) {
            Tools.msgBoxSQLException(e);
        }

        // add table rows
        table.removeAllRows();
        if (users != null) {
            for (int i = 0; i < users.size(); ++i) {
                User user = users.get(i);
                table.addRow(new Object[]{user.getName(), user.getRole().toString(),
                    user.getClubMember() != null ? user.getClubMember().toString() : ""}, i);
            }
        }
    }

    /* PRIVATE */
    private final TableWithButtons table;

    private List<User> users = null;
}
