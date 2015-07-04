package com.clubeek.dao.impl.performance.test.indexes;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class IntegerTestModule {
    private static Random gen = new Random();

    public Map<String, Long> testIndexedVsNotIndexedIntegerColumns(IndexedTableDao indexedTableDao, int numOfTestRounds) {
        Map<String, Long> result = new HashMap<String, Long>();
        long indexed = 0;
        long notIndexed = 0;
        long start;
        
        int numOfRows = indexedTableDao.getAllMyEntries().size();
        
        for (int i = 0; i < numOfTestRounds; i++) {
            int rand = gen.nextInt(numOfRows);
            
            // change the order of tests
            if (gen.nextInt(2) == 0) {
                start = System.nanoTime();
                indexedTableDao.getMyEntryByIntIndexed(rand);
                indexed += System.nanoTime() - start;
                
                start = System.nanoTime();
                indexedTableDao.getMyEntryByInt(rand);
                notIndexed += System.nanoTime() - start;
            } else {
                start = System.nanoTime();
                indexedTableDao.getMyEntryByInt(rand);
                notIndexed += System.nanoTime() - start;
                
                start = System.nanoTime();
                indexedTableDao.getMyEntryByIntIndexed(rand);
                indexed += System.nanoTime() - start;
            }
        }
        
        result.put("indexed", indexed);
        result.put("notIndexed", notIndexed);
        return result;
    }
}
