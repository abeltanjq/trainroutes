package com.abeltan.trainroutes.journey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
public class JourneyPlan {
    @Getter
    @Setter
    private String source;
    @Getter
    @Setter
    private String destination;
    @Getter
    @Setter
    private int numberOfStations;
    @Getter
    @Setter
    private List<String> travelledStationCodes;
    @Getter
    @Setter
    private List<String> travelSteps;
}
