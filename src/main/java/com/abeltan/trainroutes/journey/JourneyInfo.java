package com.abeltan.trainroutes.journey;

import com.abeltan.trainroutes.station.StationCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
public class JourneyInfo {
    @Getter
    @Setter
    private String source;
    @Getter
    @Setter
    private String destination;
    @Getter
    @Setter
    private int weight;
    @Getter
    @Setter
    private int numberOfStationsTravelled;
    @Getter
    @Setter
    private List<StationCode> travelledStationCodes;
    @Getter
    @Setter
    private List<String> travelSteps;
}
