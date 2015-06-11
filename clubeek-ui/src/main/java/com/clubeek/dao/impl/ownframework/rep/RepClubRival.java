package com.clubeek.dao.impl.ownframework.rep;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.clubeek.db.Admin;
import com.clubeek.db.Repository;
import com.clubeek.db.Admin.ColumnData;
import com.clubeek.model.ClubRival;

public class RepClubRival implements Repository<ClubRival> {

    /** Nazev tabulky */
    public static final String tableName = "t_club_rival";

    /** Identifikatory sloupcu tabulky */
    public static enum TableColumn {

        ID("id"),
        NAME("name"),
        WEB("web"),
        GPS("gps"),
        STREET("street"),
        CITY("city"),
        CODE("code"),
        ICON("icon");

        private TableColumn(String dbColumnName) {
            this.name = dbColumnName;
        }

        @Override
        public String toString() {
            return name;
        }

        public final String name;
    }

    public static RepClubRival getInstance() {
        return clubDb;
    }

    // DML insert
    /**
     * Vlozi a inicializuje radek v tabulce "club".
     *
     * @param club data klubu, ktera budou zapsana do databaze
     *
     */
    public static int insert(ClubRival club) {
        return insert(club.getName(), club.getWeb(), club.getGPS(), club.getStreet(), club.getCity(), club.getCode(),
                club.getIcon());
    }

    /**
     * Vlozi a inicializuje radek v tabulce "club".
     *
     *
     */
    public static int insert(String name, String web, String gps, String street, String city, String code, byte[] icon) {
        // sestaveni sql prikazu
        String sql = String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s) VALUES ( ?, ?, ?, ?, ?, ?, ?)", tableName,
                TableColumn.NAME.name, TableColumn.WEB.name, TableColumn.GPS.name, TableColumn.STREET.name,
                TableColumn.CITY.name, TableColumn.CODE.name, TableColumn.ICON.name);
        // provedeni transakce
        return Admin.update(sql, new ColumnData[]{new ColumnData(name), new ColumnData(web), new ColumnData(gps),
            new ColumnData(street), new ColumnData(city), new ColumnData(code), new ColumnData(icon)}, true);
    }

    // DML Update
    /**
     * Modifikuje radek v tabulce "club"
     *
     * @param club data klubu, ktera budou zapsana do databaze
     *
     */
    public static void update(ClubRival club) {
        update(club.getId(), club.getName(), club.getWeb(), club.getGPS(), club.getStreet(), club.getCity(), club.getCode(),
                club.getIcon());
    }

    /**
     * Modifikuje radek v tabulce "club"
     *
     * @param id index modifikovane radky tabulky
     *
     */
    public static void update(int id, String name, String web, String gps, String street, String city, String code, byte[] small_icon) {
        // sestaveni sql prikazu
        String sql = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = %d",
                tableName, TableColumn.NAME.name, TableColumn.WEB.name, TableColumn.GPS.name, TableColumn.STREET.name,
                TableColumn.CITY.name, TableColumn.CODE.name, TableColumn.ICON.name, TableColumn.ID.name, id);
        // provedeni transakce
        Admin.update(sql, new ColumnData[]{new ColumnData(name), new ColumnData(web), new ColumnData(gps),
            new ColumnData(street), new ColumnData(city), new ColumnData(code), new ColumnData(small_icon)});
    }

    // DML delete
    /**
     * Maze radek urceny primarnim klicem v tabulce "club"
     *
     * @param id unikatni identifikator radky tabulky
     *
     */
    public static void delete(int id) {
        Admin.delete(tableName, TableColumn.ID.name, id);
    }

    // SQL Select
    /**
     * Vraci vsechny radky a vsechny sloupce tabulky "club"
     *
     * @return seznam vsech radek tabulky
     *
     */
    public static List<ClubRival> selectAll(TableColumn[] columns) {
        columns = getColumns(columns);
        return Admin.query(ClubRival.class, String.format("SELECT %s FROM %s ORDER BY %s ASC", Admin.createSelectParams(columns),
                tableName, TableColumn.NAME), columns, RepClubRival.getInstance());
    }

    /**
     * Vraci klub z tabulky "club" dle primarniho klice
     *
     * @return data clena klubu
     *
     */
    public static ClubRival selectById(int id, TableColumn[] columns) {
        columns = getColumns(columns);
        List<ClubRival> clubList = Admin.query(ClubRival.class, String.format("SELECT %s FROM %s WHERE %s = %d ORDER BY %s ASC",
                Admin.createSelectParams(columns), tableName, TableColumn.ID, id, TableColumn.NAME), columns, getInstance());
        return (clubList != null) && (clubList.size() == 1) ? clubList.get(0) : null;
    }

    // interface Globals.SqlExtension<Club>
    @Override
    public void updateRow(ClubRival value) {
        update(value);
    }

    @Override
    public void insertRow(ClubRival value) {
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
    public void readValue(ResultSet result, int resultsColumnId, ClubRival data, Object dataColumnId) {
        try {
            switch ((RepClubRival.TableColumn) dataColumnId) {
                case ID:
                    data.setId(result.getInt(resultsColumnId));
                    break;
                case NAME:
                    data.setName(result.getString(resultsColumnId));
                    break;
                case WEB:
                    data.setWeb(result.getString(resultsColumnId));
                    break;
                case GPS:
                    data.setGPS(result.getString(resultsColumnId));
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
                case ICON:
                    data.setIcon(result.getBytes(resultsColumnId));
                    break;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* PRIVATE */
    private RepClubRival() {
    }

    private static RepClubRival.TableColumn[] getColumns(RepClubRival.TableColumn[] columns) {
        return columns != null ? columns : TableColumn.values();
    }

    private final static RepClubRival clubDb = new RepClubRival();
}
