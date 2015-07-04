package com.clubeek.dao.impl.performance.test.indexes;

import java.util.List;

import com.clubeek.dao.impl.performance.model.MyEntry;

public interface IndexedTableDao {

    public List<MyEntry> getAllMyEntries();
    public MyEntry getMyEntryById(int id);
    
    public MyEntry getMyEntryByInt(int number);
    public MyEntry getMyEntryByIntIndexed(int number);
    
    public List<MyEntry> getMyEntriesByMyEnum(int myEnum);
    public List<MyEntry> getMyEntriesByMyEnumIndexed(int myEnum);
    
    public void deleteAllMyEntries();
    public void insertRow(MyEntry myEntry);
}
