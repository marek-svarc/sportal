package com.clubeek.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.clubeek.db.Admin.ColumnData;
import com.clubeek.model.TeamMember;
import com.clubeek.model.User;
import com.clubeek.model.User.Role;

public class RepUser implements Repository<User> {

    /** Nazev tabulky */
	public static final String tableName = "user_data";

    /** Identifikatory sloupcu tabulky */
    public static enum TableColumn {

        ID("id"),
        NAME("name"),
        PASSWORD("password"),
        PERMISSIONS("permissions"),
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

    public static RepUser getInstance() {
        return userDb;
    }

    // DML insert
    /**
     * Vlozi a inicializuje radek v tabulce "User"
     *
     * @param user informace o uzivateli
     */
    public static void insert(User user) {
        insert(user.getName(), user.GetHashPassword(), user.getRole(), user.getClubMemberId());
    }

    /**
     * Vlozi a inicializuje radek v tabulce "User"
     *
     * @param name jmeno uzivatele
     * @param password heslo uzivatele
     * @param role uzivatelska role
     */
    public static void insert(String name, String password, Role role, int clubMemberId) {
        // sestaveni sql prikazu
        String sql = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES ( ? , ? , ? , ? )", tableName, TableColumn.NAME,
                TableColumn.PASSWORD, TableColumn.PERMISSIONS, TableColumn.CLUB_MEMBER_ID);
        // provedeni transakce
        Admin.update(sql, new ColumnData[]{new ColumnData(name), new ColumnData(password), new ColumnData(role.ordinal()),
            new ColumnData(clubMemberId > 0 ? clubMemberId : Integer.MIN_VALUE)});
    }

    // DML update
    /**
     * Modifikuje radek tabulky "User"
     *
     * @param user data uzivatele
     */
    public static void update(User user) {
        update(user.getId(), user.getRole(), user.GetHashPassword());
    }

    /**
     * Modifikuje radek tabulky "User"
     *
     * @param id unikatni identifikator uzivatele
     * @param role uzivatelska role
     * @param clubMemberId id clena klubu asociovaneho s uzivatelem
     */
    public static void update(int id, Role role, String password) {
        if (password != null) {
            // sestaveni sql prikazu
            String sql = String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s = %d", tableName, TableColumn.PERMISSIONS,
                    TableColumn.PASSWORD, TableColumn.ID, id);
            // provedeni transakce
            Admin.update(sql, new ColumnData[]{new ColumnData(role.ordinal()), new ColumnData(password)});
        } else {
            // sestaveni sql prikazu
            String sql = String.format("UPDATE %s SET %s = ? WHERE %s = %d", tableName, TableColumn.PERMISSIONS, TableColumn.ID,
                    id);
            // provedeni transakce
            Admin.update(sql, new ColumnData[]{new ColumnData(role.ordinal())});
        }
    }

    // DML delete
    public static void delete(int id) {
        Admin.delete(tableName, TableColumn.ID.name, id);
    }

    // SQL Select
    /**
     * Nacte data uzivatele identifikovaneho uzivatelskym jmenem
     *
     * @param name uzivatelske jmeno
     * @param columns seznam pozadovanych sloupcu tabulky
     */
    public static User selectByName(String name, boolean fillDependencies, TableColumn[] columns) {
        columns = getColumns(columns);
        List<User> userList = Admin.query(User.class, String.format("SELECT %s FROM %s WHERE lower(%s) = lower('%s')",
                Admin.createSelectParams(columns), tableName, TableColumn.NAME, name), columns, getInstance());
        User user = (userList != null) && (userList.size() == 1) ? userList.get(0) : null;

        if ((user != null) && fillDependencies) {
            // nacteni dat uzivatele
            if (user.getClubMemberId() > 0) {
                user.setClubMember(RepClubMember.selectById(user.getClubMemberId(), null));
                // nacteni asociovanych tymu
                List<TeamMember> teams = RepTeamMember.selectByClubMemberId(user.getClubMemberId(),
                        new RepTeamMember.TableColumn[]{RepTeamMember.TableColumn.CLUB_TEAM_ID});
                if ((teams != null) && (teams.size() > 0)) {
                    int[] teamsId = new int[teams.size()];
                    for (int i = 0; i < teams.size(); ++i) {
                        teamsId[i] = teams.get(i).getClubTeamId();
                    }
                    user.setTeams(teamsId);
                } else {
                    user.setTeams(null);
                }

            }
        }
        return user;
    }

    /**
     * Nacte data uzivatele identifikovaneho clenstvim v klubu
     *
     * @param clubMemberId identifikator clena klubu
     * @param columns seznam pozadovanych sloupcu tabulky
     */
    public static User selectByClubMemberId(int clubMemberId, TableColumn[] columns) {
        columns = getColumns(columns);
        List<User> userList = Admin.query(User.class, String.format("SELECT %s FROM %s WHERE %s = %s",
                Admin.createSelectParams(columns), tableName, TableColumn.CLUB_MEMBER_ID, Integer.toString(clubMemberId)),
                columns, getInstance());
        return (userList != null) && (userList.size() == 1) ? userList.get(0) : null;
    }

    /** Vrati vsechny uzivatele s pravy administratiora */
    public static List<User> selectAllAdministrators(TableColumn[] columns) {
        columns = getColumns(columns);
        return Admin.query(User.class, String.format("SELECT %s FROM %s WHERE %s = %s", Admin.createSelectParams(columns),
                tableName, TableColumn.PERMISSIONS, User.Role.ADMINISTRATOR.ordinal()), columns, getInstance());
    }

    /**
     * Vrati vsechny radky tabulky "User"
     *
     * @param columns Pole pozadovanych sloupcu tabulky
     */
    public static List<User> selectAll(TableColumn[] columns) {
        columns = getColumns(columns);
        return Admin.query(User.class, String.format("SELECT %s FROM %s", Admin.createSelectParams(columns), tableName), columns,
                getInstance());
    }

    // interface Repository<User>
    @Override
    public void updateRow(User value) {
        update(value);
    }

    @Override
    public void insertRow(User value) {
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
    public void readValue(ResultSet result, int resultsColumnId, User data, Object dataColumnId) {
        try {
            switch ((RepUser.TableColumn) dataColumnId) {
                case ID:
                    data.setId(result.getInt(resultsColumnId));
                    break;
                case NAME:
                    data.setName(result.getString(resultsColumnId));
                    break;
                case PASSWORD:
                    data.setPassword(result.getString(resultsColumnId));
                    break;
                case PERMISSIONS:
                    data.setRole(Role.values()[result.getInt(resultsColumnId)]);
                    break;
                case CLUB_MEMBER_ID:
                    data.setClubMemberId(result.getInt(resultsColumnId));
                    break;
            }
        } catch (SQLException e) {
            throw  new RuntimeException(e);
        }
    }

    /* PRIVATE */
    private static RepUser.TableColumn[] getColumns(RepUser.TableColumn[] columns) {
        return columns != null ? columns : TableColumn.values();
    }

    private RepUser() {
    }

    private final static RepUser userDb = new RepUser();
}
