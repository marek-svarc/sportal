package com.clubeek.dao.impl.ownframework.rep;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.clubeek.db.Admin;
import com.clubeek.db.Repository;
import com.clubeek.db.Admin.ColumnData;
import com.clubeek.model.ClubMember;
import com.clubeek.model.ModelTools;
import com.clubeek.model.TeamMember;

/**
 * Trida poskytujici pristup do databazove tabulky "team_member", ktera obsahuje
 * cleny klubu rozdelene do tymu
 *
 * @author Marek Svarc
 *
 */
public class RepTeamMember implements Repository<TeamMember> {

    /** Nazev tabulky */
    public static final String tableName = "t_team_member";

    /** Identifikatory sloupcu tabulky */
    public static enum TableColumn {

        ID("id"),
        FUNCTIONS("functions"),
        CLUB_MEMBER_ID("club_member_id"),
        CLUB_TEAM_ID("club_team_id");

        private TableColumn(String dbColumnName) {
            this.name = dbColumnName;
        }

        @Override
        public String toString() {
            return name;
        }

        public final String name;
    }

    public static RepTeamMember getInstance() {
        return teamMemberDb;
    }

    // SQL Insert
    /**
     * Vlozi a inicializuje radek v tabulce "team_member"
     *
     * @param teamMember data jednoho clena tymu, ktera budou vlozena do
     * databaze
     */
    public static void insert(TeamMember teamMember) {
        insert(teamMember.getFunctions(), teamMember.getClubMemberId(), teamMember.getClubTeamId());
    }

    /**
     * Vlozi a inicializuje radek v tabulce "team_member"
     *
     * @param functions bitovy priznak asociovanych funkci v tymu
     * @param clubMemberId identifikator asociovaneho clena klubu
     * @param clubTeamId identifikator asociovaneho tymu
     */
    public static void insert(int functions, int clubMemberId, int clubTeamId) {
        // sestaveni sql prikazu
        String sql = String.format("INSERT INTO %s (%s, %s, %s) VALUES ( ?, ?, ?)", tableName, TableColumn.FUNCTIONS.name,
                TableColumn.CLUB_MEMBER_ID.name, TableColumn.CLUB_TEAM_ID.name);
        // provedeni transakce
        Admin.update(sql,
                new ColumnData[]{new ColumnData(functions), new ColumnData(clubMemberId), new ColumnData(clubTeamId)});
    }

    // SQL Update
    /**
     * Modifikuje radek tabulky "team_member"
     *
     * @param teamMember data jednoho clena tymu, ktera budou vlozena do
     * databaze
     */
    public static void update(TeamMember teamMember) {
        update(teamMember.getId(), teamMember.getFunctions(), teamMember.getClubMemberId(), teamMember.getClubTeamId());
    }

    /**
     * Modifikuje radek tabulky "team_member"
     *
     * @param id unikatni identifikator radky tabulky
     * @param functions bytovy priznak asociovanych funkci v tymu
     * @param clubMemberId identifikator asociovaneho clena klubu
     * @param clubTeamId identifikator asociovaneho tymu
     */
    public static void update(int id, int functions, int clubMemberId, int clubTeamId) {
        // sestaveni sql prikazu
        String sql = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ? WHERE %s = %s", tableName, TableColumn.FUNCTIONS.name,
                TableColumn.CLUB_MEMBER_ID.name, TableColumn.CLUB_TEAM_ID.name, TableColumn.ID.name, Integer.toString(id));
        // provedeni transakce
        Admin.update(sql,
                new ColumnData[]{new ColumnData(functions), new ColumnData(clubMemberId), new ColumnData(clubTeamId)});
    }

    /**
     * Vicenasobna uprava clenu tymu (dle rozdilu v seznamu se volaji metody
     * insert, update a delete)
     *
     * @param oldTeamMembers puvodni seznam clenu
     * @param newTeamMembers novy seznam clenu
     */
    public static void update(List<TeamMember> oldTeamMembers, List<TeamMember> newTeamMembers) {
        Admin.synchronize(oldTeamMembers, newTeamMembers, getInstance());
    }

    // SQL delete
    public static void delete(int id) {
        Admin.delete(tableName, TableColumn.ID.name, id);
    }

    // SQL Select
    
    /**
     * Vraci clena klubu dle unikatniho identifikatoru
     *
     * @return data clena klubu
     *
     */
    public static TeamMember selectById(int id, TableColumn[] columns) {
        columns = getColumns(columns);
        List<TeamMember> teamMemberList = Admin.query(TeamMember.class, String.format("SELECT %s FROM %s WHERE %s = %d",
                Admin.createSelectParams(columns), tableName, TableColumn.ID, id), columns, getInstance());
        return (teamMemberList != null) && (teamMemberList.size() == 1) ? teamMemberList.get(0) : null;
    }
    
    /**
     * Vraci seznam clenu jednoho tymu asociovanych se clenem klubu
     *
     * @return seznam clenu tymu
     */
    public static List<TeamMember> selectByTeamId(int clubTeamId, TableColumn[] columns) {
        columns = getColumns(columns);
        return Admin.query(TeamMember.class, String.format("SELECT %s FROM %s WHERE %s = %d", Admin.createSelectParams(columns),
                tableName, TableColumn.CLUB_TEAM_ID, clubTeamId), columns, getInstance());
    }

    /**
     * Vraci seznam tymu, do kterych patri jeden clen klubu
     *
     * @param clubMemberId unikatni identifikator clena klubu
     * @param columns definice sloupcu tabulky, ktere se maji nacist z databaze
     * @return seznam clenu
     */
    public static List<TeamMember> selectByClubMemberId(int clubMemberId, TableColumn[] columns) {
        columns = getColumns(columns);
        return Admin.query(TeamMember.class, String.format("SELECT %s FROM %s WHERE %s = %d", Admin.createSelectParams(columns),
                tableName, TableColumn.CLUB_MEMBER_ID, clubMemberId), columns, getInstance());
    }

    /**
     * Vraci seznam clenu dle seznamu clenu klubu. Pokud je clen klubu i clenem
     * tymu nactou se data clena tymu z databaze. Pokud clen klubu neni clenem
     * tymu, vytori se nova instance clena tymu.
     *
     * @return seznam clenu tymu
     */
    public static List<TeamMember> selectOrCreateByClubMembers(int clubTeamId, List<ClubMember> clubMembers, TableColumn[] columns) {
        List<TeamMember> teamMembers = new ArrayList<TeamMember>();
        if (clubMembers.size() > 0) {
            // vyber clenu tymu, kterym odpovida nejaky zaznam v seznamu clenu
            // klubu
            columns = getColumns(columns);
            teamMembers.addAll(Admin.query(TeamMember.class, String.format("SELECT %s FROM %s WHERE (%s = %d) AND (%s IN (%s))",
                    Admin.createSelectParams(columns), tableName, TableColumn.CLUB_TEAM_ID, clubTeamId,
                    TableColumn.CLUB_MEMBER_ID.name, ModelTools.listToString(clubMembers)), columns, getInstance()));
            // vytvoreni novych clenu tymu
            int index, clubMemberId;
            for (int i = 0; i < clubMembers.size(); ++i) {
                clubMemberId = clubMembers.get(i).getId();

                index = -1;
                for (int j = 0; j < teamMembers.size(); ++j) {
                    if (teamMembers.get(j).getClubMemberId() == clubMemberId) {
                        index = j;
                        break;
                    }
                }

                if (index < 0) {
                    teamMembers.add(new TeamMember(clubTeamId, clubMemberId));
                }
            }
        }
        return teamMembers;
    }

    // Implementace Rozhrani Globals.SqlExtension
    @Override
    public void insertRow(TeamMember value) {
        insert(value);
    }

    @Override
    public void updateRow(TeamMember value) {
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
    public void readValue(ResultSet result, int resultsColumnId, TeamMember data, Object dataColumnId) {
        try {
            switch ((RepTeamMember.TableColumn) dataColumnId) {
                case ID:
                    data.setId(result.getInt(resultsColumnId));
                    break;
                case FUNCTIONS:
                    data.setFunctions(result.getInt(resultsColumnId));
                    break;
                case CLUB_MEMBER_ID:
                    data.setClubMemberId(result.getInt(resultsColumnId));
                    break;
                case CLUB_TEAM_ID:
                    data.setClubTeamId(result.getInt(resultsColumnId));
                    break;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* PRIVATE */
    private static RepTeamMember.TableColumn[] getColumns(RepTeamMember.TableColumn[] columns) {
        return columns != null ? columns : TableColumn.values();
    }

    public RepTeamMember() {
    }

    private final static RepTeamMember teamMemberDb = new RepTeamMember();
}
