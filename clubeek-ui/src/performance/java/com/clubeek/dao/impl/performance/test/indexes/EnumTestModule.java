package com.clubeek.dao.impl.performance.test.indexes;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.clubeek.dao.impl.performance.enums.MyEnum;

public class EnumTestModule {

    private static Random gen = new Random();

    public Map<String, Long> testIndexedVsNotIndexedEnumColumns(IndexedTableDao indexedTableDao, int numOfTestRounds) {
        Map<String, Long> result = new HashMap<String, Long>();
        long indexed = 0;
        long notIndexed = 0;
        long start;
        
        int numOfEnums = MyEnum.values().length;
        
        for (int i = 0; i < numOfTestRounds; i++) {
            int rand = gen.nextInt(numOfEnums);
            
            if (gen.nextInt(2) == 0) {
                start = System.nanoTime();
                indexedTableDao.getMyEntriesByMyEnumIndexed(rand);
                indexed += System.nanoTime() - start;
                
                start = System.nanoTime();
                indexedTableDao.getMyEntriesByMyEnum(rand);
                notIndexed += System.nanoTime() - start;
            } else {
                start = System.nanoTime();
                indexedTableDao.getMyEntriesByMyEnum(rand);
                notIndexed += System.nanoTime() - start;
                
                start = System.nanoTime();
                indexedTableDao.getMyEntriesByMyEnumIndexed(rand);
                indexed += System.nanoTime() - start;
            }
        }
        
        result.put("indexed", indexed);
        result.put("notIndexed", notIndexed);
        return result;
    }
}
