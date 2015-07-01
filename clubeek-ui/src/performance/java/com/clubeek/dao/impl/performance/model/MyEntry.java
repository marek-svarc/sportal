package com.clubeek.dao.impl.performance.model;

public class MyEntry {

    private int id;
    private String stringCol;
    private String stringColIndexed;
    private int intCol;
    private int intColIndexed;
    private int myEnum;
    private int myEnumIndexed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStringCol() {
        return stringCol;
    }

    public void setStringCol(String stringCol) {
        this.stringCol = stringCol;
    }

    public int getIntCol() {
        return intCol;
    }

    public void setIntCol(int intCol) {
        this.intCol = intCol;
    }

    public int getMyEnum() {
        return myEnum;
    }

    public void setMyEnum(int myEnum) {
        this.myEnum = myEnum;
    }
    public int getIntColIndexed() {
        return intColIndexed;
    }

    public void setIntColIndexed(int intColIndexed) {
        this.intColIndexed = intColIndexed;
    }

    public String getStringColIndexed() {
        return stringColIndexed;
    }

    public void setStringColIndexed(String stringColIndexed) {
        this.stringColIndexed = stringColIndexed;
    }

    public int getMyEnumIndexed() {
        return myEnumIndexed;
    }

    public void setMyEnumIndexed(int myEnumIndexed) {
        this.myEnumIndexed = myEnumIndexed;
    }
    
    
}
