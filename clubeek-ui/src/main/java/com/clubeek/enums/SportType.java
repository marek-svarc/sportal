package com.clubeek.enums;

/**
 * Identifies type of sport
 *
 * @author Marek Svarc
 */
public enum SportType {

    UNKNOWN("neznámý"), FOOTBALL("fotbal"), HOCKEY("hokej"), BASKETBALL("basketbal"),
    VOLLEYBALL("volejbal"), BEACH_VOLLEYBALL("plážový volejbal"), FLOORBALL("florbal"),
    BASEBALL("baseball"), SOFTBALL("softball"), TENNIS("tenis");

    private final String text;

    private SportType(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
    
}
