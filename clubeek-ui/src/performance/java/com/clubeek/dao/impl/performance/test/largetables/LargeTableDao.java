package com.clubeek.dao.impl.performance.test.largetables;

import java.util.List;

import com.clubeek.dao.impl.performance.model.LargeTableRow;

public interface LargeTableDao {

    public String getOneColumn(int id);
    public LargeTableRow getAllColumns(int id);
    
    public List<String> getListOfOneColumns();
    public List<LargeTableRow> getListOfAllColumns();
    
    public void insertRow(LargeTableRow ltr);
    public void insertList(List<LargeTableRow> list);
    
    public void deleteRow(LargeTableRow ltr);
    public void deleteList(List<LargeTableRow> list);
}
