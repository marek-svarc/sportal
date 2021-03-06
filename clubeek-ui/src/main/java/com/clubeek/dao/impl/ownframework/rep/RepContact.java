package com.clubeek.dao.impl.ownframework.rep;

import java.sql.ResultSet;
import java.util.List;

import com.clubeek.dao.impl.ownframework.rep.Admin.ColumnData;
import com.clubeek.enums.ContactType;
import com.clubeek.enums.NotificationType;
import com.clubeek.model.Contact;

/**
 * Trida poskytujici pristup do databazove tabulky "t_contact"
 *
 * @author Marek Svarc
 *
 */
public class RepContact implements Repository<Contact> {

    /** Nazev tabulky */
    public static final String tableName = "t_contact";

    /** Identifikatory sloupcu tabulky */
    public enum TableColumn {

        ID("id"),
        TYPE("type"),
        DESCRIPTION("description"),
        CONTACT("contact"),
        NOTIFICATION("notification"),
        CLUB_MEMBER_ID("club_member_id");

        private TableColumn(String dbColumnName) {
            this.name = dbColumnName;
        }

        @Override
        public String toString() {
            return name;
        }

        public final String name;
    }

    public static RepContact getInstance() {
        return contactDb;
    }

    // SQL Insert
    /**
     * Vlozi a inicializuje radek v tabulce "contact"
     *
     * @param contact data jednoho kontaktu, ktera budou vlozena do databaze
     */
    public static void insert(Contact contact) {
        insert(contact.getContactType(), contact.getDescription(), contact.getContact(), contact.getNotificationType(),
                contact.getClubMemberId());
    }

    /**
     * Vlozi a inicializuje radek v tabulce "contact"
     */
    public static void insert(ContactType type, String description, String contact,
            NotificationType notification, int memberId) {
        // sestaveni sql prikazu
        String sql = String.format("INSERT INTO %s (%s, %s, %s, %s, %s) VALUES ( ?, ?, ?, ?, ?)", tableName,
                TableColumn.TYPE.name, TableColumn.DESCRIPTION.name, TableColumn.CONTACT.name, TableColumn.NOTIFICATION.name,
                TableColumn.CLUB_MEMBER_ID.name);
        // provedeni transakce
        Admin.update(sql, new ColumnData[]{new ColumnData(type.ordinal()), new ColumnData(description),
            new ColumnData(contact), new ColumnData(notification.ordinal()), new ColumnData(memberId)});
    }

    // DML delete
    public static void delete(int id) {
        Admin.delete(tableName, TableColumn.ID.name, id);
    }

    // SQL Update
    /**
     * Aktualizuje kontakty pro jednoho clena porovnanim starych a novych
     * kontaktu
     *
     * @param oldContacts seznam puvodnich kontaktu
     * @param newContacts seznam novych kontaktu
     */
    public static void update(List<Contact> oldContacts, List<Contact> newContacts) {
        Admin.synchronize(oldContacts, newContacts, getInstance());
    }

    /**
     * Modifikuje radek tabulky dle prametru od
     *
     * @param contact data kontaktu
     */
    public static void update(Contact contact) {
        update(contact.getId(), contact.getContactType(), contact.getDescription(), contact.getContact(), contact.getNotificationType(),
                contact.getClubMemberId());
    }

    /**
     * Modifikuje radek tabulky dle prametru od
     *
     * @param id index modifikovane radky tabulky
     * @param description popis kategorie
     * @param priority priorita kategorie
     */
    public static void update(int id, ContactType type, String description, String contact,
            NotificationType notification, int clubMemberId) {
        // sestaveni sql prikazu
        String sql = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = %s", tableName,
                TableColumn.TYPE.name, TableColumn.DESCRIPTION.name, TableColumn.CONTACT.name, TableColumn.NOTIFICATION.name,
                TableColumn.CLUB_MEMBER_ID.name, TableColumn.ID.name, Integer.toString(id));
        // provedeni transakce
        Admin.update(sql, new ColumnData[]{new ColumnData(type.ordinal()), new ColumnData(description),
            new ColumnData(contact), new ColumnData(notification.ordinal()), new ColumnData(clubMemberId)});
    }

    // SQL Select
    /**
     * Vraci seznam kontaktu asociovanych se clenem klubu
     *
     * @return seznam kontaktu
     */
    public static List<Contact> selectByClubMemberId(int clubMemberId, TableColumn[] columns) {
        columns = getColumns(columns);
        return Admin.query(Contact.class, String.format("SELECT %s FROM %s WHERE %s = %s", Admin.createSelectParams(columns),
                tableName, TableColumn.CLUB_MEMBER_ID.name, Integer.toString(clubMemberId)), columns, getInstance());
    }

    // Rozhrani Globals.SqlExtension<Contact, ContactDb.TableColumn>
    @Override
    public void updateRow(Contact value) {
        update(value);
    }

    @Override
    public void insertRow(Contact value) {
        insert(value);
    }

    @Override
    public void deleteRow(int id) {
        delete(id);
    }

    @Override
    public void exchangeRows(int idA, int idB) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void readValue(ResultSet result, int resultsColumnId, Contact data, Object dataColumnId) {
        try {
            switch ((RepContact.TableColumn) dataColumnId) {
                case ID:
                    data.setId(result.getInt(TableColumn.ID.name));
                    break;
                case TYPE:
                    data.setContactType(ContactType.values()[result.getInt(TableColumn.TYPE.name)]);
                    break;
                case DESCRIPTION:
                    data.setDescription(result.getString(TableColumn.DESCRIPTION.name));
                    break;
                case CONTACT:
                    data.setContact(result.getString(TableColumn.CONTACT.name));
                    break;
                case NOTIFICATION:
                    data.setNotificationType(NotificationType.values()[result.getInt(TableColumn.NOTIFICATION.name)]);
                    break;
                case CLUB_MEMBER_ID:
                    data.setClubMemberId(result.getInt(TableColumn.CLUB_MEMBER_ID.name));
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* PRIVATE */
    private static RepContact.TableColumn[] getColumns(RepContact.TableColumn[] columns) {
        return columns != null ? columns : TableColumn.values();
    }

    private RepContact() {
    }

    private final static RepContact contactDb = new RepContact();
}
