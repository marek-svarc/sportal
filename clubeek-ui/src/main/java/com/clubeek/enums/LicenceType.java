package com.clubeek.enums;

/**
 * Identifies type of the club license
 *
 * @author Marek Svarc
 */
public enum LicenceType {
    
    FREE("volná"), STANDARD("standardní"), PROFESSIONAL("profesionální");
    
    private final String text;

    private LicenceType(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }

}
