package com.clubeek.dao.impl.performance.test.indexes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.clubeek.dao.impl.performance.enums.MyEnum;
import com.clubeek.dao.impl.performance.model.MyEntry;

public class Tools {
    private static Random gen = new Random();

    public static List<MyEntry> createListOfMyEntries(int numOfEntries) {
        List<MyEntry> list = new ArrayList<MyEntry>();
        for (int i = 0; i < numOfEntries; i++) {
            MyEntry me = new MyEntry();
            
            me.setIntCol(i);
            me.setIntColIndexed(i);
            MyEnum en = getRandomMyEnum();
            me.setMyEnum(en.ordinal());
            me.setMyEnumIndexed(en.ordinal());
            list.add(me);
        }
        return list;
    }
    
    private static MyEnum getRandomMyEnum() {
        int i = gen.nextInt(MyEnum.values().length);
        return MyEnum.values()[i];
    }
}
