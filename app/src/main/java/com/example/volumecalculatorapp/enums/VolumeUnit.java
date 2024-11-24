package com.example.volumecalculatorapp.enums;

public enum VolumeUnit {
    MM3("mm³"),
    CM3("cm³"),
    M3("m³"),
    CUIN("cu in"),
    CUFT("cu ft"),
    CUYD("cu yd"),
    L("liters");
    private final String abbreviation;

    VolumeUnit(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
}
