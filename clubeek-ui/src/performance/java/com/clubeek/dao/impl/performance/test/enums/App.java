package com.clubeek.dao.impl.performance.test.enums;

import java.util.Random;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class App {
    private static Random gen = new Random();
    
    private static final int NUM_OF_ENUMS = 100;
    private static final int NUM_OF_SELECT_ENUMS = 10_000;
    private static final int NUM_OF_TEST_FOR_INSERT_ROUNDS = 10;
    private static final int NUM_OF_TEST_FOR_SELECT_ROUNDS = 100;

    public static void main(String[] args) {
        long valueInsertTime = 0;
        long numberInsertTime = 0;
        long valueSelectTime = 0;
        long numberSelectTime = 0;
        
        ApplicationContext ctx = new FileSystemXmlApplicationContext("src/performance/java/com/clubeek/dao/impl/performance/test/enums/test-config.xml");
        MyEnumDao myEnumDao = ctx.getBean("myEnumDao", MyEnumDao.class);
        
        MyEnumValueModule valueModule = new MyEnumValueModule();
        MyEnumNumberModule numberModule = new MyEnumNumberModule();
        
        // insert test
        for (int j = 0; j < NUM_OF_TEST_FOR_INSERT_ROUNDS; j++) {
            // change of test order
            if (gen.nextInt(2) == 0) {
                myEnumDao.deleteAllEnumAsValue();
                for (int i = 0; i < NUM_OF_TEST_FOR_INSERT_ROUNDS; i++) {
                    valueInsertTime += valueModule.testInsertMyEnumValue(myEnumDao, Tools.createMyEnumList(NUM_OF_ENUMS));
                }
                
                myEnumDao.deleteAllEnumAsNumber();
                for (int i = 0; i < NUM_OF_TEST_FOR_INSERT_ROUNDS; i++) {
                    numberInsertTime += numberModule.testInsertMyEnumNumber(myEnumDao, Tools.createMyEnumList(NUM_OF_ENUMS));
                }
            } else {
                myEnumDao.deleteAllEnumAsNumber();
                for (int i = 0; i < NUM_OF_TEST_FOR_INSERT_ROUNDS; i++) {
                    numberInsertTime += numberModule.testInsertMyEnumNumber(myEnumDao, Tools.createMyEnumList(NUM_OF_ENUMS));
                }
                
                myEnumDao.deleteAllEnumAsValue();
                for (int i = 0; i < NUM_OF_TEST_FOR_INSERT_ROUNDS; i++) {
                    valueInsertTime += valueModule.testInsertMyEnumValue(myEnumDao, Tools.createMyEnumList(NUM_OF_ENUMS));
                }
            }
        }
        
        // create test data (value)
        myEnumDao.deleteAllEnumAsValue();
        valueModule.testInsertMyEnumValue(myEnumDao, Tools.createMyEnumList(NUM_OF_SELECT_ENUMS));
        // create test data (number)
        myEnumDao.deleteAllEnumAsNumber();
        numberModule.testInsertMyEnumNumber(myEnumDao, Tools.createMyEnumList(NUM_OF_SELECT_ENUMS));
        
        // select test
        for (int j = 0; j < NUM_OF_TEST_FOR_SELECT_ROUNDS; j++) {
            // change of test order
            if (gen.nextInt(2) == 0) {
                valueSelectTime += valueModule.testSelectMyEnumValue(myEnumDao, NUM_OF_TEST_FOR_SELECT_ROUNDS);
                numberSelectTime += numberModule.testSelectMyEnumNumber(myEnumDao, NUM_OF_TEST_FOR_SELECT_ROUNDS);
            } else {
                numberSelectTime += numberModule.testSelectMyEnumNumber(myEnumDao, NUM_OF_TEST_FOR_SELECT_ROUNDS);
                valueSelectTime += valueModule.testSelectMyEnumValue(myEnumDao, NUM_OF_TEST_FOR_SELECT_ROUNDS);
            }
        }
        
        System.out.println();
        System.out.println("Number of test rounds for insert: " + NUM_OF_TEST_FOR_INSERT_ROUNDS);
        System.out.println("Saving value time:      " + valueInsertTime);
        System.out.println("Saving number time:     " + numberInsertTime);
        System.out.println("valueTime - numberTime: " + (valueInsertTime - numberInsertTime) + ", in %: " + (numberInsertTime / (valueInsertTime / 100)));
        System.out.println();
        System.out.println("Number of test rounds for select: " + NUM_OF_TEST_FOR_SELECT_ROUNDS);
        System.out.println("Select value time:      " + valueSelectTime);
        System.out.println("Select number time:     " + numberSelectTime);
        System.out.println("valueTime - numberTime: " + (valueSelectTime - numberSelectTime) + ", in %: " + (numberSelectTime / (valueSelectTime / 100)));
    }
}
