package com.abeltan.trainroutes.journey;

import com.abeltan.trainroutes.graph.DistanceToV;
import com.abeltan.trainroutes.graph.StationGraphGenerator;
import com.abeltan.trainroutes.station.AdjacencyMap;
import com.abeltan.trainroutes.station.StationCode;
import com.abeltan.trainroutes.station.StationCodes;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class JourneyPlanner {
    @Getter private final AdjacencyMap stationAdjacents;
    @Getter private final StationCodes stationNameToCode;
    @Getter private final Map<String, String> stationCodeToName;
    @Getter private final List<String> orderedStationList;

    public static final String PEAK_HOUR = "peak hour";
    public static final String NIGHT_HOUR = "night hour";
    public static final String NORMAL_HOUR = "normal hour";
    public static final String NO_TIME_CONSIDERATION = "no time consideration";

    final int INFINITY = 999999999;

    public JourneyInfo dijkstra(String src, String dest, AdjacencyMap adjMap, Map<String, Integer> edgeWeight) {
        Queue<DistanceToV> pq = new PriorityQueue<>(new DistanceToV.DistanceToVComparator());
        Map<String, Integer> distanceTo = new HashMap<>();
        Map<String, String> previous = new HashMap<>();

        // initialize
        distanceTo.put(src, 0);
        pq.add(new DistanceToV(0, src));

        while(!pq.isEmpty()) {
            DistanceToV current = pq.poll();
            if (current.getDistance() == distanceTo.get(current.getVertex())) {
                List<String> neighbours = adjMap.getAdjacencyOf(current.getVertex());
                for (String neighbour: neighbours) {
                    // Ignore neighbour if the line is closed.
                     if (edgeWeight.get(StationCode.getStationLineFrom(neighbour)) != StationGraphGenerator.STATION_CLOSED) {
                         int distanceSrcToCurrent = distanceTo.get(current.getVertex()) == null ? INFINITY : distanceTo.get(current.getVertex());
                         int possibleDistanceToNext = distanceSrcToCurrent + edgeWeightBetween(current.getVertex(), neighbour, edgeWeight);
                         int distanceSrcToNeighbour = distanceTo.get(neighbour) == null ? INFINITY : distanceTo.get(neighbour);
                         if (distanceSrcToNeighbour > possibleDistanceToNext) {
                             distanceTo.put(neighbour, possibleDistanceToNext);
                             pq.add(new DistanceToV(possibleDistanceToNext, neighbour));
                             previous.put(neighbour, current.getVertex());
                         }
                     }
                }
            }
        }

        List<String> route = reconstructRouteFrom(previous, dest);
        int distanceToDestination = distanceTo.get(dest) == null ? 0 : distanceTo.get(dest);
        int stationsTravelled = route.isEmpty() ? 0 : route.size() - 1;
        return new JourneyInfo(stationCodeToName.get(src), stationCodeToName.get(dest), distanceToDestination, stationsTravelled, route, new ArrayList<>());
    }

    public int edgeWeightBetween(String src, String dest, Map<String, Integer> edgeWeight) {
        if (StationCode.isSameLine(src, dest)) {
            return edgeWeight.get(StationCode.getStationLineFrom(dest));
        } else {
            return edgeWeight.get("change");
        }
    }

    private List<String> reconstructRouteFrom(Map<String, String> previous, String dest) {
        List<String> route = new LinkedList<>();
        if (previous.get(dest) != null) {
            route.add(dest);
            String end = dest;
            while (previous.containsKey(end)) {
                String previousStation = previous.get(end);
                route.add(0, previousStation);
                end = previousStation;
            }
        }

        return route;
    }

    public JourneyInfo journeyPlanFor(JourneyInfo journeyInfo) {
        String sourceName = journeyInfo.getSource();
        String destinationName = journeyInfo.getDestination();
        List<String> journeyInstructions = new ArrayList<>();
        if (sourceName.equals(destinationName)) {
            journeyInstructions.add("You are already at " + destinationName);
            journeyInfo.setWeight(0);
        } else if (journeyInfo.getTravelledStationCodes().isEmpty()) {
            journeyInstructions.add("There is no route to " + destinationName);
            journeyInfo.setWeight(0);
        } else {
            String previous = null;
            for (String stationCode : journeyInfo.getTravelledStationCodes()) {
                if (previous != null) {
                    String beforeStationCode = new StationCode(previous).getLineCode();
                    String currentStationCode = new StationCode(stationCode).getLineCode();
                    if (beforeStationCode.equals(currentStationCode)) {
                        journeyInstructions.add("Take " + beforeStationCode + " line from " + stationCodeToName.get(previous) + " to " + stationCodeToName.get(stationCode));
                    } else {
                        journeyInstructions.add("Change from " + beforeStationCode + " line to " + currentStationCode + " line");
                    }
                }
                journeyInstructions.add(stationCode);
                previous = stationCode;
            }
        }
        journeyInfo.setTravelSteps(journeyInstructions);
        return journeyInfo;
    }

    // Params: Station names. eg "Dhoby Ghaut", "Kovan"
    // Returns a list of shortest routes between multiple sources and destination.
    public List<JourneyInfo> findRoutesBetween(String stationA, String stationB, Map<String, Integer> edgeWeight) {
        List<JourneyInfo> possibleRoutes = new ArrayList<>();
        List<StationCode> stationACode = stationNameToCode.getStationCodesFrom(stationA);
        List<StationCode> stationBCode = stationNameToCode.getStationCodesFrom(stationB);

        for (int src = 0; src < stationACode.size(); src++) {
            for (int dest = 0; dest < stationBCode.size(); dest++) {
                possibleRoutes.add(dijkstra(stationACode.get(src).toString(), stationBCode.get(dest).toString(), stationAdjacents, edgeWeight));
            }
        }

        // filter for shortest routes
        Integer lowestWeight = possibleRoutes
                .stream()
                .mapToInt(journey -> journey.getWeight())
                .min().orElseThrow(NoSuchElementException::new);

        List<JourneyInfo> shortestRoutes = possibleRoutes
                .stream()
                .filter(journey -> journey.getWeight() == lowestWeight)
                .collect(Collectors.toList());

        return shortestRoutes;
    }

    public List<String> getStationNames() {
        return orderedStationList;
    }
}