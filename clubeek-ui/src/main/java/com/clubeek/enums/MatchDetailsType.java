package com.clubeek.enums;

/**
 * Identifies type of match details
 *
 * @author Marek Svarc
 */
public enum MatchDetailsType {

    NONE("žádné"), BASE("základní"), FULL("podrobné");

    private final String text;
    
    private MatchDetailsType(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
