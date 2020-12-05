package com.abeltan.trainroutes.journey;

import com.abeltan.trainroutes.graph.StationGraphGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.abeltan.trainroutes.journey.JourneyPlanner.*;

public class JourneyClient {
    private final JourneyPlanner journeyPlanner;
    private final StationGraphGenerator sgg;

    public JourneyClient() {
        sgg = new StationGraphGenerator();
        journeyPlanner = new JourneyPlanner(
                sgg.getStationCodeAdjMap(),
                sgg.getNameToCodes(),
                sgg.getCodeToName(),
                sgg.getOrderedStationList());
    }

    public List<JourneyInfo> getRouteInstructionsBetween(String src, String dest, String typeOfHour) {
        List<JourneyInfo> journeyInfos = new ArrayList<>();
        List<JourneyInfo> routes = journeyPlanner.findRoutesBetween(src, dest, getEdgeWeight(typeOfHour));
        if (src.equals(dest)) {
            journeyInfos.add(journeyPlanner.journeyPlanFor(routes.get(0)));
        } else {
            for (JourneyInfo route : routes) {
                journeyInfos.add(journeyPlanner.journeyPlanFor(route));
            }
        }
        return journeyInfos;
    }

    public Map<String, Integer> getEdgeWeight(String hour) {
        switch(hour) {
            case PEAK_HOUR:
                return sgg.getPeakEdgeWeight();
            case NIGHT_HOUR:
                return sgg.getNightEdgeWeight();
            case NORMAL_HOUR:
                return sgg.getNormalEdgeWeight();
            default:
                return sgg.getUniformEdgeWeight();
        }
    }

    public List<String> getStationsNames() {
        return journeyPlanner.getStationNames();
    }
}
