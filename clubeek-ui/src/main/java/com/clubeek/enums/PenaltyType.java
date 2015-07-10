package com.clubeek.enums;

/**
 * Identifies type of the player penalty
 *
 * @author Marek Svarc
 */
public enum PenaltyType {

    YELLOW_CARD("žlutá karta"), RED_CARD("červená karta"), EXCLUSION("vyloučení");

    private final String text;    
    
    private PenaltyType(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
