package com.abeltan.trainroutes.station;

import lombok.Getter;

public class StationCode {
    @Getter
    private String lineCode;
    private int number;

    public StationCode(String stationCode) {
        String line = getStationLineFrom(stationCode);
        int number = Integer.parseInt(stationCode.substring(2));
        this.lineCode = line;
        this.number = number;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StationCode)) {
            return false;
        }
        StationCode otherStationCode = (StationCode) obj;
        return this.lineCode.equals(otherStationCode.lineCode) && this.number == otherStationCode.number;
    }

    @Override
    public String toString() {
        return lineCode + number;
    }

    public static String getStationLineFrom(String stationCode) {
        return stationCode.substring(0, 2);
    }

    public static boolean isSameLine(String code1, String code2) {
        return getStationLineFrom(code1).equals(getStationLineFrom(code2));
    }
}
