package com.abeltan.trainroutes;

import com.abeltan.trainroutes.graph.StationGraphGenerator;
import com.abeltan.trainroutes.journey.JourneyPlanner;
import com.abeltan.trainroutes.station.AdjacencyMap;
import com.abeltan.trainroutes.station.StationCodes;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JourneyPlannerTest {
    StationGraphGenerator stationGraphGenerator = new StationGraphGenerator();
    AdjacencyMap trainStations = stationGraphGenerator.getTrainStations();
    StationCodes nameToCodes = stationGraphGenerator.getNameToCodes();
    Map<String, String> codeToName = stationGraphGenerator.getCodeToName();
    List<String> orderedStationList = stationGraphGenerator.getOrderedStationList();

    @Test
    void getRouteOfStraightGraph() {
        JourneyPlanner jp = new JourneyPlanner(trainStations, nameToCodes, codeToName,orderedStationList);
        List<String> route = jp.bfs("NE3", "NE6", trainStations);
        List<String> expectedRoute = new LinkedList<>();
        expectedRoute.add("NE3");
        expectedRoute.add("NE4");
        expectedRoute.add("NE5");
        expectedRoute.add("NE6");

        assertEquals(expectedRoute, route);
    }

    @Test
    void getRouteOfStationWithMultipleNeighbours() {
        JourneyPlanner jp = new JourneyPlanner(trainStations, nameToCodes, codeToName,orderedStationList);
        List<String> route = jp.bfs("NE5", "CC2", trainStations);
        List<String> expectedRoute = new LinkedList<>();
        expectedRoute.add("NE5");
        expectedRoute.add("NE6");
        expectedRoute.add("CC1");
        expectedRoute.add("CC2");

        assertEquals(expectedRoute, route);
    }

    @Test
    void getRouteFromHollandVillageToBugis() {
        JourneyPlanner jp = new JourneyPlanner(trainStations, nameToCodes, codeToName, orderedStationList);
        List<String> route = jp.bfs("CC21", "DT14", trainStations);
        List<String> expectedRoute = new LinkedList<>();
        expectedRoute.add("CC21");
        expectedRoute.add("CC20");
        expectedRoute.add("CC19");
        expectedRoute.add("DT9");
        expectedRoute.add("DT10");
        expectedRoute.add("DT11");
        expectedRoute.add("DT12");
        expectedRoute.add("DT13");
        expectedRoute.add("DT14");

        assertEquals(expectedRoute, route);
    }

    @Test
    void getTravellingRoutes() {
        List<String> route = new LinkedList<>();
        route.add("CC21");
        route.add("CC20");
        route.add("CC19");
        route.add("DT9");
        route.add("DT10");
        route.add("DT11");
        route.add("DT12");
        route.add("DT13");
        route.add("DT14");
        String expected = "Travel from Holland Village to Bugis\n" +
                          "Stations travelled: 8\n" +
                          "Route:\n" +
                          "CC21\n" +
                          "  |  Take CC line from Holland Village to Farrer Road\n" +
                          "CC20\n" +
                          "  |  Take CC line from Farrer Road to Botanic Gardens\n" +
                          "CC19\n" +
                          "  |  Change from CC line to DT line\n" +
                          "DT9\n" +
                          "  |  Take DT line from Botanic Gardens to Stevens\n" +
                          "DT10\n" +
                          "  |  Take DT line from Stevens to Newton\n" +
                          "DT11\n" +
                          "  |  Take DT line from Newton to Little India\n" +
                          "DT12\n" +
                          "  |  Take DT line from Little India to Rochor\n" +
                          "DT13\n" +
                          "  |  Take DT line from Rochor to Bugis\n" +
                          "DT14";
        JourneyPlanner jp = new JourneyPlanner(trainStations, nameToCodes, codeToName, orderedStationList);
        assertEquals(expected, jp.travellingStepsFor(route));
    }

    @Test
    void getRouteUsingStationNames() {
        JourneyPlanner jp = new JourneyPlanner(trainStations, nameToCodes, codeToName, orderedStationList);
        List<List<String>> route = jp.findRoutesBetween("Outram Park", "Dhoby Ghaut");
        List<String> expectedRoute = new LinkedList<>();
        expectedRoute.add("NE3");
        expectedRoute.add("NE4");
        expectedRoute.add("NE5");
        expectedRoute.add("NE6");

        assertEquals(expectedRoute, route.get(0));
    }
}