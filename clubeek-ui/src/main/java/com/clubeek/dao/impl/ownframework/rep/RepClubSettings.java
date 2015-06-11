package com.clubeek.dao.impl.ownframework.rep;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.clubeek.db.Admin;
import com.clubeek.db.Repository;
import com.clubeek.model.ClubSettings;

public class RepClubSettings implements Repository<ClubSettings> {

    /** Nazev tabulky */
    public static final String tableName = "t_club_setting";

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
     * Vraci nastaveni klubu z tabulky "t_club_setting"
     *
     * @return data nastaveni klubu
     *
     */
    public static ClubSettings select(int id, TableColumn[] columns) {
        columns = getColumns(columns);
        List<ClubSettings> clubSettingsList = Admin.query(ClubSettings.class, String.format("SELECT %s FROM %s WHERE %s = %d",
                Admin.createSelectParams(columns), tableName, TableColumn.ID.name, id), columns, getInstance());
        return (clubSettingsList != null) && (clubSettingsList.size() == 1) ? clubSettingsList.get(0) : null;
    }

    @Override
    public void updateRow(ClubSettings value) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void insertRow(ClubSettings value) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void deleteRow(int id) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void exchangeRows(int idA, int idB) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void readValue(ResultSet result, int resultsColumnId, ClubSettings data, Object dataColumnId) {
        try {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
