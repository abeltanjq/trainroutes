package com.abeltan.trainroutes.journey;

import com.abeltan.trainroutes.graph.StationGraphGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JourneyClient {
    private final JourneyPlanner journeyPlanner;
    private final StationGraphGenerator sgg;

    public JourneyClient() {
        sgg = new StationGraphGenerator();
        journeyPlanner = new JourneyPlanner(
                sgg.getStationCodeAdjMap(),
                sgg.getNameToCodes(),
                sgg.getCodeToName(),
                sgg.getOrderedStationList(),
                new TrainService());
    }

    public List<JourneyInfo> getRouteInstructionsBetween(String src, String dest, LocalDateTime boadingTime) {
        List<JourneyInfo> journeyInfos = new ArrayList<>();
        List<JourneyInfo> routes = journeyPlanner.findRoutesBetween(src, dest, boadingTime);
        if (src.equals(dest)) {
            journeyInfos.add(journeyPlanner.journeyPlanFor(routes.get(0)));
        } else {
            for (JourneyInfo route : routes) {
                journeyInfos.add(journeyPlanner.journeyPlanFor(route));
            }
        }
        return journeyInfos;
    }

    public List<String> getStationsNames() {
        return journeyPlanner.getStationNames();
    }
}
