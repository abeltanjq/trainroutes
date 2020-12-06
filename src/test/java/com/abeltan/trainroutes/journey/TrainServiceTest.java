package com.abeltan.trainroutes.journey;

import com.abeltan.trainroutes.station.StationCode;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TrainServiceTest {
    @Test
    void isTime_AtPeakHour () {
        LocalDateTime mondayMorning = LocalDateTime.parse("2020-11-02T06:00");
        LocalDateTime mondayEvening = LocalDateTime.parse("2020-11-02T20:59");
        LocalDateTime mondayNight = LocalDateTime.parse("2020-11-02T21:00");
        LocalDateTime sunday = LocalDateTime.parse("2020-11-01T06:01");
        assertTrue(TrainService.isPeak(mondayMorning));
        assertFalse(TrainService.isPeak(sunday));
        assertTrue(TrainService.isPeak(mondayEvening));
        assertFalse(TrainService.isPeak(mondayNight));
    }

    @Test
    void isTime_AtNighHour () {
        LocalDateTime mondayNight = LocalDateTime.parse("2020-11-02T21:59");
        LocalDateTime saturdayNight = LocalDateTime.parse("2020-11-07T22:00");
        LocalDateTime midnight = LocalDateTime.parse("2020-11-07T00:00");
        assertFalse(TrainService.isNight(mondayNight));
        assertTrue(TrainService.isNight(saturdayNight));
        assertTrue(TrainService.isNight(midnight));
    }

    @Test
    void thatTravellingTime_DuringPeakHour_IsCorrect() {
        LocalDateTime mondayMorning = LocalDateTime.parse("2020-11-02T06:00");
        LocalDateTime tuesdayNight = LocalDateTime.parse("2020-11-03T20:45");

        TrainService trainService = new TrainService();
        assertEquals(15, trainService.getTravellingTimeOf(new StationCode("NS1"), new StationCode("CC12"), mondayMorning));
        assertEquals(12, trainService.getTravellingTimeOf(new StationCode("NE2"), new StationCode("NE3"), mondayMorning));
        assertEquals(10, trainService.getTravellingTimeOf(new StationCode("DT5"), new StationCode("DT6"), mondayMorning));
        assertEquals(15, trainService.getTravellingTimeOf(new StationCode("NS1"), new StationCode("CC12"), tuesdayNight));
        assertEquals(12, trainService.getTravellingTimeOf(new StationCode("NE2"), new StationCode("NE3"), tuesdayNight));
        assertEquals(10, trainService.getTravellingTimeOf(new StationCode("DT5"), new StationCode("DT6"), tuesdayNight));
    }

    @Test
    void thatTravellingTime_DuringNightHour_IsCorrect() {
        LocalDateTime saturdayNight = LocalDateTime.parse("2020-11-07T22:00");
        LocalDateTime midnight = LocalDateTime.parse("2020-11-07T00:00");

        TrainService trainService = new TrainService();
        assertNull(trainService.getTravellingTimeOf(new StationCode("NS1"), new StationCode("DT12"), saturdayNight));
        assertNull(trainService.getTravellingTimeOf(new StationCode("DT11"), new StationCode("DT12"), saturdayNight));
        assertNull(trainService.getTravellingTimeOf(new StationCode("CC12"), new StationCode("CG23"), saturdayNight));
        assertNull(trainService.getTravellingTimeOf(new StationCode("CG22"), new StationCode("CG23"), saturdayNight));
        assertNull(trainService.getTravellingTimeOf(new StationCode("NE22"), new StationCode("CE23"), saturdayNight));
        assertEquals(10, trainService.getTravellingTimeOf(new StationCode("NS2"), new StationCode("EW3"), midnight));
        assertEquals(10, trainService.getTravellingTimeOf(new StationCode("NS2"), new StationCode("NS3"), midnight));
        assertEquals(8, trainService.getTravellingTimeOf(new StationCode("TE1"), new StationCode("TE2"), midnight));
    }

    @Test
    void thatTravellingTime_DuringNonPeakHour_IsCorrect() {
        LocalDateTime saturdayMorning = LocalDateTime.parse("2020-11-07T08:00");
        LocalDateTime mondayAfternoon = LocalDateTime.parse("2020-11-02T13:00");

        TrainService trainService = new TrainService();
        assertEquals(8, trainService.getTravellingTimeOf(new StationCode("DT1"), new StationCode("DT2"), saturdayMorning));
        assertEquals(8, trainService.getTravellingTimeOf(new StationCode("TE1"), new StationCode("TE2"), saturdayMorning));
        assertEquals(10, trainService.getTravellingTimeOf(new StationCode("NS1"), new StationCode("NS2"), mondayAfternoon));
        assertEquals(10, trainService.getTravellingTimeOf(new StationCode("NS1"), new StationCode("EW2"), mondayAfternoon));
    }

    @Test
    void thatDateTime_IsFormatted_Correctly() {
        String actualDateTime1 = TrainService.formatDateTime("2020-07-12", "21", "12");
        assertEquals("2020-07-12T21:12", actualDateTime1);
        String actualDateTime2 = TrainService.formatDateTime("2020-07-12", "1", "1");
        assertEquals("2020-07-12T01:01", actualDateTime2);
    }
}