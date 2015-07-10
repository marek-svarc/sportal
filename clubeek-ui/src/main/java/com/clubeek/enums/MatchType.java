package com.clubeek.enums;

/**
 * Identifies type (importance) of the match
 *
 * @author Marek Svarc
 */
public enum MatchType {

    MATCH("utkání"), FRIENDLY_MATCH("přátelské utkání"), CUP("pohár"), CHAMPIONSHIP("mistrovství");

    private final String text;

    private MatchType(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
    
}
