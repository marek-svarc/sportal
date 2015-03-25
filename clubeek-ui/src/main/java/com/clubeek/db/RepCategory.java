package com.clubeek.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.clubeek.db.Admin.ColumnData;
import com.clubeek.model.Category;

public class RepCategory implements Repository<Category> {

    /** Nazev tabulky */
    public static final String tableName = "category";

    /** Identifikatory sloupcu tabulky */
    public static enum TableColumn {

        ID("id"),
        ACTIVE("active"),
        DESCRIPTION("description"),
        SORTING("sorting");

        private TableColumn(String dbColumnName) {
            this.name = dbColumnName;
        }

        @Override
        public String toString() {
            return name;
        }

        public final String name;
    }

    public static RepCategory getInstance() {
        return categoryDb;
    }

    // SQL insert
    /**
     * Vlozi a inicializuje radek v tabulce "Category"
     *
     * @param category data kategorie
     * @throws SQLException
     */
    public static void insert(Category category) throws SQLException {
        insert(category.getDescription(), category.getActive());
    }

    /**
     * Vlozi a inicializuje radek v tabulce "Category"
     *
     * @param description popis kategorie
     * @param active priznak zda je kategorie aktivni
     * @param priority priorita kategorie
     * @throws SQLException
     */
    public static void insert(String description, boolean active) throws SQLException {
        // sestaveni sql prikazu
        String sql = String.format("INSERT INTO %s (%s, %s) VALUES ( ? , ? )", tableName, TableColumn.DESCRIPTION.name,
                TableColumn.ACTIVE.name);
        // provedeni transakce
        int id = Admin.update(sql, new ColumnData[]{new ColumnData(description), new ColumnData(active)}, true);
        // inicializace hodnoty pro razeni
        update(id, id);
    }

    // SQL update
    /**
     * Modifikuje radek tabulky "Category"
     *
     * @param category data, ktera budou zapsana do databaze
     * @throws SQLException
     */
    public static void update(Category category) throws SQLException {
        update(category.getId(), category.getDescription(), category.getActive());
    }

    /**
     * Modifikuje radek tabulky "Category"
     *
     * @param id index modifikovane radky tabulky
     * @param description popis kategorie
     * @param active priznak zda je kategorie aktivni
     * @throws SQLException
     */
    public static void update(int id, String description, boolean active) throws SQLException {
        // sestaveni sql prikazu
        String sql = String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s = %d", tableName, TableColumn.DESCRIPTION.name,
                TableColumn.ACTIVE.name, TableColumn.ID.name, id);
        // provedeni transakce
        Admin.update(sql, new ColumnData[]{new ColumnData(description), new ColumnData(active)});
    }

    /**
     * Modifikuje hodnotu definujici defaultni razeni tabulky "Category"
     *
     * @param id index modifikovane radky tabulky
     * @param sorting hodnota definujci razeni
     * @throws SQLException
     */
    public static void update(int id, int sorting) throws SQLException {
        // sestaveni sql prikazu
        String sql = String.format("UPDATE %s SET %s = ? WHERE %s = %d", tableName, TableColumn.SORTING.name,
                TableColumn.ID.name, id);
        // provedeni transakce
        Admin.update(sql, new ColumnData[]{new ColumnData(sorting)});
    }

    /**
     * Prohodi hodnotu ve sloupci "sorting" ve dvou radkach tabulky "Category"
     *
     * @param idA index radky tabulky
     * @param idB index radky tabulky
     * @throws SQLException
     */
    public static void exchange(int idA, int idB) throws SQLException {
        Category categoryA = selectById(idA, null);
        Category categoryB = selectById(idB, null);
        if ((categoryA != null) && (categoryB != null)) {
            update(idA, categoryB.getSorting());
            update(idB, categoryA.getSorting());
        }
    }

    // DML delete
    public static void delete(int id) throws SQLException {
        Admin.delete(tableName, TableColumn.ID.name, id);
    }

    // SQL select
    /**
     * Vrati vsechny radky tabulky "Category", ale pouze vybrane sloupce
     *
     * @param columns seznam pozadovanych sloupcu
     * @return seznam vsech radek tabulky
     * @throws SQLException
     */
    public static List<Category> selectAll(TableColumn[] columns) throws SQLException {
        columns = getColumns(columns);
        return Admin.query(Category.class, String.format("SELECT %s FROM %s ORDER BY %s ASC", Admin.createSelectParams(columns),
                tableName, TableColumn.SORTING), columns, getInstance());
    }

    /**
     * Vraci radku tabulky dle indexu radky
     *
     * @param id index radku v tabolce "category"
     * @param columns seznam sloupcu, ktere je potreba nacist z databaze (pokud
     * je null, uvazuji se vsechny sloupce)
     * @return vraci kategorii, ktera ma index id
     * @throws SQLException
     */
    public static Category selectById(int id, TableColumn[] columns) throws SQLException {
        columns = getColumns(columns);
        List<Category> categoryList = Admin.query(Category.class, String.format("SELECT %s FROM %s WHERE %s = %s",
                Admin.createSelectParams(columns), tableName, TableColumn.ID, Integer.toString(id)), columns, getInstance());
        return (categoryList != null) && (categoryList.size() == 1) ? categoryList.get(0) : null;
    }

    /**
     * Vraci radky dle aktivity kategorie
     *
     * @return seznam vsech radek tabulky
     * @throws SQLException
     */
    public static List<Category> select(boolean active, TableColumn[] columns) throws SQLException {
        columns = getColumns(columns);
        return Admin.query(Category.class, String.format("SELECT %s FROM %s WHERE %s = %s ORDER BY %s ASC",
                Admin.createSelectParams(columns), tableName, TableColumn.ACTIVE, active ? 1 : 0, TableColumn.SORTING.name),
                columns, getInstance());
    }

    // Rozhrani Globals.SqlReadable<Category>
    @Override
    public void updateRow(Category value) throws SQLException {
        RepCategory.update(value);
    }

    @Override
    public void insertRow(Category value) throws SQLException {
        RepCategory.insert(value);
    }

    @Override
    public void deleteRow(int id) throws SQLException {
        RepCategory.delete(id);
    }

    @Override
    public void exchangeRows(int idA, int idB) throws SQLException {
        RepCategory.exchange(idA, idB);
    }

    @Override
    public void readValue(ResultSet result, int resultsColumnId, Category data, Object dataColumnId) throws SQLException {
        switch ((RepCategory.TableColumn) dataColumnId) {
            case ID:
                data.setId(result.getInt(resultsColumnId));
                break;
            case DESCRIPTION:
                data.setDescription(result.getString(resultsColumnId));
                break;
            case ACTIVE:
                data.setActive(result.getBoolean(resultsColumnId));
                break;
            case SORTING:
                data.setSorting(result.getInt(resultsColumnId));
                break;
        }
    }

    /* PRIVATE */
    private static RepCategory.TableColumn[] getColumns(RepCategory.TableColumn[] columns) {
        return columns != null ? columns : TableColumn.values();
    }

    public RepCategory() {
    }

    private final static RepCategory categoryDb = new RepCategory();

}
