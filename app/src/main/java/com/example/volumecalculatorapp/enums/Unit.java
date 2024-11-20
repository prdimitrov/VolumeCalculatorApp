package com.example.volumecalculatorapp.enums;

public enum Unit {
    MM("mm"),
    CM("cm"),
    M("m"),
    KM("km"),
    IN("in"),
    FT("ft"),
    YD("yd"),
    MI("mi");

    private final String abbreviation;

    Unit(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
}