package com.clubeek.enums;

/**
 * Identifies type (importance) of the match
 *
 * @author Marek Svarc
 */
public enum MatchType {
                                            // ordinal
    MATCH("utkání"),                        // 0
    FRIENDLY_MATCH("přátelské utkání"),     // 1
    CUP("pohár"),                           // 2
    CHAMPIONSHIP("mistrovství");            // 3

    private final String text;

    private MatchType(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
    
}
