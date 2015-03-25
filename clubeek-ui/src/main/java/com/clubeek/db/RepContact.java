package com.clubeek.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.clubeek.db.Admin.ColumnData;
import com.clubeek.model.Contact;

/**
 * Trida poskytujici pristup do databazove tabulky "contact"
 *
 * @author Marek Svarc
 *
 */
public class RepContact implements Repository<Contact> {

    /** Nazev tabulky */
    public static final String tableName = "contact";

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
     * @throws SQLException
     */
    public static void insert(Contact contact) throws SQLException {
        insert(contact.getType(), contact.getDescription(), contact.getContact(), contact.getNotification(),
                contact.getClubMemberId());
    }

    /**
     * Vlozi a inicializuje radek v tabulce "contact"
     *
     * @throws SQLException
     */
    public static void insert(Contact.ContactType type, String description, String contact,
            Contact.NotificationType notification, int memberId) throws SQLException {
        // sestaveni sql prikazu
        String sql = String.format("INSERT INTO %s (%s, %s, %s, %s, %s) VALUES ( ?, ?, ?, ?, ?)", tableName,
                TableColumn.TYPE.name, TableColumn.DESCRIPTION.name, TableColumn.CONTACT.name, TableColumn.NOTIFICATION.name,
                TableColumn.CLUB_MEMBER_ID.name);
        // provedeni transakce
        Admin.update(sql, new ColumnData[]{new ColumnData(type.ordinal()), new ColumnData(description),
            new ColumnData(contact), new ColumnData(notification.ordinal()), new ColumnData(memberId)});
    }

    // DML delete
    public static void delete(int id) throws SQLException {
        Admin.delete(tableName, TableColumn.ID.name, id);
    }

    // SQL Update
    /**
     * Aktualizuje kontakty pro jednoho clena porovnanim starych a novych
     * kontaktu
     *
     * @param oldContacts seznam puvodnich kontaktu
     * @param newContacts seznam novych kontaktu
     * @throws SQLException
     */
    public static void update(List<Contact> oldContacts, List<Contact> newContacts) throws SQLException {
        Admin.synchronize(oldContacts, newContacts, getInstance());
    }

    /**
     * Modifikuje radek tabulky dle prametru od
     *
     * @param contact data kontaktu
     * @throws SQLException
     */
    public static void update(Contact contact) throws SQLException {
        update(contact.getId(), contact.getType(), contact.getDescription(), contact.getContact(), contact.getNotification(),
                contact.getClubMemberId());
    }

    /**
     * Modifikuje radek tabulky dle prametru od
     *
     * @param id index modifikovane radky tabulky
     * @param description popis kategorie
     * @param priority priorita kategorie
     * @throws SQLException
     */
    public static void update(int id, Contact.ContactType type, String description, String contact,
            Contact.NotificationType notification, int clubMemberId) throws SQLException {
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
     * @throws SQLException
     */
    public static List<Contact> selectByClubMemberId(int clubMemberId, TableColumn[] columns) throws SQLException {
        columns = getColumns(columns);
        return Admin.query(Contact.class, String.format("SELECT %s FROM %s WHERE %s = %s", Admin.createSelectParams(columns),
                tableName, TableColumn.CLUB_MEMBER_ID.name, Integer.toString(clubMemberId)), columns, getInstance());
    }

    // Rozhrani Globals.SqlExtension<Contact, ContactDb.TableColumn>
    @Override
    public void updateRow(Contact value) throws SQLException {
        update(value);
    }

    @Override
    public void insertRow(Contact value) throws SQLException {
        insert(value);
    }

    @Override
    public void deleteRow(int id) throws SQLException {
        delete(id);
    }

    @Override
    public void exchangeRows(int idA, int idB) throws SQLException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void readValue(ResultSet result, int resultsColumnId, Contact data, Object dataColumnId) throws SQLException {
        switch ((RepContact.TableColumn) dataColumnId) {
            case ID:
                data.setId(result.getInt(TableColumn.ID.name));
                break;
            case TYPE:
                data.setType(Contact.ContactType.values()[result.getInt(TableColumn.TYPE.name)]);
                break;
            case DESCRIPTION:
                data.setDescription(result.getString(TableColumn.DESCRIPTION.name));
                break;
            case CONTACT:
                data.setContact(result.getString(TableColumn.CONTACT.name));
                break;
            case NOTIFICATION:
                data.setNotification(Contact.NotificationType.values()[result.getInt(TableColumn.NOTIFICATION.name)]);
                break;
            case CLUB_MEMBER_ID:
                data.setClubMemberId(result.getInt(TableColumn.CLUB_MEMBER_ID.name));
                break;
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
