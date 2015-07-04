package com.clubeek.dao.impl.performance.test.enums;

import java.util.List;
import java.util.Random;

import com.clubeek.dao.impl.performance.enums.MyEnum;

public class MyEnumValueModule {
    private static Random gen = new Random();

    public long testInsertMyEnumValue(MyEnumDao myEnumDao, List<MyEnum> list) {

        long start = System.nanoTime();
        for (MyEnum me : list) {
            myEnumDao.saveMyEnumAsValue(me);
        }
        return System.nanoTime() - start;
    }
    
    public long testSelectMyEnumValue(MyEnumDao myEnumDao, int rounds) {
        
        long time = 0;
        for (int i = 0; i < rounds; i++) {
            String value = MyEnum.values()[gen.nextInt(MyEnum.values().length)].name();
            long start = System.nanoTime();
            myEnumDao.selectMyEnumByValue(value);
            time += System.nanoTime() - start;
        }
        return time;
    }
}
