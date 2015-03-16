package com.clubeek.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test functionality of the SimpleEnumSet
 *
 * @author Marek Svarc
 */
public class SimpleEnumSetTest {

    public static enum TestEnum {

        ENUM_A, ENUM_B, ENUM_C;
    }

    public SimpleEnumSetTest() {
    }

    /**
     * Test of addValue method, of class SimpleEnumSet.
     */
    @Test
    public void testAddValue() {
        SimpleEnumSet test = new SimpleEnumSet();
        test.addValue(TestEnum.ENUM_A);
        test.addValue(TestEnum.ENUM_B);
        test.addValue(TestEnum.ENUM_C);
        assertEquals(test.getFlags(), 7);
    }

    /**
     * Test of addValues method, of class SimpleEnumSet.
     */
    @Test
    public void testAddValues() {
        SimpleEnumSet test = new SimpleEnumSet();
        test.addValues(TestEnum.ENUM_A, TestEnum.ENUM_B, TestEnum.ENUM_C);
        assertEquals(test.getFlags(), 7);
    }
    
    /**
     * Test of clear method, of class SimpleEnumSet.
     */
    @Test
    public void testClear() {
        SimpleEnumSet set = new SimpleEnumSet(TestEnum.ENUM_A, TestEnum.ENUM_B);
        assertFalse(set.isEmpty());
        
        set.clear();
        assertTrue(set.isEmpty());
    }

    /**
     * Test of contains method, of class SimpleEnumSet.
     */
    @Test
    public void testContains() {
        SimpleEnumSet test = new SimpleEnumSet(TestEnum.ENUM_A, TestEnum.ENUM_C);
        assertFalse(test.contains(TestEnum.ENUM_B));
        assertTrue(test.contains(TestEnum.ENUM_A));
    }

    /**
     * Test of containsSome method, of class SimpleEnumSet.
     */
    @Test
    public void testContainsSome() {
        SimpleEnumSet test = new SimpleEnumSet(TestEnum.ENUM_A);
        assertFalse(test.containsSome(TestEnum.ENUM_B, TestEnum.ENUM_C));
        assertTrue(test.containsSome(TestEnum.ENUM_A, TestEnum.ENUM_B));
    }

    /**
     * Test of containsAll method, of class SimpleEnumSet.
     */
    @Test
    public void testContainsAll() {
        SimpleEnumSet test = new SimpleEnumSet(TestEnum.ENUM_A, TestEnum.ENUM_B);
        assertFalse(test.containsAll(TestEnum.ENUM_B, TestEnum.ENUM_C));
        assertTrue(test.containsAll(TestEnum.ENUM_A, TestEnum.ENUM_B));
    }

}
