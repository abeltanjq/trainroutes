package com.abeltan.trainroutes.station;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class StationCode {
    @Getter
    private Line lineCode;
    private int number;

    @AllArgsConstructor
    public enum Line {
        NS("NS"),
        EW("EW"),
        CG("CG"),
        NE("NE"),
        CC("CC"),
        CE("CE"),
        DT("DT"),
        TE("TE");

        private final String line;
    }

    public StationCode(String stationCode) {
        String line = getStationLineFrom(stationCode);
        int number = Integer.parseInt(stationCode.substring(2));
        this.lineCode = Line.valueOf(line);
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
        return lineCode.toString() + number;
    }

    public static String getStationLineFrom(String stationCode) {
        return stationCode.substring(0, 2);
    }

    public static boolean isSameLine(StationCode code1, StationCode code2) {
        return code1.getLineCode().equals(code2.getLineCode());
    }
}
