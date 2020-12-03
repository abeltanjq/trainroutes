package com.abeltan.trainroutes.station;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StationCodeTest {

    @Test
    void testEquals() {
        StationCode sc1 = new StationCode("DT0");
        StationCode sc2 = new StationCode("DT0");
        assertTrue(sc1.equals(sc2));
    }

    @Test
    void testToString() {
        StationCode sc1 = new StationCode("DT0");
        assertEquals("DT0", sc1.toString());
    }
}