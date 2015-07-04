package com.clubeek.dao.impl.performance.test.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.clubeek.dao.impl.performance.enums.MyEnum;

public class Tools {
    private static Random gen = new Random();

    public static List<MyEnum> createMyEnumList(int enums) {
        List<MyEnum> list = new ArrayList<MyEnum>();
        for (int i = 0; i < enums; i++) {
            MyEnum me = null;
            int num = gen.nextInt(MyEnum.values().length);
            switch (num) {
                case 0:
                    me = MyEnum.MY_ENUM_ONE;
                    break;
                case 1:
                    me = MyEnum.MY_ENUM_TWO;
                    break;
                case 2:
                    me = MyEnum.MY_ENUM_THREE;
                    break;
                case 3:
                    me = MyEnum.MY_ENUM_FOUR;
                    break;
                case 4:
                    me = MyEnum.MY_ENUM_FIVE;
                    break;
            }
            list.add(me);
        }
        return list;
    }
}
