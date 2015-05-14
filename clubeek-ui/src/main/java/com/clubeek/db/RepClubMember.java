package com.clubeek.db;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

import com.clubeek.db.Admin.ColumnData;
import com.clubeek.model.ClubMember;
import com.clubeek.model.Contact;
import java.sql.SQLException;

/**
 * Trida poskytujici pristup do databazove tabulky "club_member"
 *
 * @author Marek Svarc
 *
 */
public class RepClubMember implements Repository<ClubMember> {

    /** Nazev tabulky */
    public static final String tableName = "club_member";

    /** Nazev pohledu club_member - team_member - team */
    public static final String viewClubMemberByTeam = "club_member_by_team";

    public static final String viewClubMemberByTeam_team_id = "team_id";

    public static final String viewClubMemberByTeam_club_member_id = "club_member_id";

    /** Identifikatory sloupcu tabulky */
    public static enum TableColumn {

        ID("id"),
        ID_PERSONAL("id_personal"),
        ID_REGISTRATION("id_registration"),
        NAME("name"), SURNAME("surname"),
        BIRTHDATE("birthdate"),
        STREET("street"),
        CITY("city"),
        CODE("code"),
        PHOTO("photo");

        private TableColumn(String dbColumnName) {
            this.name = dbColumnName;
        }

        @Override
        public String toString() {
            return name;
        }

        public final String name;
    }

    public static RepClubMember getInstance() {
        return clubMemberDb;
    }

    // SQL insert
    /**
     * Vlozi a inicializuje radek v tabulce "club_member". Zaroven vlozi
     * asociovane kontakty do tabulky "contact".
     *
     * @param clubMember data clena klubu, ktera budou zapsana do databaze
     *
     */
    public static int insert(ClubMember clubMember) {
        return insert(clubMember.getIdPersonal(), clubMember.getIdRegistration(), clubMember.getName(), clubMember.getSurname(),
                clubMember.getBirthdate(), clubMember.getStreet(), clubMember.getCity(), clubMember.getCode(),
                clubMember.getPhoto(), clubMember.getContacts());
    }

    /**
     * Vlozi a inicializuje radek v tabulce "club_member". Zaroven vlozi
     * asociovane kontakty do tabulky "contact".
     *
     *
     */
    public static int insert(String idPersonal, String idRegistration, String name, String surname, Date birthdate, String street,
            String city, String code, byte[] photo, List<Contact> contacts) {
        // sestaveni sql prikazu
        String sql = String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s, %s) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                tableName, TableColumn.ID_PERSONAL.name, TableColumn.ID_REGISTRATION.name, TableColumn.NAME.name,
                TableColumn.SURNAME.name, TableColumn.BIRTHDATE.name, TableColumn.STREET.name, TableColumn.CITY.name,
                TableColumn.CODE.name, TableColumn.PHOTO.name);
        // provedeni transakce
        int id = Admin.update(sql, new ColumnData[]{new ColumnData(idPersonal), new ColumnData(idRegistration),
            new ColumnData(name), new ColumnData(surname), new ColumnData(birthdate, true), new ColumnData(street),
            new ColumnData(city), new ColumnData(code), new ColumnData(photo)}, true);
        // vlozeni kontaktu do tabulky "contact"
        for (Contact contact : contacts) {
            RepContact.insert(contact.getType(), contact.getDescription(), contact.getContact(), contact.getNotification(), id);
        }
        return id;
    }

    // SQL Update
    /**
     * Modifikuje radek v tabulce "club_member"
     *
     * @param clubMember data clena klubu, ktera budou zapsana do databaze
     *
     */
    public static void update(ClubMember clubMember) {
        update(clubMember.getId(), clubMember.getIdPersonal(), clubMember.getIdRegistration(), clubMember.getName(),
                clubMember.getSurname(), clubMember.getBirthdate(), clubMember.getStreet(), clubMember.getCity(),
                clubMember.getCode(), clubMember.getPhoto());
    }

    /**
     * Modifikuje radek v tabulce "club_member"
     *
     * @param id index modifikovane radky tabulky
     *
     */
    public static void update(int id, String idPersonal, String idRegistration, String name, String surname, Date birthdate,
            String street, String city, String code, byte[] photo) {
        // sestaveni sql prikazu
        String sql = String.format(
                "UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = %s", tableName,
                TableColumn.ID_PERSONAL.name, TableColumn.ID_REGISTRATION.name, TableColumn.NAME.name, TableColumn.SURNAME.name,
                TableColumn.BIRTHDATE.name, TableColumn.STREET.name, TableColumn.CITY.name, TableColumn.CODE.name,
                TableColumn.PHOTO.name, TableColumn.ID.name, Integer.toString(id));
        // provedeni transakce
        Admin.update(sql, new ColumnData[]{new ColumnData(idPersonal), new ColumnData(idRegistration), new ColumnData(name),
            new ColumnData(surname), new ColumnData(birthdate, true), new ColumnData(street), new ColumnData(city),
            new ColumnData(code), new ColumnData(photo)});
    }

    // SQL delete
    /**
     * Maze radek v tabulce "club_member"
     *
     * @param id unikatni identifikator radky tabulky
     *
     */
    public static void delete(int id) {
        Admin.delete(tableName, TableColumn.ID.name, id);
    }

    // SQL Select
    /**
     * Vraci vsechny radky a vsechny sloupce tabulky
     *
     * @return seznam vsech radek tabulky
     *
     */
    public static List<ClubMember> selectAll(TableColumn[] columns) {
        columns = getColumns(columns);
        return Admin.query(ClubMember.class, String.format("SELECT %s FROM %s", Admin.createSelectParams(columns), tableName),
                columns, getInstance());
    }

    /**
     * Vraci clena klubu dle unikatniho identifikatoru
     *
     * @return data clena klubu
     *
     */
    public static ClubMember selectById(int id, TableColumn[] columns) {
        columns = getColumns(columns);
        List<ClubMember> clubMemberList = Admin.query(ClubMember.class, String.format("SELECT %s FROM %s WHERE %s = %d",
                Admin.createSelectParams(columns), tableName, TableColumn.ID, id), columns, getInstance());
        return (clubMemberList != null) && (clubMemberList.size() == 1) ? clubMemberList.get(0) : null;
    }

    public static List<ClubMember> selectByTeamId(int id, TableColumn[] columns) {
        columns = getColumns(columns);
        return Admin.query(ClubMember.class, String.format("SELECT %s FROM %s WHERE %s = %d", Admin.createSelectParams(columns),
                viewClubMemberByTeam, viewClubMemberByTeam_team_id, id), columns, getInstance());
    }

    public static List<ClubMember> dbSelectByYearOfBirth(int yearMin, int yearMax, TableColumn[] columns) {
        columns = getColumns(columns);
        return Admin.query(ClubMember.class, String.format("SELECT %s FROM %s WHERE (YEAR(%s) >= %d) AND (YEAR(%s) <= %d)",
                Admin.createSelectParams(columns), tableName, TableColumn.BIRTHDATE, yearMin, TableColumn.BIRTHDATE,
                yearMax), columns, getInstance());
    }

    public static List<ClubMember> dbSelectByBirthdate(int olderOrEqualTo, int youngerOrEqualTo, TableColumn[] columns) {
        columns = getColumns(columns);
        return Admin.query(ClubMember.class, String.format(
                "SELECT %s , TRUNC(MONTHS_BETWEEN(LOCALTIMESTAMP, %s) / 12) AS age FROM %s WHERE (age >= %d) AND (age <= %d)",
                Admin.createSelectParams(columns), TableColumn.BIRTHDATE, tableName, olderOrEqualTo, youngerOrEqualTo),
                columns, getInstance());
    }

    // Rozhrani Globals.SqlReadable<ClubMember>
    @Override
    public void insertRow(ClubMember value) {
        insert(value);
    }

    @Override
    public void updateRow(ClubMember value) {
        update(value);
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
    public void readValue(ResultSet result, int resultsColumnId, ClubMember data, Object dataColumnId) {
        try {
            switch ((RepClubMember.TableColumn) dataColumnId) {
                case ID:
                    data.setId(result.getInt(resultsColumnId));
                    break;
                case ID_PERSONAL:
                    data.setIdPersonal(result.getString(resultsColumnId));
                    break;
                case ID_REGISTRATION:
                    data.setIdRegistration(result.getString(resultsColumnId));
                    break;
                case NAME:
                    data.setName(result.getString(resultsColumnId));
                    break;
                case SURNAME:
                    data.setSurname(result.getString(resultsColumnId));
                    break;
                case STREET:
                    data.setStreet(result.getString(resultsColumnId));
                    break;
                case CITY:
                    data.setCity(result.getString(resultsColumnId));
                    break;
                case CODE:
                    data.setCode(result.getString(resultsColumnId));
                    break;
                case BIRTHDATE:
                    data.setBirthdate(result.getDate(resultsColumnId));
                    break;
                case PHOTO:
                    data.setPhoto(result.getBytes(resultsColumnId));
                    break;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* PRIVATE */
    private static RepClubMember.TableColumn[] getColumns(RepClubMember.TableColumn[] columns) {
        return columns != null ? columns : TableColumn.values();
    }

    public RepClubMember() {
    }

    private final static RepClubMember clubMemberDb = new RepClubMember();
}
