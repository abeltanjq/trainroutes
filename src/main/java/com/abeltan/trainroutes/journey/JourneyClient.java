package com.abeltan.trainroutes.journey;

import com.abeltan.trainroutes.graph.StationGraphGenerator;

import java.util.ArrayList;
import java.util.List;

public class JourneyClient {
    private final JourneyPlanner journeyPlanner;
    public JourneyClient() {
        StationGraphGenerator sgg = new StationGraphGenerator();
        journeyPlanner = new JourneyPlanner(
                sgg.getTrainStations(),
                sgg.getNameToCodes(),
                sgg.getCodeToName(),
                sgg.getOrderedStationList());
    }

    public List<JourneyPlan> getRouteInstructionsBetween(String src, String dest) {
        List<JourneyPlan> journeyPlans = new ArrayList<>();
        List<List<String>> routes = journeyPlanner.findRoutesBetween(src, dest);
        if (src.equals(dest)) {
            journeyPlans.add(journeyPlanner.journeyPlanFor(routes.get(0)));
        } else {
            for (List<String> route: routes) {
                journeyPlans.add(journeyPlanner.journeyPlanFor(route));
            }
        }
        return journeyPlans;
    }

    public List<String> getStationsNames() {
        return journeyPlanner.getStationNames();
    }
}
