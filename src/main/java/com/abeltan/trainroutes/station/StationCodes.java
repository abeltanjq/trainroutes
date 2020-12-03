package com.abeltan.trainroutes.station;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StationCodes {
    private Map<String, List<StationCode>> stationCodes;

    public StationCodes() {
        stationCodes = new HashMap<>();
    }

    public void add(String stationName, String stationCode) {
        List<StationCode> sc;
        if (stationCodes.get(stationName) == null) {
            sc = new ArrayList<>();
        } else {
            sc = stationCodes.get(stationName);
        }
        sc.add(new StationCode(stationCode));
        stationCodes.put(stationName, sc);
    }

    public List<StationCode> getStationCodesFrom(String stationName) {
        return stationCodes.get(stationName);
    }
}
