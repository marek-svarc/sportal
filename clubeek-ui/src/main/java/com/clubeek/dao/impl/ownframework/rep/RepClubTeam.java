package com.clubeek.dao.impl.ownframework.rep;

import java.sql.ResultSet;
import java.util.List;

import com.clubeek.db.Admin;
import com.clubeek.db.Repository;
import com.clubeek.db.Admin.ColumnData;
import com.clubeek.model.ClubTeam;

import java.sql.SQLException;

public class RepClubTeam implements Repository<ClubTeam> {

    /* PUBLIC */
    /** Nazev tabulky */
    public static final String tableName = "t_club_team";

    /** Identifikatory sloupcu tabulky */
    public enum TableColumn {

        ID("id"),
        NAME("name"),
        ACTIVE("active"),
        SORTING("sorting"),
        CATEGORY_ID("category_id");

        private TableColumn(String dbColumnName) {
            this.name = dbColumnName;
        }

        @Override
        public String toString() {
            return name;
        }

        public final String name;
    }

    public static RepClubTeam getInstance() {
        return teamDb;
    }

    // SQL Insert
    /**
     * Vlozi a inicializuje radek v tabulce "team"
     *
     * @param team data pro jeden tym, ktera budou vlozena do databaze
     */
    public static void insert(ClubTeam team) {
        insert(team.getName(), team.getActive(), team.getCategoryId());
    }

    /**
     * Vlozi a inicializuje radek v tabulce "team"
     *
     * @param name Nazev tymu
     * @param active priznak zda je tym zobrazovan
     * @param categoryId kategorie do ktere je tym zarazen
     */
    public static void insert(String name, Boolean active, int categoryId) {
        // sestaveni sql prikazu
        String sql = String.format("INSERT INTO %s (%s, %s, %s) VALUES ( ? , ? , ?)", tableName, TableColumn.NAME.name,
                TableColumn.ACTIVE.name, TableColumn.CATEGORY_ID.name);
        // provedeni transakce
        int id = Admin.update(sql, new ColumnData[]{new ColumnData(name), new ColumnData(active),
            new ColumnData(categoryId > 0 ? categoryId : Integer.MIN_VALUE)}, true);
        // zatridedni dle prirazeneho id
        update(id, id);
    }

    // SQL Update
    /**
     * Modifikuje radek tabulky "team" dle prametru id
     *
     * @param team data pro jeden tym, ktera budou vlozena do databaze
     */
    public static void update(ClubTeam team) {
        update(team.getId(), team.getName(), team.getActive(), team.getCategoryId());
    }

    /**
     * Modifikuje radek tabulky "team" dle prametru id
     *
     * @param name Nazev tymu
     * @param active priznak zda je tym zobrazovan
     * @param categoryId kategorie do ktere je tym zarazen
     *
     */
    public static void update(int id, String name, Boolean active, int categoryId) {
        // sestaveni sql prikazu
        String sql = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ? WHERE %s = %d", tableName, TableColumn.NAME.name,
                TableColumn.ACTIVE.name, TableColumn.CATEGORY_ID.name, TableColumn.ID.name, id);
        // provedeni transakce
        Admin.update(sql, new ColumnData[]{new ColumnData(name), new ColumnData(active),
            new ColumnData(categoryId > 0 ? categoryId : Integer.MIN_VALUE)});
    }

    /**
     * Modifikuje hodnotu definujici defaultni razeni tabulky "Team"
     *
     * @param id index modifikovane radky tabulky
     * @param sorting hodnota definujci razeni
     *
     */
    public static void update(int id, int sorting) {
        // sestaveni sql prikazu
        String sql = String.format("UPDATE %s SET %s = ? WHERE %s = %d", tableName, TableColumn.SORTING.name,
                TableColumn.ID.name, id);
        // provedeni transakce
        Admin.update(sql, new ColumnData[]{new ColumnData(sorting)});
    }

    /**
     * Prohodi hodnotu ve sloupci "sortId" ve dvou radkach tabulky "Team"
     *
     * @param idA index radky tabulky
     * @param idB index radky tabulky
     *
     */
    public static void exchange(int idA, int idB) {
        ClubTeam teamA = selectById(idA, null);
        ClubTeam teamB = selectById(idB, null);
        if ((teamA != null) && (teamB != null)) {
            update(idA, teamB.getSorting());
            update(idB, teamA.getSorting());
        }
    }

    // SQL delete
    public static void delete(int id) {
        Admin.delete(tableName, TableColumn.ID.name, id);
    }

    // SQL Select
    /**
     * Vraci vsechny radky a vsechny sloupce tabulky "team"
     *
     * @return seznam vsech radek tabulky
     *
     */
    public static List<ClubTeam> select(boolean onlyActive, TableColumn[] columns) {
        columns = getColumns(columns);
        if (onlyActive) {
            return Admin.query(ClubTeam.class, String.format("SELECT %s FROM %s WHERE %s = true ORDER BY %s ASC",
                    Admin.createSelectParams(columns), tableName, TableColumn.ACTIVE, TableColumn.SORTING), columns,
                    getInstance());
        } else {
            return Admin.query(ClubTeam.class, String.format("SELECT %s FROM %s ORDER BY %s ASC",
                    Admin.createSelectParams(columns), tableName, TableColumn.SORTING), columns, getInstance());
        }
    }

    /**
     * Vraci seznam tymu, ktere patri do stejne kategorie
     *
     * @return seznam vsech radek tabulky
     *
     */
    public static List<ClubTeam> select(int category, boolean onlyActive, TableColumn[] columns) {
        columns = getColumns(columns);

        String categoryCheck = TableColumn.CATEGORY_ID + (category <= 0 ? " IS NULL" : " = " + Integer.toString(category));

        if (onlyActive) {
            return Admin.query(ClubTeam.class, String.format("SELECT %s FROM %s WHERE %s AND %s = true ORDER BY %s ASC",
                    Admin.createSelectParams(columns), tableName, categoryCheck, TableColumn.ACTIVE, TableColumn.SORTING),
                    columns, getInstance());
        } else {
            return Admin.query(ClubTeam.class, String.format("SELECT %s FROM %s WHERE %s ORDER BY %s ASC",
                    Admin.createSelectParams(columns), tableName, categoryCheck, TableColumn.SORTING), columns, getInstance());
        }
    }

    /**
     * Vraci tym dle unikatniho identifikatoru
     *
     * @return tym
     *
     */
    public static ClubTeam selectById(int id, TableColumn[] columns) {
        columns = getColumns(columns);
        List<ClubTeam> teamList = Admin.query(ClubTeam.class, String.format("SELECT %s FROM %s WHERE %s = %s",
                Admin.createSelectParams(columns), tableName, TableColumn.ID.name, Integer.toString(id)), columns, getInstance());
        return (teamList != null) && (teamList.size() == 1) ? teamList.get(0) : null;
    }

    // Rozhrani Globals.SqlExtension<Team, TeamDb.TableColumn>
    @Override
    public void insertRow(ClubTeam value) {
        RepClubTeam.insert(value);
    }

    @Override
    public void updateRow(ClubTeam value) {
        RepClubTeam.update(value);
    }

    @Override
    public void deleteRow(int id) {
        RepClubTeam.delete(id);
    }

    @Override
    public void exchangeRows(int idA, int idB) {
        RepClubTeam.exchange(idA, idB);
    }

    @Override
    public void readValue(ResultSet result, int resultsColumnId, ClubTeam data, Object dataColumnId) {
        try {
            switch ((RepClubTeam.TableColumn) dataColumnId) {
                case ID:
                    data.setId(result.getInt(TableColumn.ID.name));
                    break;
                case NAME:
                    data.setName(result.getString(TableColumn.NAME.name));
                    break;
                case ACTIVE:
                    data.setActive(result.getBoolean(TableColumn.ACTIVE.name));
                    break;
                case SORTING:
                    data.setSorting(result.getInt(TableColumn.SORTING.name));
                    break;
                case CATEGORY_ID:
                    data.setCategoryId(result.getInt(TableColumn.CATEGORY_ID.name));
                    break;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* PRIVATE */
    private static RepClubTeam.TableColumn[] getColumns(RepClubTeam.TableColumn[] columns) {
        return columns != null ? columns : TableColumn.values();
    }

    public RepClubTeam() {
    }

    private final static RepClubTeam teamDb = new RepClubTeam();
}
