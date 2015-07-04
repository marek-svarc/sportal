package com.clubeek.dao.impl.performance.test.indexes;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.clubeek.dao.impl.performance.model.MyEntry;

public class App {
    private static final int NUMBER_OF_ROWS = 100_000;
    private static final int NUMBER_OF_TEST_ROUNDS = 100;

    public static void main(String[] args) {
        ApplicationContext ctx = new FileSystemXmlApplicationContext("src/performance/java/com/clubeek/dao/impl/performance/test/indexes/test-config.xml");
        IndexedTableDao indexedTableDao = ctx.getBean("indexedTableDao", IndexedTableDao.class);
        
        if (indexedTableDao.getAllMyEntries().size() < NUMBER_OF_ROWS) {
            indexedTableDao.deleteAllMyEntries();
            for (MyEntry me : Tools.createListOfMyEntries(NUMBER_OF_ROWS)) {
                indexedTableDao.insertRow(me);
            }
        }
        
        Long indexedInt;
        Long notIndexedInt;
        Long indexedEnum;
        Long notIndexedEnum;
        
        String indexedKeyName = "indexed";
        String notIndexedKeyName = "notIndexed";
        
        Map<String, Long> intResults;
        Map<String, Long> enumResults;
        
        IntegerTestModule itm = new IntegerTestModule();
        EnumTestModule etm = new EnumTestModule();
        
        intResults = itm.testIndexedVsNotIndexedIntegerColumns(indexedTableDao, NUMBER_OF_TEST_ROUNDS);
        enumResults = etm.testIndexedVsNotIndexedEnumColumns(indexedTableDao, NUMBER_OF_TEST_ROUNDS);
        
        indexedInt = intResults.get(indexedKeyName);
        notIndexedInt = intResults.get(notIndexedKeyName);
        indexedEnum = enumResults.get(indexedKeyName);
        notIndexedEnum = enumResults.get(notIndexedKeyName);
        
        System.out.println();
        System.out.println("Number of row in test table: " + NUMBER_OF_ROWS);
        System.out.println("Number of test rounds: " + NUMBER_OF_TEST_ROUNDS);
        System.out.println();
        System.out.println("Not indexed int column: " + notIndexedInt);
        System.out.println("Indexed int column:     " + indexedInt);
        System.out.println("Difference:             " + (notIndexedInt - indexedInt) + ", in %: " + (indexedInt / (notIndexedInt / 100)));
        System.out.println();
        System.out.println("Not indexed enum column: " + notIndexedEnum);
        System.out.println("Indexed enum column:     " + indexedEnum);
        System.out.println("Difference:              " + (notIndexedEnum - indexedEnum) + ", in %: " + (indexedEnum / (notIndexedEnum / 100)));
    }
}
