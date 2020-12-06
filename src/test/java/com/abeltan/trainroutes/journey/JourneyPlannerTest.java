package com.abeltan.trainroutes.journey;

import com.abeltan.trainroutes.graph.StationGraphGenerator;
import com.abeltan.trainroutes.station.AdjacencyMap;
import com.abeltan.trainroutes.station.StationCodes;
import com.abeltan.trainroutes.station.StationCode;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JourneyPlannerTest {
    StationGraphGenerator stationGraphGenerator = new StationGraphGenerator();
    AdjacencyMap trainStations = stationGraphGenerator.getStationCodeAdjMap();
    StationCodes nameToCodes = stationGraphGenerator.getNameToCodes();
    Map<String, String> codeToName = stationGraphGenerator.getCodeToName();
    List<String> orderedStationList = stationGraphGenerator.getOrderedStationList();
    TrainService trainService = new TrainService();
    @Test
    void getRouteOfStraightGraph() {
        JourneyPlanner jp = new JourneyPlanner(trainStations, nameToCodes, codeToName, orderedStationList, trainService);
        JourneyInfo route = jp.dijkstra(new StationCode("NE3"), new StationCode("NE6"), null);
        List<StationCode> expectedRoute = new LinkedList<>();
        expectedRoute.add(new StationCode("NE3"));
        expectedRoute.add(new StationCode("NE4"));
        expectedRoute.add(new StationCode("NE5"));
        expectedRoute.add(new StationCode("NE6"));

        assertEquals(expectedRoute, route.getTravelledStationCodes());
    }

    @Test
    void getRouteOfStation_WithMultipleNeighbours() {
        JourneyPlanner jp = new JourneyPlanner(trainStations, nameToCodes, codeToName, orderedStationList, trainService);
        JourneyInfo route = jp.dijkstra(new StationCode("NE5"), new StationCode("CC2"), null);
        List<StationCode> expectedRoute = new LinkedList<>();
        expectedRoute.add(new StationCode("NE5"));
        expectedRoute.add(new StationCode("NE6"));
        expectedRoute.add(new StationCode("CC1"));
        expectedRoute.add(new StationCode("CC2"));

        assertEquals(expectedRoute, route.getTravelledStationCodes());
    }

    @Test
    void getRouteFrom_HollandVillageToBugis_WithBFS() {
        JourneyPlanner jp = new JourneyPlanner(trainStations, nameToCodes, codeToName, orderedStationList, trainService);
        JourneyInfo route = jp.dijkstra(new StationCode("CC21"), new StationCode("DT14"), null);
        List<StationCode> expectedRoute = new LinkedList<>();
        expectedRoute.add(new StationCode("CC21"));
        expectedRoute.add(new StationCode("CC20"));
        expectedRoute.add(new StationCode("CC19"));
        expectedRoute.add(new StationCode("DT9"));
        expectedRoute.add(new StationCode("DT10"));
        expectedRoute.add(new StationCode("DT11"));
        expectedRoute.add(new StationCode("DT12"));
        expectedRoute.add(new StationCode("DT13"));
        expectedRoute.add(new StationCode("DT14"));

        assertEquals(expectedRoute, route.getTravelledStationCodes());
    }

    @Test
    void thatThere_ShouldBe_NoRoute_WhenStations_AreClosed() {
        JourneyPlanner jp = new JourneyPlanner(trainStations, nameToCodes, codeToName, orderedStationList, trainService);
        LocalDateTime saturdayNight = LocalDateTime.parse("2020-11-07T22:00");
        JourneyInfo route = jp.dijkstra(new StationCode("DT5"), new StationCode("DT14"), saturdayNight);
        List<String> expectedRoute = new LinkedList<>();
        assertEquals(expectedRoute, route.getTravelledStationCodes());
    }

    @Test
    void thatThere_ShouldBe_NoRoute_WhenLaterStations_AreClosed() {
        JourneyPlanner jp = new JourneyPlanner(trainStations, nameToCodes, codeToName, orderedStationList, trainService);
        LocalDateTime saturdayNight = LocalDateTime.parse("2020-11-07T22:00");
        JourneyInfo route = jp.dijkstra(new StationCode("NS1"), new StationCode("DT4"), saturdayNight);
        List<String> expectedRoute = new LinkedList<>();
        assertEquals(expectedRoute, route.getTravelledStationCodes());
    }

    @Test
    void getRouteFrom_HollandVillageToBugis_WithDijkstra() {
        JourneyPlanner jp = new JourneyPlanner(trainStations, nameToCodes, codeToName, orderedStationList, trainService);
        JourneyInfo route = jp.dijkstra(new StationCode("CC21"), new StationCode("DT14"), null);
        List<StationCode> expectedRoute = new LinkedList<>();
        expectedRoute.add(new StationCode("CC21"));
        expectedRoute.add(new StationCode("CC20"));
        expectedRoute.add(new StationCode("CC19"));
        expectedRoute.add(new StationCode("DT9"));
        expectedRoute.add(new StationCode("DT10"));
        expectedRoute.add(new StationCode("DT11"));
        expectedRoute.add(new StationCode("DT12"));
        expectedRoute.add(new StationCode("DT13"));
        expectedRoute.add(new StationCode("DT14"));

        assertEquals(expectedRoute, route.getTravelledStationCodes());
    }

    @Test
    void testThatJourneyPlanIsCorrect() {
        List<StationCode> route = new LinkedList<>();
        StationCode src = new StationCode("CC21");
        StationCode dest = new StationCode("DT14");
        route.add(src);
        route.add(new StationCode("CC20"));
        route.add(new StationCode("CC19"));
        route.add(new StationCode("DT9"));
        route.add(new StationCode("DT10"));
        route.add(new StationCode("DT11"));
        route.add(new StationCode("DT12"));
        route.add(new StationCode("DT13"));
        route.add(dest);
        List<String> expectedTravelSteps = new ArrayList<>();
        expectedTravelSteps.add("CC21");
        expectedTravelSteps.add("Take CC line from Holland Village to Farrer Road");
        expectedTravelSteps.add("CC20");
        expectedTravelSteps.add("Take CC line from Farrer Road to Botanic Gardens");
        expectedTravelSteps.add("CC19");
        expectedTravelSteps.add("Change from CC line to DT line");
        expectedTravelSteps.add("DT9");
        expectedTravelSteps.add("Take DT line from Botanic Gardens to Stevens");
        expectedTravelSteps.add("DT10");
        expectedTravelSteps.add("Take DT line from Stevens to Newton");
        expectedTravelSteps.add("DT11");
        expectedTravelSteps.add("Take DT line from Newton to Little India");
        expectedTravelSteps.add("DT12");
        expectedTravelSteps.add("Take DT line from Little India to Rochor");
        expectedTravelSteps.add("DT13");
        expectedTravelSteps.add("Take DT line from Rochor to Bugis");
        expectedTravelSteps.add("DT14");
        JourneyPlanner jp = new JourneyPlanner(trainStations, nameToCodes, codeToName, orderedStationList, trainService);
        assertEquals(expectedTravelSteps, jp.journeyPlanFor(new JourneyInfo("Holland Village", "Bugis", 8, 8, route, new ArrayList<>())).getTravelSteps());
    }

    @Test
    void testThatEmptyRoute_IsHandled_Correctly() {
        List<StationCode> route = new LinkedList<>();
        String src = "CC21";
        String dest = "DT14";
        List<String> expectedTravelSteps = new ArrayList<>();
        expectedTravelSteps.add("There is no route to " + codeToName.get(dest));
        JourneyPlanner jp = new JourneyPlanner(trainStations, nameToCodes, codeToName, orderedStationList, trainService);
        assertEquals(expectedTravelSteps, jp.journeyPlanFor(new JourneyInfo(codeToName.get(src), codeToName.get(dest), 0, 0, route, new ArrayList<>())).getTravelSteps());
    }

    @Test
    void getRouteUsingStationNames() {
        JourneyPlanner jp = new JourneyPlanner(trainStations, nameToCodes, codeToName, orderedStationList, trainService);
        List<JourneyInfo> route = jp.findRoutesBetween("Outram Park", "Dhoby Ghaut", null);
        List<StationCode> expectedRoute = new LinkedList<>();
        expectedRoute.add(new StationCode("NE3"));
        expectedRoute.add(new StationCode("NE4"));
        expectedRoute.add(new StationCode("NE5"));
        expectedRoute.add(new StationCode("NE6"));

        assertEquals(expectedRoute, route.get(0).getTravelledStationCodes());
    }

    @Test
    void getRoute_RafflesPlace_AndCityHall() {
        JourneyPlanner jp = new JourneyPlanner(trainStations, nameToCodes, codeToName, orderedStationList, trainService);
        List<JourneyInfo> route = jp.findRoutesBetween("Raffles Place", "City Hall", null);
        List<StationCode> expectedRoute1 = new LinkedList<>();
        expectedRoute1.add(new StationCode("EW14"));
        expectedRoute1.add(new StationCode("EW13"));

        List<StationCode> expectedRoute2 = new LinkedList<>();
        expectedRoute2.add(new StationCode("NS26"));
        expectedRoute2.add(new StationCode("NS25"));

        assertEquals(expectedRoute2, route.get(0).getTravelledStationCodes());
        assertEquals(expectedRoute1, route.get(1).getTravelledStationCodes());
    }

    @Test
    void testThat_JourneyPlan_ForSrcAndDestBeingTheSame_IsCorrect() {
        List<StationCode> route = new LinkedList<>();
        String src = "CC21";
        String dest = "CC21";
        List<String> expectedTravelSteps = new ArrayList<>();
        expectedTravelSteps.add("You are already at " + codeToName.get(dest));
        JourneyPlanner jp = new JourneyPlanner(trainStations, nameToCodes, codeToName, orderedStationList, trainService);
        assertEquals(expectedTravelSteps, jp.journeyPlanFor(new JourneyInfo(codeToName.get(src), codeToName.get(dest), 0, 0, route, new ArrayList<>())).getTravelSteps());
    }

    @Test
    void testThat_JourneyPlan_ForStationsThatAre_UnreachableIsCorrect() {
        List<StationCode> route = new LinkedList<>();
        String src = "CC21";
        String dest = "DT14";
        List<String> expectedTravelSteps = new ArrayList<>();
        expectedTravelSteps.add("There is no route to " + codeToName.get("DT14"));
        JourneyPlanner jp = new JourneyPlanner(trainStations, nameToCodes, codeToName, orderedStationList, trainService);
        assertEquals(expectedTravelSteps, jp.journeyPlanFor(new JourneyInfo(codeToName.get(src), codeToName.get(dest), 0, 0, route, new ArrayList<>())).getTravelSteps());
    }

//    @Test
//    void thatReconstructedRoutes_DoNotHave_LineChanges_AtTheBeginning_AndEnd() {
//        Map<String, String> previous = new HashMap<>();
//        previous.put("NS1", "EW1");
//        previous.put("EW1", "DT1");
//        List<StationCode> onlyEW1 = JourneyPlanner.reconstructRouteFrom(previous, new StationCode("NS1"));
//
//        assertEquals(List.of(new StationCode("EW1")), onlyEW1);
//    }

    @Test
    void thatReconstructedRoutes_AreDistinct() {

    }
}