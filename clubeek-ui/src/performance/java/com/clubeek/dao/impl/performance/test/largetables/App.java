package com.clubeek.dao.impl.performance.test.largetables;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.clubeek.dao.impl.performance.model.LargeTableRow;

public class App {

    private static Random gen = new Random();
    private static final int NUMBER_OF_TEST_ROUNDS = 100;
    private static final int NUMBER_OF_TEST_ROUNDS_FOR_INSERT = 30;
    private static final int NUMBER_OF_ROWS_IN_TABLE = 10_000;

    public static void main(String[] args) {
        ApplicationContext ctx = new FileSystemXmlApplicationContext("src/performance/java/com/clubeek/dao/impl/performance/test/largetables/test-config.xml");
        LargeTableDao largeTableDao = ctx.getBean("largeTableDao", LargeTableDao.class);
        
        long insert = 0;
        long batchInsert = 0;
        long delete = 0;
        long batchDelete = 0;
        
        for (int i = 0; i < NUMBER_OF_TEST_ROUNDS_FOR_INSERT; i++) {
            if (gen.nextInt(2) == 0) {
                insert = fillTableWithData(largeTableDao);
                batchInsert = fillTableWithDataFromList(largeTableDao);
            } else {
                batchInsert = fillTableWithDataFromList(largeTableDao);
                insert = fillTableWithData(largeTableDao);
            }
        }
        
        long oneColumn = 0;
        long allColumns = 0;
        
        for (int i = 0; i < NUMBER_OF_TEST_ROUNDS; i++) {
            if (gen.nextInt(2) == 0) {
                oneColumn += runOneColumnTest(largeTableDao);
                allColumns += runAllColumnsTest(largeTableDao);
            } else {
                allColumns += runAllColumnsTest(largeTableDao);
                oneColumn += runOneColumnTest(largeTableDao);
            }
        }
        
        System.out.println();
        System.out.println("Normal INSERT:  " + insert);
        System.out.println("Batch INSERT:   " + batchInsert);
        System.out.println("Diff: " + (insert - batchInsert) + " | in %: " + (batchInsert / (insert / 100)));
        System.out.println();
        System.out.println("One Column:  " + oneColumn);
        System.out.println("All Columns: " + allColumns);
        System.out.println("Diff: " + (oneColumn - allColumns) + " | in %: " + (allColumns / (oneColumn / 100)));
    }
    
    private static long runOneColumnTest(LargeTableDao largeTableDao) {
        int id = gen.nextInt(NUMBER_OF_ROWS_IN_TABLE) + 1;
        long start = System.nanoTime();
        largeTableDao.getOneColumn(id);
        return System.nanoTime() - start;
    }
    
    private static long runAllColumnsTest(LargeTableDao largeTableDao) {
        int id = gen.nextInt(NUMBER_OF_ROWS_IN_TABLE) + 1;
        long start = System.nanoTime();
        largeTableDao.getAllColumns(id);
        return System.nanoTime() - start;
    }
    
    private static long fillTableWithData(LargeTableDao largeTableDao) {
        List<LargeTableRow> list = createListOfLargeTableRows();
        long start = System.nanoTime();
        for (LargeTableRow l : list) {
            largeTableDao.insertRow(l);
        }
        return System.nanoTime() - start;
    }
    
    private static long fillTableWithDataFromList(LargeTableDao largeTableDao) {
        List<LargeTableRow> list = createListOfLargeTableRows();
        long start = System.nanoTime();
        largeTableDao.insertList(list);
        return System.nanoTime() - start;
    }
    
    private static List<LargeTableRow> createListOfLargeTableRows() {
        List<LargeTableRow> list = new ArrayList<LargeTableRow>();
        for (int i = 0; i < NUMBER_OF_ROWS_IN_TABLE; i++) {
            LargeTableRow l = new LargeTableRow();
            
            l.setStrCol1(UUID.randomUUID().toString());
            l.setStrCol2(UUID.randomUUID().toString());
            l.setStrCol3(UUID.randomUUID().toString());
            l.setStrCol4(UUID.randomUUID().toString());
            l.setStrCol5(UUID.randomUUID().toString());
            l.setStrCol6(UUID.randomUUID().toString());
            l.setStrCol7(UUID.randomUUID().toString());
            l.setStrCol8(UUID.randomUUID().toString());
            l.setStrCol9(UUID.randomUUID().toString());
            l.setStrCol10(UUID.randomUUID().toString());
            
            l.setIntCol1(gen.nextInt());
            l.setIntCol2(gen.nextInt());
            l.setIntCol3(gen.nextInt());
            l.setIntCol4(gen.nextInt());
            l.setIntCol5(gen.nextInt());
            l.setIntCol6(gen.nextInt());
            l.setIntCol7(gen.nextInt());
            l.setIntCol8(gen.nextInt());
            l.setIntCol9(gen.nextInt());
            l.setIntCol10(gen.nextInt());
            
            list.add(l);
        }
        return list;
    }
}
