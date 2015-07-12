package com.clubeek.enums;

/**
 * Identifies type of sport
 *
 * @author Marek Svarc
 */
public enum SportType {
                                            // ordinal number
    UNKNOWN("neznámý"),                     // 0 
    FOOTBALL("fotbal"),                     // 1
    HOCKEY("hokej"),                        // 2
    BASKETBALL("basketbal"),                // 3
    VOLLEYBALL("volejbal"),                 // 4
    BEACH_VOLLEYBALL("plážový volejbal"),   // 5
    FLOORBALL("florbal"),                   // 6
    BASEBALL("baseball"),                   // 7
    SOFTBALL("softball"),                   // 8
    TENNIS("tenis");                        // 9

    private final String text;

    private SportType(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
    
}
