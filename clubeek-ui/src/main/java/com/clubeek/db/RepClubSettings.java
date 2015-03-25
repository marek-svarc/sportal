package com.clubeek.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.clubeek.model.ClubSettings;

public class RepClubSettings implements Repository<ClubSettings> {

    /** Nazev tabulky */
    public static final String tableName = "club_settings";

    /** Identifikatory sloupcu tabulky */
    public static enum TableColumn {

        ID("id"), 
        TITLE("title"), 
        COMMENT("comment"), 
        LOGO("logo");

        private TableColumn(String dbColumnName) {
            this.name = dbColumnName;
        }

        @Override
        public String toString() {
            return name;
        }

        public final String name;
    }

    public static RepClubSettings getInstance() {
        return clubDb;
    }

    /**
     * Vraci nastaveni klubu z tabulky "club_settings"
     *
     * @return data nastaveni klubu
     * @throws SQLException
     */
    public static ClubSettings select(int id, TableColumn[] columns) throws SQLException {
        columns = getColumns(columns);
        List<ClubSettings> clubSettingsList = Admin.query(ClubSettings.class, String.format("SELECT %s FROM %s WHERE %s = %d",
                Admin.createSelectParams(columns), tableName, TableColumn.ID.name, 1), columns, getInstance());
        return (clubSettingsList != null) && (clubSettingsList.size() == 1) ? clubSettingsList.get(0) : null;
    }

    @Override
    public void updateRow(ClubSettings value) throws SQLException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void insertRow(ClubSettings value) throws SQLException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void deleteRow(int id) throws SQLException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void exchangeRows(int idA, int idB) throws SQLException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void readValue(ResultSet result, int resultsColumnId, ClubSettings data, Object dataColumnId) throws SQLException {
        switch ((RepClubSettings.TableColumn) dataColumnId) {
            case ID:
                data.setId(result.getInt(resultsColumnId));
                break;
            case TITLE:
                data.setTitle(result.getString(resultsColumnId));
                break;
            case COMMENT:
                data.setComment(result.getString(resultsColumnId));
                break;
            case LOGO:
                data.setLogo(result.getBytes(resultsColumnId));
                break;
        }
    }

    /* PRIVATE */
    private RepClubSettings() {
    }

    private static RepClubSettings.TableColumn[] getColumns(RepClubSettings.TableColumn[] columns) {
        return columns != null ? columns : TableColumn.values();
    }

    private final static RepClubSettings clubDb = new RepClubSettings();
}
