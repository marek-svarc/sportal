package com.clubeek.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.clubeek.db.Admin.ColumnData;
import com.clubeek.model.Article;
import com.clubeek.model.Article.Location;
import com.clubeek.model.Article.Owner;

public class RepArticle implements Repository<Article> {

    /** Nazev tabulky */
    public static final String tableName = "article";

    /** Identifikatory sloupcu tabulky */
    public static enum TableColumn {

        ID("id"),
        LOCATION("location"),
        PRIORITY("priority"),
        CAPTION("caption"),
        SUMMARY("summary"),
        CONTENT("content"),
        CREATION_DATE("creation_date"),
        EXPIRATION_DATE("expiration_date"),
        OWNER_TYPE("owner_type"),
        CLUB_TEAM_ID("club_team_id"),
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

    public static RepArticle getInstance() {
        return articleDb;
    }

    // SQL insert
    /**
     * Vlozi a inicializuje radek v tabulce "Article"
     *
     * @param article data publikovaneho clanku
     *
     */
    public static void insert(Article article) {
        insert(article.getLocation(), article.getPriority(), article.getCaption(), article.getSummary(), article.getContent(),
                article.getCreationDate(), article.getExpirationDate(), article.getOwner(), article.getClubTeamId(),
                article.getCategoryId());
    }

    /**
     * Vlozi a inicializuje radek v tabulce "Article"
     *
     * @param type Umisteni clanku
     * @param priority Priznak zda ma clanek vysokou prioritu
     * @param caption Nadpis clanku
     * @param content Obsah clanku
     * @param creationDate Datum vytvoreni/posledni zmeny clanku
     * @param expirationDate Datum znepristupneni clanku
     */
    public static void insert(Article.Location location, boolean priority, String caption, String summary, String content,
            Date creationDate, Date expirationDate, Owner owner, int clubTeamId, int categoryId) {
        // sestaveni sql prikazu
        String sql = String.format(
                "INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s) VALUES ( ? , ? ,? ,? , ? , ?, ?, ?, ?, ?)", tableName,
                TableColumn.LOCATION, TableColumn.PRIORITY, TableColumn.CAPTION, TableColumn.SUMMARY, TableColumn.CONTENT,
                TableColumn.CREATION_DATE, TableColumn.EXPIRATION_DATE, TableColumn.OWNER_TYPE, TableColumn.CLUB_TEAM_ID,
                TableColumn.CATEGORY_ID);
        // provedeni transakce
        Admin.update(sql, new ColumnData[]{new ColumnData(location.ordinal()), new ColumnData(priority),
            new ColumnData(caption), new ColumnData(summary), new ColumnData(content), new ColumnData(creationDate, false),
            new ColumnData(expirationDate, true), new ColumnData(owner.ordinal()),
            new ColumnData(clubTeamId > 0 ? clubTeamId : Integer.MIN_VALUE),
            new ColumnData(categoryId > 0 ? categoryId : Integer.MIN_VALUE)});
    }

    // SQL update
    /**
     * Modifikuje radek tabulky "Article"
     *
     * @param article data, ktera budou zapsana do databaze
     */
    public static void update(Article article) {
        update(article.getId(), article.getLocation(), article.getPriority(), article.getCaption(), article.getSummary(),
                article.getContent(), article.getCreationDate(), article.getExpirationDate(), article.getOwner(),
                article.getClubTeamId(), article.getCategoryId());
    }

    /**
     * Modifikuje radek tabulky "Article"
     *
     * @param id identifikator modifikovane radky tbulky
     * @param type Umisteni clanku
     * @param priority Uroven priority clanku
     * @param caption Nadpis clanku
     * @param content Obsah clanku
     * @param creationDate Datum vytvoreni/posledn� zm�ny clanku
     * @param expirationDate Datum znepristupneni clanku
     *
     */
    public static void update(int id, Article.Location location, boolean priority, String caption, String summary,
            String content, Date creationDate, Date expirationDate, Owner owner, int clubTeamId, int categoryId) {
        // sestaveni sql prikazu
        String sql = String
                .format("UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = %d",
                        tableName, TableColumn.LOCATION, TableColumn.PRIORITY, TableColumn.CAPTION, TableColumn.SUMMARY,
                        TableColumn.CONTENT, TableColumn.CREATION_DATE, TableColumn.EXPIRATION_DATE, TableColumn.OWNER_TYPE,
                        TableColumn.CLUB_TEAM_ID, TableColumn.CATEGORY_ID, TableColumn.ID, id);
        // provedeni transakce
        Admin.update(sql, new ColumnData[]{new ColumnData(location.ordinal()), new ColumnData(priority),
            new ColumnData(caption), new ColumnData(summary), new ColumnData(content), new ColumnData(creationDate, false),
            new ColumnData(expirationDate, true), new ColumnData(owner.ordinal()),
            new ColumnData(clubTeamId > 0 ? clubTeamId : Integer.MIN_VALUE),
            new ColumnData(categoryId > 0 ? categoryId : Integer.MIN_VALUE)});
    }

    // DML delete
    public static void delete(int id) {
        Admin.delete(tableName, TableColumn.ID.name, id);
    }

    // SQL select
    /**
     * Vrati vsechny radky tabulky "Article", ale pouze vybran� sloupce
     *
     * @param columns seznam pozadovanych sloupcu
     * @return seznam v�ech radek tabulky
     *
     */
    public static List<Article> select(int clubTeamId, int categoryId, Location location, TableColumn[] columns) {
        columns = getColumns(columns);
        return Admin.query(Article.class, String.format("SELECT %s FROM %s WHERE %s AND %s AND %s ORDER BY %s DESC, %s DESC",
                Admin.createSelectParams(columns), tableName, sqlOwnerCondition(clubTeamId, categoryId),
                sqlLocationCondition(location), sqlExpiredDateCondition(), TableColumn.PRIORITY, TableColumn.CREATION_DATE),
                columns, getInstance());
    }

    /**
     * Vrati vsechny radky tabulky "Article", ale pouze vybran� sloupce
     *
     * @param columns seznam pozadovanych sloupcu
     * @return seznam v�ech radek tabulky
     *
     */
    public static List<Article> selectAll(TableColumn[] columns) {
        columns = getColumns(columns);
        return Admin.query(Article.class, String.format("SELECT %s FROM %s ORDER BY %s DESC, %s DESC",
                Admin.createSelectParams(columns), tableName, TableColumn.PRIORITY, TableColumn.CREATION_DATE), columns,
                getInstance());
    }

    /**
     * Vraci radku tabulky dle indexu rdky
     *
     * @param id index radku v tabulce "category"
     * @param columns seznam sloupcu, ktere je potreba nacist z databaze (pokud
     * je null, uvazuji se vsechny sloupce)
     * @return vraci kategorii, ktere odpovida index id
     *
     */
    public static Article selectById(int id, TableColumn[] columns) {
        columns = getColumns(columns);
        List<Article> articleList = Admin.query(Article.class, String.format("SELECT %s FROM %s WHERE %s = %s",
                Admin.createSelectParams(columns), tableName, TableColumn.ID, Integer.toString(id)), columns, getInstance());
        return (articleList != null) && (articleList.size() == 1) ? articleList.get(0) : null;
    }

    // Rozhrani Repository<Article>
    @Override
    public void insertRow(Article value) {
        RepArticle.insert(value);
    }

    @Override
    public void updateRow(Article value) {
        RepArticle.update(value);
    }

    @Override
    public void deleteRow(int id) {
        RepArticle.delete(id);
    }

    @Override
    public void exchangeRows(int idA, int idB) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void readValue(ResultSet result, int resultsColumnId, Article data, Object dataColumnId) {
        try {
            switch ((RepArticle.TableColumn) dataColumnId) {
                case ID:
                    data.setId(result.getInt(resultsColumnId));
                    break;
                case LOCATION:
                    data.setLocation(Article.Location.values()[result.getInt(resultsColumnId)]);
                    break;
                case PRIORITY:
                    data.setPriority(result.getBoolean(resultsColumnId));
                    break;
                case CAPTION:
                    data.setCaption(result.getString(resultsColumnId));
                    break;
                case SUMMARY:
                    data.setSummary(result.getString(resultsColumnId));
                    break;
                case CONTENT:
                    data.setContent(result.getString(resultsColumnId));
                    break;
                case CREATION_DATE:
                    data.setCreationDate(result.getTimestamp(resultsColumnId));
                    break;
                case EXPIRATION_DATE:
                    data.setExpirationDate(result.getDate(resultsColumnId));
                    break;
                case OWNER_TYPE:
                    data.setOwner(Owner.values()[result.getInt(resultsColumnId)]);
                    break;
                case CLUB_TEAM_ID:
                    data.setClubTeamId(result.getInt(resultsColumnId));
                    break;
                case CATEGORY_ID:
                    data.setCategoryId(result.getInt(resultsColumnId));
                    break;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* PRIVATE */
    /** Podminka pro vyber clanku dle umisteni na jedne strance */
    private static String sqlLocationCondition(Location location) {
        return String.format("(%s = %d)", TableColumn.LOCATION, location.ordinal());
    }

    /** Podminka pro vyber clanku dle umisteni na webu */
    private static String sqlOwnerCondition(int clubTeamId, int categoryId) {
        if ((clubTeamId > 0) || (categoryId > 0)) {
            return String.format("((%s = %d) OR (%s = %d) OR (%s = %d))", TableColumn.CLUB_TEAM_ID, clubTeamId,
                    TableColumn.CATEGORY_ID, categoryId, TableColumn.OWNER_TYPE, Owner.CLUB_ALL.ordinal());
        } else {
            return String.format("((%s = %d) OR (%s = %d))", TableColumn.OWNER_TYPE, Owner.CLUB_ALL.ordinal(),
                    TableColumn.OWNER_TYPE, Owner.CLUB.ordinal());
        }
    }

    /** Podminka pro vyber clanku dle datumu vyprseni platnosti */
    private static String sqlExpiredDateCondition() {
        return String.format("((%s is null) OR (SYSDATE() <= %s))", TableColumn.EXPIRATION_DATE, TableColumn.EXPIRATION_DATE);
    }

    private static RepArticle.TableColumn[] getColumns(RepArticle.TableColumn[] columns) {
        return columns != null ? columns : TableColumn.values();
    }

    private RepArticle() {
    }

    private final static RepArticle articleDb = new RepArticle();

}
