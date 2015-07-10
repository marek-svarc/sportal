package com.clubeek.enums;

/**
 * Identifies type of the club license
 *
 * @author Marek Svarc
 */
public enum LicenseType {
    
    FREE("volná"), STANDARD("standardní"), PROFESSIONAL("profesionální");
    
    private final String text;

    private LicenseType(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }

}
