package com.abeltan.trainroutes.station;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StationCodesTest {

    @Test
    void aStationCanHaveMultipleStationCodes() {
        StationCodes stationCodes = new StationCodes();
        stationCodes.add("Bugis", "DT14");
        stationCodes.add("Bugis", "EW12");

        assertTrue(stationCodes.getStationCodesFrom("Bugis").contains(new StationCode("DT14")));
        assertTrue(stationCodes.getStationCodesFrom("Bugis").contains(new StationCode("EW12")));
    }
}