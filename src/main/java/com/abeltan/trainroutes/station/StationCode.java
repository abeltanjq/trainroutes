package com.abeltan.trainroutes.station;

public class StationCode {
    private String lineCode;
    private int number;
    public StationCode(String stationCode) {
        String line = stationCode.substring(0,2);
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

    public String getLineCode() {
        return lineCode.toUpperCase();
    }
}
