package com.abeltan.trainroutes.station;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void thatThe_StaionLine_IsCorrect() {
        String code = "NS12";
        assertEquals("NS", StationCode.getStationLineFrom(code));
    }

    @Test
    void thatTwo_LinesAre_TheSame_OrNot() {
        assertTrue(StationCode.isSameLine("NS1", "NS12"));
        assertFalse(StationCode.isSameLine("NS1", "DT1"));
    }
}