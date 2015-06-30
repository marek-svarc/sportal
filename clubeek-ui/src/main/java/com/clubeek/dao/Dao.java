package com.clubeek.dao;

import java.util.List;

import com.clubeek.model.Unique;

public interface Dao<T extends Unique> {

    public void updateRow(T object);
    public void insertRow(T object);
    public void deleteRow(int id);
    public void deleteRows(List<T> objects);
    public void exchangeRows(int idA, int idB);
}
