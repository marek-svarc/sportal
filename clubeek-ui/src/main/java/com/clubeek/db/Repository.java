package com.clubeek.db;

import com.clubeek.model.Unique;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interface providing standard operations on the database table.
 *
 * @param <T> Data class used for data exchange with database.
 */
public interface Repository<T extends Unique> {

    /**
     * Modifies one row in the database table.
     *
     * @param value Data to be writen into database.
     * @throws SQLException
     */
    void updateRow(T value) throws SQLException;

    /**
     * Insert one row to the database table.
     *
     * @param value Data to be inserted into database.
     * @throws SQLException
     */
    void insertRow(T value) throws SQLException;

    /**
     * Delete one row from the database table.
     *
     * @param id Index of row which to be deleted from the database.
     * @throws SQLException
     */
    void deleteRow(int id) throws SQLException;

    /**
     * Exchange two records in the table
     *
     * @param idA first index of the row to be exchanged
     * @param idB second index of the row to be exchanged
     * @throws SQLException
     */
    void exchangeRows(int idA, int idB) throws SQLException;

    /**
     * Reads data from one column and writes them into data object.
     *
     * @param result Result of the SQL select command.
     * @param resultsColumnId Id of the table column.
     * @param data Instance of data object.
     * @param dataColumnId Id of object item where are writen data from the
     * database.
     * @throws SQLException
     */
    void readValue(ResultSet result, int resultsColumnId, T data, Object dataColumnId) throws SQLException;
}
