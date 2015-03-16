package com.clubeek.ui.views;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.clubeek.db.RepClubMember;
import com.clubeek.db.RepContact;
import com.clubeek.model.ClubMember;
import com.clubeek.model.Contact;
import com.clubeek.model.User.Role;
import com.clubeek.ui.ModalDialog;
import com.clubeek.ui.Security;
import com.clubeek.ui.Tools;
import com.clubeek.ui.ModalDialog.Mode;
import com.clubeek.ui.components.TableWithButtons;
import com.clubeek.ui.frames.FrameClubMember;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public final class ViewClubMembers extends VerticalLayout implements View {

    public enum Columns {

        NAME, SURNAME, REG_NO, DATE_OF_BIRTH, PERSONAL_NO, ADDRESS;
    }

    public ViewClubMembers() {
        this.setCaption(Messages.getString("members"));

        TableWithButtons.UserColumnInfo[] columns = {
            new TableWithButtons.UserColumnInfo(Columns.NAME, String.class, Messages.getString("name")),
            new TableWithButtons.UserColumnInfo(Columns.SURNAME, String.class, Messages.getString("surname")),
            new TableWithButtons.UserColumnInfo(Columns.REG_NO, String.class, Messages.getString("regNo")),
            new TableWithButtons.UserColumnInfo(Columns.DATE_OF_BIRTH, String.class, Messages.getString("dateOfBirth")),
            new TableWithButtons.UserColumnInfo(Columns.PERSONAL_NO, String.class, Messages.getString("personalNo")),
            new TableWithButtons.UserColumnInfo(Columns.ADDRESS, String.class, Messages.getString("address"))
        };

        table = new TableWithButtons(TableWithButtons.CtrlColumn.getStandardSet(), columns, null);
        table.addToOwner(this);
    }

    // interface View
    @Override
    public void enter(ViewChangeEvent event) {

        Security.authorize(Role.CLUB_MANAGER);

        try {
            members = RepClubMember.selectAll(null);

            table.removeAllRows();
            for (int i = 0; i < members.size(); ++i) {
                ClubMember member = members.get(i);

                String address = Tools.Strings.concatenateText(
                        new String[]{member.getStreet(), member.getCity(), member.getCode()}, ", "); //$NON-NLS-1$

                table.addRow(new Object[]{member.getName(), member.getSurname(), member.getIdRegistration(),
                    member.getBirthdateAsString(), member.getIdPersonal(), address}, new Integer(i));
            }
        } catch (SQLException e) {
            Tools.msgBoxSQLException(e);
        }
    }

    /* PRIVATE */
    /** Spusti modalni dialog pro pridani noveho clena klubu */
    public void addMember() {
        ModalDialog.show(this, Mode.ADD_ONCE, Messages.getString("newMember"), new FrameClubMember(), new ClubMember(), //$NON-NLS-1$
                RepClubMember.getInstance(), null);
    }

    /**
     * Spusti modalni dialog pro zmenu vlastnosti vybraneho clena klubu
     *
     * @throws SQLException
     */
    public void editSelectedMember() throws SQLException {
        if (table.getSelectedRow() >= 0) {
            // data asociovana s vybranym radkem
            final ClubMember data = members.get(table.getSelectedRow());
            // kopie kontaktu
            final ArrayList<Contact> oldContacts = new ArrayList<>(data.getContacts());
            // vytvoreni dialogu
            ModalDialog<ClubMember> dlg = new ModalDialog<ClubMember>(Mode.EDIT, Messages.getString("clubMemberProperties"), //$NON-NLS-1$
                    new FrameClubMember(), data, new ClickListener() {

                        @Override
                        public void buttonClick(ClickEvent event) {
                            try {
                                // zmena vlastnosti clena v databazi
                                RepClubMember.update(data);
                                // zmena kontaktu v databazi
                                RepContact.update(oldContacts, data.getContacts());
                                // aktualizace stranky
                                enter(null);
                            } catch (SQLException e) {
                                Tools.msgBoxSQLException(e);
                            }
                        }
                    });
            // zobrazeni dialogu
            getUI().addWindow(dlg);
        }
    }

    /** Zobrazi dotaz uzivateli a pripadne odstrani vybranou ktegorii */
    public void deleteSelectedMember() {
        try {
            // vymazani prvku z databaze
            if ((table.getSelectedRow() >= 0) && (table.getSelectedRow() < members.size())) {
                RepClubMember.delete(members.get(table.getSelectedRow()).getId());
            }
            // aktualizace stranky
            enter(null);
        } catch (SQLException e) {
            Tools.msgBoxSQLException(e);
        }
    }

    /* PRIVATE */
    /** Komponenty tabulky */
    private TableWithButtons table;

    /** Seznam aktualne zobrazenych kategorii */
    private List<ClubMember> members = null;

}
