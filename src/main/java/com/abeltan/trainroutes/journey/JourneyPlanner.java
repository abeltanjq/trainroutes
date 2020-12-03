package com.abeltan.trainroutes.journey;

import com.abeltan.trainroutes.station.AdjacencyMap;
import com.abeltan.trainroutes.station.StationCode;
import com.abeltan.trainroutes.station.StationCodes;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class JourneyPlanner {
    private final AdjacencyMap stationAdjacents;
    private final StationCodes stationNameToCode;
    private final Map<String, String> stationCodeToName;
    private final List<String> orderedStationList;

    // returns: the route to dest station in a list of station codes.
    // Implementation of Breath First Search
    public List<String> bfs(String src, String dest, AdjacencyMap adjMap) {
        Map<String, Boolean> visited = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        boolean destReached = false;

        Map<String, String> previous = new HashMap<>();
        visited.put(src, true);
        queue.add(src);

        while (queue.size() != 0 || destReached) {
            String currentStation = queue.poll();
            if (currentStation.equals(dest)) {
                destReached = true;
                break;
            }

            Iterator<String> iterator = adjMap.getAdjacencyOf(currentStation).iterator();
            while (iterator.hasNext()) {
                String adjacentStation = iterator.next();
                if (visited.get(adjacentStation) == null) {
                    visited.put(adjacentStation, true);
                    previous.put(adjacentStation, currentStation);
                    queue.add(adjacentStation);
                }
            }
        }

        if (!destReached) {
            return null;
        } else {
            return reconstructRouteFrom(previous, dest);
        }
    }

    private List<String> reconstructRouteFrom(Map<String, String> previous, String dest) {
        List<String> route = new LinkedList<>();
        route.add(dest);
        String end = dest;
        while (previous.containsKey(end)) {
            String previousStation = previous.get(end);
            route.add(0, previousStation);
            end = previousStation;
        }
        return route;
    }

    public JourneyPlan journeyPlanFor(List<String> routesInStationCodes) {
        String start = stationCodeToName.get(routesInStationCodes.get(0));
        String end = stationCodeToName.get(routesInStationCodes.get(routesInStationCodes.size() - 1));
        int numOfStations = (int) routesInStationCodes.stream().map(stationCodeToName::get).distinct().count();

        JourneyPlan journeyPlan;
        List<String> journeyInstructions = new ArrayList<>();

        if (start.equals(end)) {
            journeyInstructions.add("You are already at " + end);
            journeyPlan = new JourneyPlan(start, end, numOfStations, routesInStationCodes, journeyInstructions);
            return journeyPlan;
        }

        String previous = null;
        for (String route : routesInStationCodes) {
            if (previous != null) {
                String beforeStationCode = new StationCode(previous).getLineCode();
                String currentStationCode = new StationCode(route).getLineCode();
                if (beforeStationCode.equals(currentStationCode)) {
                    journeyInstructions.add("Take " + beforeStationCode + " line from " + stationCodeToName.get(previous) + " to " + stationCodeToName.get(route));
                } else {
                    journeyInstructions.add("Change from " + beforeStationCode + " line to " + currentStationCode + " line");
                }
            }
            journeyInstructions.add(route);
            previous = route;
        }
        journeyPlan = new JourneyPlan(start, end, numOfStations, routesInStationCodes, journeyInstructions);
        return journeyPlan;
    }

    // Params: Station names. eg "Dhoby Ghaut", "Kovan"
    // Returns a list of shortest routes between multiple sources and destination.
    public List<List<String>> findRoutesBetween(String stationA, String stationB) {
        List<List<String>> possibleRoutes = new ArrayList<>();
        List<StationCode> stationACode = stationNameToCode.getStationCodesFrom(stationA);
        List<StationCode> stationBCode = stationNameToCode.getStationCodesFrom(stationB);

        for (int src = 0; src < stationACode.size(); src++) {
            for (int dest = 0; dest < stationBCode.size(); dest++) {
                possibleRoutes.add(bfs(stationACode.get(src).toString(), stationBCode.get(dest).toString(), stationAdjacents));
            }
        }

        // filter for shortest routes
        Integer fewestStation = possibleRoutes
                .stream()
                .mapToInt(list -> list.size())
                .min().orElseThrow(NoSuchElementException::new);

        List<List<String>> shortestRoutes = possibleRoutes
                .stream()
                .filter(list -> list.size() == fewestStation)
                .collect(Collectors.toList());

        return shortestRoutes;
    }

    public List<String> getStationNames() {
        return orderedStationList;
    }
}