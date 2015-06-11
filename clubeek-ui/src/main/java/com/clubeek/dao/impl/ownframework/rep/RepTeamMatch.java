package com.clubeek.dao.impl.ownframework.rep;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

import com.clubeek.db.Admin;
import com.clubeek.db.Repository;
import com.clubeek.db.Admin.ColumnData;
import com.clubeek.model.TeamMatch;

import java.sql.SQLException;

public class RepTeamMatch implements Repository<TeamMatch> {

    /** Nazev tabulky */
    public static final String tableName = "t_team_match";

    /** Identifikatory sloupcu tabulky */
    public enum TableColumn {

        ID("id"),
        START("start"),
        SCORE_POS("score_pos"),
        SCORE_NEG("score_neg"),
        HOME_COURT("home_court"),
        COMMENT("comment"),
        PUBLISH("publish"),
        CLUB_TEAM_ID("club_team_id"),
        CLUB_RIVAL_ID("club_rival_id"),
        CLUB_RIVAL_COMMENT("club_rival_comment");

        private TableColumn(String dbColumnName) {
            this.name = dbColumnName;
        }

        @Override
        public String toString() {
            return name;
        }

        public final String name;
    }

    public static RepTeamMatch getInstance() {
        return gameDb;
    }

    // DML Insert
    /**
     * Vlozi a inicializuje radek v tabulce "game".
     *
     */
    public static int insert(TeamMatch match) {
        return insert(match.getStart(), match.getClubTeamId(), match.getClubRivalId(), match.getClubRivalComment(),
                match.getHomeCourt(), match.getScorePos(), match.getScoreNeg(), match.getPublish(), match.getComment());
    }

    /**
     * Vlozi a inicializuje radek v tabulce "game".
     *
     */
    public static int insert(Date start, int clubTeamId, int clubRivalId, String clubRivalComment, boolean homeCourt,
            int scorePos, int scoreNeg, boolean publish, String comment) {
        // sestaveni sql prikazu
        String sql = String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ? )",
                tableName, TableColumn.START.name, TableColumn.CLUB_TEAM_ID.name, TableColumn.CLUB_RIVAL_ID.name,
                TableColumn.CLUB_RIVAL_COMMENT.name, TableColumn.HOME_COURT.name, TableColumn.SCORE_POS.name,
                TableColumn.SCORE_NEG.name, TableColumn.PUBLISH.name, TableColumn.COMMENT.name);
        // provedeni transakce
        return Admin.update(sql, new ColumnData[]{new ColumnData(start, false), new ColumnData(clubTeamId),
            new ColumnData(clubRivalId), new ColumnData(clubRivalComment), new ColumnData(homeCourt),
            new ColumnData(scorePos), new ColumnData(scoreNeg), new ColumnData(publish), new ColumnData(comment)}, true);
    }

    // DML Update
    /**
     * Modifikuje radek v tabulce "game"
     *
     */
    public static void update(TeamMatch match) {
        update(match.getId(), match.getStart(), match.getClubTeamId(), match.getClubRivalId(), match.getClubRivalComment(),
                match.getHomeCourt(), match.getScorePos(), match.getScoreNeg(), match.getPublish(), match.getComment());
    }

    /**
     * Modifikuje radek v tabulce "game"
     *
     */
    public static void update(int id, Date start, int clubTeamId, int clubRivalId, String clubRivalComment, boolean homeCourt,
            int scorePos, int scoreNeg, boolean publish, String comment) {
        // sestaveni sql prikazu
        String sql = String.format(
                "UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = %d", tableName,
                TableColumn.START.name, TableColumn.CLUB_TEAM_ID.name, TableColumn.CLUB_RIVAL_ID.name,
                TableColumn.CLUB_RIVAL_COMMENT.name, TableColumn.HOME_COURT.name, TableColumn.SCORE_POS.name,
                TableColumn.SCORE_NEG.name, TableColumn.PUBLISH.name, TableColumn.COMMENT.name, TableColumn.ID.name, id);
        // provedeni transakce
        Admin.update(sql, new ColumnData[]{new ColumnData(start, false), new ColumnData(clubTeamId),
            new ColumnData(clubRivalId), new ColumnData(clubRivalComment), new ColumnData(homeCourt),
            new ColumnData(scorePos), new ColumnData(scoreNeg), new ColumnData(publish), new ColumnData(comment)});
    }

    // DML delete
    /**
     * Maze radek urceny primarnim klicem v tabulce "game"
     *
     */
    public static void delete(int id) {
        Admin.delete(tableName, TableColumn.ID.name, id);
    }

    // SQL Select
    /**
     * Vraci zapas z tabulky "game" dle identifikatoru zapasu
     *
     * @param id unikatni identifikator zapasu
     */
    public static TeamMatch selectById(int id, TableColumn[] columns) {
        columns = getColumns(columns);
        List<TeamMatch> gameList = Admin.query(TeamMatch.class, String.format("SELECT %s FROM %s WHERE %s = %d ORDER BY %s DESC",
                Admin.createSelectParams(columns), tableName, TableColumn.ID.name, id, TableColumn.START), columns, RepTeamMatch.getInstance());
        return (gameList != null) && (gameList.size() == 1) ? gameList.get(0) : null;

    }

    /**
     * Vraci seznam zapasu z tabulky "game" dle identifikatoru tymu
     *
     * @param clubTeamId unikatni identifikator tymu
     */
    public static List<TeamMatch> selectByTeamId(int clubTeamId, TableColumn[] columns) {
        columns = getColumns(columns);
        return Admin.query(TeamMatch.class, String.format("SELECT %s FROM %s WHERE %s = %d ORDER BY %s DESC", Admin.createSelectParams(columns),
                tableName, TableColumn.CLUB_TEAM_ID.name, clubTeamId, TableColumn.START), columns, RepTeamMatch.getInstance());
    }

    /**
     * Vraci odehrane a publikovatelne zapasy z tabulky "game"
     *
     * @param clubTeamId unikatni identifikator tymu
     */
    public static List<TeamMatch> selectPublishable(int clubTeamId, TableColumn[] columns) {
        columns = getColumns(columns);
        return Admin.query(TeamMatch.class, String.format("SELECT %s FROM %s WHERE (%s = %d) AND (%s = true) AND (LOCALTIMESTAMP >= %s) ORDER BY %s DESC",
                Admin.createSelectParams(columns), tableName, TableColumn.CLUB_TEAM_ID, clubTeamId, TableColumn.PUBLISH,
                TableColumn.START, TableColumn.START), columns, RepTeamMatch.getInstance());
    }

    /**
     * Vraci nejblizsi neodehrane domaci zapasy
     *
     * @param months pocet mesicu pro vyhledavani
     */
    public static List<TeamMatch> selectHomeMatches(int months, TableColumn[] columns) {
        columns = getColumns(columns);
        return Admin.query(TeamMatch.class, String.format(
                "SELECT %s FROM %s WHERE (%s = true) AND (%s > LOCALTIMESTAMP) AND (%s <= (LOCALTIMESTAMP + INTERVAL '%d MONTH')) ORDER BY %s",
                Admin.createSelectParams(columns), tableName, TableColumn.HOME_COURT, TableColumn.START,
                TableColumn.START, months, TableColumn.START), columns, RepTeamMatch.getInstance());
    }

    // implementation of Repository<Game>
    @Override
    public void updateRow(TeamMatch value) {
        update(value);
    }

    @Override
    public void insertRow(TeamMatch value) {
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
    public void readValue(ResultSet result, int resultsColumnId, TeamMatch data, Object dataColumnId) {
        try {
            switch ((RepTeamMatch.TableColumn) dataColumnId) {
                case ID:
                    data.setId(result.getInt(resultsColumnId));
                    break;
                case START:
                    data.setStart(new Date(result.getTimestamp(resultsColumnId).getTime()));
                    break;
                case SCORE_POS:
                    data.setScorePos(result.getInt(resultsColumnId));
                    break;
                case SCORE_NEG:
                    data.setScoreNeg(result.getInt(resultsColumnId));
                    break;
                case HOME_COURT:
                    data.setHomeCourt(result.getBoolean(resultsColumnId));
                    break;
                case PUBLISH:
                    data.setPublish(result.getBoolean(resultsColumnId));
                    break;
                case COMMENT:
                    data.setComment(result.getString(resultsColumnId));
                    break;
                case CLUB_RIVAL_COMMENT:
                    data.setClubRivalComment(result.getString(resultsColumnId));
                    break;
                case CLUB_RIVAL_ID:
                    data.setClubRivalId(result.getInt(resultsColumnId));
                    data.setClubRival(RepClubRival.selectById(data.getClubRivalId(), null));
                    break;
                case CLUB_TEAM_ID:
                    data.setClubTeamId(result.getInt(resultsColumnId));
                    data.setClubTeam(RepClubTeam.selectById(data.getClubTeamId(), null));
                    break;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* PRIVATE */
    private RepTeamMatch() {
    }

    private static RepTeamMatch.TableColumn[] getColumns(RepTeamMatch.TableColumn[] columns) {
        return columns != null ? columns : TableColumn.values();
    }

    private final static RepTeamMatch gameDb = new RepTeamMatch();

}
