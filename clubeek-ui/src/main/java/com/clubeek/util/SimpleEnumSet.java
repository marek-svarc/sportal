package com.clubeek.util;

import java.io.Serializable;

/**
 * Enables create fast bit set of the enum values.
 *
 * @author Marek Svarc
 * @param <E> Type of the enum.
 */
public final class SimpleEnumSet<E extends Enum<?>> implements Serializable {

    /** Stored enum values using a bit operations */
    private long flags = 0;

    /**
     * Converts array of enum values to the bit set.
     *
     * @param values Array of enum values to be converted.
     * @return Long value with stored enums.
     */
    private long convertArrayToBitSet(E... values) {
        long bitSet = 0;
        for (E value : values) {
            bitSet |= (1 << value.ordinal());
        }

        return bitSet;
    }

    /** Empty constuctor */
    public SimpleEnumSet() {
    }

    /**
     * Create and initialize set of enum values
     *
     * @param values Array of enum values that will be added to the set.
     */
    public SimpleEnumSet(E... values) {
        addValues(values);
    }

    /**
     * Clear all flags.
     */
    public void clear() {
        flags = 0;
    }

    /**
     * Add enum value to the set.
     *
     * @param value Enum value that will be added to the set.
     */
    public void addValue(E value) {
        flags |= (1 << value.ordinal());
    }

    /**
     * Add array of enumn values to the set.
     *
     * @param values Array of enum values that will be added to the set.
     */
    public void addValues(E... values) {
        flags |= convertArrayToBitSet(values);
    }

    /**
     * Remove array of enumn values from the set.
     *
     * @param values Array of enum values that will be removed to the set.
     */
    public void removeValues(E... values) {
        flags &= (~convertArrayToBitSet(values));
    }

    public boolean isEmpty() {
        return flags == 0;
    }

    public boolean contains(E value) {
        return ((flags & (1 << value.ordinal())) != 0);
    }

    /**
     * Test if set contains some of tested values.
     *
     * @param values Array of tested values.
     * @return If set contains some tested values returns true.
     */
    public boolean containsSome(E... values) {
        return ((flags & convertArrayToBitSet(values)) != 0);
    }

    /**
     * Test if set contains all tested values.
     *
     * @param values Array of tested values.
     * @return If set contains all tested values returns true.
     */
    public boolean containsAll(E... values) {
        long testFlags = convertArrayToBitSet(values);
        return ((flags & testFlags) == testFlags);
    }

    /** Returns stored enum values using a bit operations. */
    public long getFlags() {
        return flags;
    }
}
