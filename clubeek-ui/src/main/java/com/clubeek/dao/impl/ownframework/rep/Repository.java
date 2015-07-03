package com.clubeek.dao.impl.ownframework.rep;

import com.clubeek.model.IUnique;
import java.sql.ResultSet;

/**
 * Interface providing standard operations on the database table.
 *
 * @param <T> Data class used for data exchange with database.
 */
public interface Repository<T extends IUnique> {

    /**
     * Modifies one row in the database table.
     *
     * @param value Data to be writen into database.
     */
    void updateRow(T value);

    /**
     * Insert one row to the database table.
     *
     * @param value Data to be inserted into database.
     */
    void insertRow(T value);

    /**
     * Delete one row from the database table.
     *
     * @param id Index of row which to be deleted from the database.
     */
    void deleteRow(int id);

    /**
     * Exchange two records in the table
     *
     * @param idA first index of the row to be exchanged
     * @param idB second index of the row to be exchanged
     */
    void exchangeRows(int idA, int idB);

    /**
     * Reads data from one column and writes them into data object.
     *
     * @param result Result of the SQL select command.
     * @param resultsColumnId Id of the table column.
     * @param data Instance of data object.
     * @param dataColumnId Id of object item where are writen data from the
     * database.
     */
    void readValue(ResultSet result, int resultsColumnId, T data, Object dataColumnId);
}
