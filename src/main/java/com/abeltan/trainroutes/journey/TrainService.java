package com.abeltan.trainroutes.journey;

import com.abeltan.trainroutes.station.StationCode;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@NoArgsConstructor
public class TrainService {

    public Integer getTravellingTimeOf(StationCode from, StationCode to, LocalDateTime startTime) {
        if (startTime == null) {
            return 1;
        } else if (isPeak(startTime)) {
            return peakHourTravellingTimeOf(from, to);
        } else if (isNight(startTime)) {
            return nightHourTravellingTimeOf(from, to);
        }
        return nonPeakTravellingHourOf(from, to);
    }

    private int nonPeakTravellingHourOf(StationCode from, StationCode to) {
        if (!StationCode.isSameLine(from, to)) {
            return 10;
        }
        switch (to.getLineCode()) {
            case DT:
            case TE:
                return 8;
            default:
                return 10;
        }
    }

    private int peakHourTravellingTimeOf(StationCode from, StationCode to) {
        if (!StationCode.isSameLine(from, to)) {
            return 15;
        }
        switch (to.getLineCode()) {
            case NS:
            case NE:
                return 12;
            default:
                return 10;
        }
    }

    private Integer nightHourTravellingTimeOf(StationCode from, StationCode to) {
        List<StationCode.Line> closedLines = List.of(StationCode.Line.DT, StationCode.Line.CG, StationCode.Line.CE);
        for (StationCode.Line cl : closedLines) {
            if (from.getLineCode() == cl || to.getLineCode() == cl) {
                return null;
            }
        }
        if (!StationCode.isSameLine(from, to)) {
            return 10;
        }
        switch (to.getLineCode()) {
            case TE:
                return 8;
            default:
                return 10;
        }
    }

    public static boolean isPeak(LocalDateTime dateTime) {
        LocalTime now = dateTime.toLocalTime();
        LocalTime am0559 = LocalTime.of(5,59);
        LocalTime am9 = LocalTime.of(9,0);
        LocalTime pm1759 = LocalTime.of(17,59);
        LocalTime pm9 = LocalTime.of(21,0);
        boolean isMorningPeak = now.isAfter(am0559) && now.isBefore(am9);
        boolean isEveningPeak = now.isAfter(pm1759) && now.isBefore(pm9);
        boolean isWeekday = dateTime.getDayOfWeek() != DayOfWeek.SUNDAY && dateTime.getDayOfWeek() != DayOfWeek.SATURDAY;
        return isWeekday && (isMorningPeak || isEveningPeak);
    }

    public static boolean isNight(LocalDateTime dateTime) {
        LocalTime now = dateTime.toLocalTime();
        LocalTime pm2159 = LocalTime.of(21,59);
        LocalTime am6 = LocalTime.of(6,0);
        return now.isAfter(pm2159) || now.isBefore(am6);
    }

    // target format: YYYY-MM-DDThh:mm
    public static String formatDateTime(String date, String hour, String min) {
        int hh = Integer.parseInt(hour);
        int mm = Integer.parseInt(min);
        return date+"T"+String.format("%02d", hh)+":"+String.format("%02d", mm);
    }

    public static String getTypeOfService(LocalDateTime dateTime) {
        if(isPeak(dateTime)) {
            return "peak hours";
        } else if (isNight(dateTime)) {
            return "night hours";
        } else {
            return "normal hours";
        }
    }
}
