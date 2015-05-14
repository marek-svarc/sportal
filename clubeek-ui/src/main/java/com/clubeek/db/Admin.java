package com.clubeek.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.clubeek.model.ModelTools;
import com.clubeek.model.Unique;

public class Admin {

    /* PUBLIC */
    public static Connection getConnection() {
        try {
            Connection connection = instance.createConnection();
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends Unique> List<T> query(Class<T> dataClass, String sql, Object[] columns, Repository<T> reader) {
        List<T> valueList = new ArrayList<>();
        try (Connection connection = getConnection()) {

            ResultSet rslt = connection.createStatement().executeQuery(sql);

            while (rslt.next()) {
                T data = dataClass.newInstance();

                for (int i = 1; i <= columns.length; ++i) {
                    reader.readValue(rslt, i, data, columns[i - 1]);
                }

                valueList.add(data);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return valueList;
    }

    /**
     * Trida zapouzdrujici data a datove typy zapis/cteni z databaze
     */
    static public final class ColumnData {

        public final int type;

        public final Object data;

        public ColumnData(java.util.Date date, boolean onlyDate) {
            if (onlyDate) {
                this.type = java.sql.Types.DATE;
                this.data = date != null ? new java.sql.Date(date.getTime()) : null;
            } else {
                this.type = java.sql.Types.TIMESTAMP;
                this.data = date != null ? new Timestamp(date.getTime()) : null;
            }
        }

        public ColumnData(String text) {
            this.type = java.sql.Types.VARCHAR;
            this.data = text;
        }

        public ColumnData(int value) {
            this.type = java.sql.Types.INTEGER;
            this.data = value == Integer.MIN_VALUE ? null : value;
        }

        public ColumnData(byte[] value) {
            this.type = java.sql.Types.LONGVARBINARY;
            this.data = value;
        }

        public ColumnData(boolean value) {
            this.type = java.sql.Types.BOOLEAN;
            this.data = value;
        }
    }

    /**
     * Provede prikaz SQL formou transakce. K provedeni prikazu se pouziva trida
     * PreparedStatement
     *
     * @param sql pozadovany SQL prikaz
     * @param parameters parametry predavane do SQL v ramci prepareStatement
     * @returnLastId priznak zda ma funkce vracet posledni ID pouzite
     * automatickym cislovanim. Pokud je priznak false vraci se standardni
     * navratov hodnota SQL operace
     */
    public static int update(String sql, ColumnData[] parameters, boolean returnLastId) {
        int result = 0;
        try {
            try (Connection connection = getConnection()) {
                if (connection != null) {
                    // vypnuti automatickeho provedeni prikazu
                    connection.setAutoCommit(false);
                    // vytvoreni bodu obnovy
                    Savepoint save = connection.setSavepoint();
                    try {
                        // sestaveni sql prikazu
                        PreparedStatement statement = connection.prepareStatement(sql,
                                returnLastId ? PreparedStatement.RETURN_GENERATED_KEYS : PreparedStatement.NO_GENERATED_KEYS);
                        // nastaveni parametru
                        if (parameters != null) {
                            for (int i = 0; i < parameters.length; ++i) {
                                if (parameters[i].data != null) {
                                    switch (parameters[i].type) {
                                        case java.sql.Types.VARCHAR:
                                            statement.setString(i + 1, (String) parameters[i].data);
                                            break;
                                        case java.sql.Types.DATE:
                                            statement.setDate(i + 1, (java.sql.Date) parameters[i].data);
                                            break;
                                        case java.sql.Types.TIMESTAMP:
                                            statement.setTimestamp(i + 1, (java.sql.Timestamp) parameters[i].data);
                                            break;
                                        case java.sql.Types.INTEGER:
                                            statement.setInt(i + 1, (int) parameters[i].data);
                                            break;
                                        case java.sql.Types.BLOB:
                                            if (parameters[i].data != null) {
                                                statement.setBytes(i + 1, (byte[]) parameters[i].data);
                                            }
                                            break;
                                        case java.sql.Types.BOOLEAN:
                                            statement.setBoolean(i + 1, (boolean) parameters[i].data);
                                            break;
                                    }
                                } else {
                                    statement.setNull(i + 1, parameters[i].type);
                                }
                            }
                        }
                        // provedeni prikazu
                        result = statement.executeUpdate();

                        // zjisteni naposledy pouzite ID automatickym cislovanim
                        if (returnLastId) {
                            ResultSet keys = statement.getGeneratedKeys();
                            if ((keys != null) && (keys.next())) {
                                result = keys.getInt(1);
                            }
                        }

                        // dokonceni transakce
                        connection.commit();

                    } catch (SQLException e) {
                        connection.rollback(save);
                        throw e;
                    } finally {

                        connection.setAutoCommit(true);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * Provede prikaz SQL formou transakce. K provedeni prikazu se pouziva trida
     * PreparedStatement.
     *
     * @param sql pozadovany SQL p��kaz
     * @param parameters parametry predavane do SQL v ramci prepareStatement
     *
     * @throws SQLException
     */
    public static int update(String sql, ColumnData[] parameters) {
        return update(sql, parameters, false);
    }

    /**
     * Aktualizuje tabulku v databazi porovnanim puvodniho a noveho seznamu
     * hodnot
     *
     * @param oldValues seznam puvodnich kontaktu
     * @param newValues seznam novych kontaktu
     * @throws SQLException
     */
    public static <T extends Unique> void synchronize(List<T> oldValues, List<T> newValues, Repository<T> reader) {
        T value;

        // pridavani novych, uprava stavajicich
        for (T item : newValues) {
            value = oldValues != null ? ModelTools.listFindById(oldValues, item.getId()) : null;
            if (value == null) {
                reader.insertRow(item);
            } else {
                reader.updateRow(item);
            }
        }

        // mazani
        for (T item : oldValues) {
            if (ModelTools.listFindById(newValues, item.getId()) == null) {
                reader.deleteRow(item.getId());
            }
        }
    }

    /**
     * Odmaze radek z tabulky
     *
     * @param tableName nazev tabulky
     * @param idName nazev sloupce tabulky, ktery obsahuje identifikator
     * @param id index radky ktera ma byt vymazana z tabulky
     */
    public static void delete(String tableName, String idName, int id) {
        // sestaveni sql prikazu
        String sql = String.format("DELETE FROM %s WHERE %s = %s", tableName, idName, Integer.toString(id));
        // provedeni transakce
        Admin.update(sql, null);
    }

    public static String createSelectParams(Object[] columns) {
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < columns.length; ++i) {
            // text.append("'");
            text.append(columns[i].toString());
            // text.append("'");
            if (i < (columns.length - 1)) {
                text.append(", ");
            }
        }

        return text.toString();
    }

    /**
     * Sestavi retezec z jednotlivych nazvu sloupcu tabulky pro pouziti v sql
     * prikazu SELECT
     *
     * @param items pole nazvu sloupcu tabulky
     * @return retezec pro pouziti za prikaz SELECT
     */
    public static String createSelectQuery(Object[] items, String query) {
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < items.length; ++i) {
            // text.append("'");
            text.append(items[i].toString());
            // text.append("'");
            if (i < (items.length - 1)) {
                text.append(", ");
            }
        }

        return String.format("SELECT %s %s", text.toString(), query);
    }

    /* PRIVATE */
    /** Cesta ke konfiguracnimu souboru */
    private static final String CONFIG_NAME = "com.clubeek.db.config"; //$NON-NLS-1$

    /** Klic pro nacteni typu databazovaho ovladace z konfiguracniho souboru */
    private static final String CONFIG_KEY_DRIVER = "DATABASE_DRIVER"; //$NON-NLS-1$

    /** Klic pro nacteni URL databaze z konfiguracniho souboru */
    private static final String CONFIG_KEY_URL = "DATABASE_URL"; //$NON-NLS-1$

    /**
     * Klic pro nacteni uzivatelskeho jmena pro pristup do databaze z
     * konfiguracniho souboru
     */
    private static final String CONFIG_KEY_USER = "DATABASE_USER"; //$NON-NLS-1$

    /**
     * Klic pro nacteni uzivatelskeho hesla pro pristup do databaze z
     * konfiguracniho souboru
     */
    private static final String CONFIG_KEY_PASSWORD = "DATABASE_PASSWORD"; //$NON-NLS-1$

    /** Defaultni konstruktor */
    private Admin() {

        // implicitni ovladac databaze
        String driver = "com.mysql.jdbc.Driver";

        try {
            ResourceBundle config = ResourceBundle.getBundle(CONFIG_NAME);
            if (config.containsKey(CONFIG_KEY_DRIVER)) {
                driver = config.getString(CONFIG_KEY_DRIVER);
            }
            if (config.containsKey(CONFIG_KEY_URL)) {
                url = config.getString(CONFIG_KEY_URL);
            }
            if (config.containsKey(CONFIG_KEY_USER)) {
                user = config.getString(CONFIG_KEY_USER);
            }
            if (config.containsKey(CONFIG_KEY_PASSWORD)) {
                password = config.getString(CONFIG_KEY_PASSWORD);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // zavadeni ovladace pro databazi
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // parametry pripojeni k databazi na OpenShift
        String openShiftHost = System.getenv("OPENSHIFT_MYSQL_DB_HOST");
        String openShiftPort = System.getenv("OPENSHIFT_MYSQL_DB_PORT");
        if ((openShiftHost != null) && (openShiftPort != null)) {
            url = String.format("jdbc:mysql://%s:%s/teamland?characterEncoding=utf8", openShiftHost, openShiftPort);
        }

        // parametry prihlaseni k databazi na OpenShift
        String openShiftUser = System.getenv("OPENSHIFT_MYSQL_DB_USERNAME");
        String openShiftPassword = System.getenv("OPENSHIFT_MYSQL_DB_PASSWORD");
        if ((openShiftUser != null) && (openShiftPassword != null)) {
            user = openShiftUser;
            password = openShiftPassword;
        }
    }

    /**
     * Vytvori objekt Connection pro cteni/zapis dat do databaze
     *
     * @throws SQLException
     */
    private Connection createConnection() throws SQLException {
        // pripojeni k databazi
        return DriverManager.getConnection(url, user, password);
    }

    /** Lokalni instance tridy Admin */
    private static final Admin instance = new Admin();

    /** Cest k databazi */
    private String url = null;

    /** Uzivatelske jmeno pro pristup do databaze */
    private String user = null;

    /** Heslo pro pristup do databaze */
    private String password = null;

}
