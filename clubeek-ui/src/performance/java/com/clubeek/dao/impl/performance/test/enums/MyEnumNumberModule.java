package com.clubeek.dao.impl.performance.test.enums;

import java.util.List;
import java.util.Random;

import com.clubeek.dao.impl.performance.enums.MyEnum;

public class MyEnumNumberModule {
    private static Random gen = new Random();

    public long testInsertMyEnumNumber(MyEnumDao myEnumDao, List<MyEnum> list) {

        long start = System.nanoTime();
        for (MyEnum me : list) {
            myEnumDao.saveMyEnumAsNumber(me);
        }
        return System.nanoTime() - start;
    }
    
    public long testSelectMyEnumNumber(MyEnumDao myEnumDao, int rounds) {
        
        long time = 0;
        for (int i = 0; i < rounds; i++) {
            int number = gen.nextInt(MyEnum.values().length);
            long start = System.nanoTime();
            myEnumDao.selectMyEnumByNumber(number);
            time += System.nanoTime() - start;
        }
        return time;
    }
}
