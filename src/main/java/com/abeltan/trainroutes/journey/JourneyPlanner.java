package com.abeltan.trainroutes.journey;

import com.abeltan.trainroutes.graph.DistanceToV;
import com.abeltan.trainroutes.station.AdjacencyMap;
import com.abeltan.trainroutes.station.StationCode;
import com.abeltan.trainroutes.station.StationCodes;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class JourneyPlanner {
    @Getter private final AdjacencyMap stationAdjacents;
    @Getter private final StationCodes stationNameToCode;
    @Getter private final Map<String, String> stationCodeToName;
    @Getter private final List<String> orderedStationList;
    private final TrainService trainService;

    final int INFINITY = 999999999;

    public JourneyInfo dijkstra(StationCode src, StationCode dest, LocalDateTime boardingTime) {
        Queue<DistanceToV> pq = new PriorityQueue<>(new DistanceToV.DistanceToVComparator());
        Map<String, Integer> distanceTo = new HashMap<>();
        Map<String, String> previous = new HashMap<>();

        // initialize
        distanceTo.put(src.toString(), 0);
        pq.add(new DistanceToV(0, src.toString()));

        while(!pq.isEmpty()) {
            DistanceToV current = pq.poll();
            if (current.getDistance() == distanceTo.get(current.getVertex())) {
                List<String> neighbours = stationAdjacents.getAdjacencyOf(current.getVertex());
                for (String neighbour: neighbours) {
                    // Ignore neighbour if the line is closed.
                    Integer travellingTime = trainService.getTravellingTimeOf(new StationCode(current.getVertex()), new StationCode(neighbour), boardingTime);
                     if (travellingTime != null) {
                         int distanceSrcToCurrent = distanceTo.get(current.getVertex()) == null ? INFINITY : distanceTo.get(current.getVertex());
                         int possibleDistanceToNext = distanceSrcToCurrent + travellingTime;
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

        // Adjustments to remove start and end if they are just changes between lines
        List<StationCode> route = reconstructRouteFrom(previous, dest);
        int distanceToDestination = distanceTo.get(dest.toString()) == null ? 0 : distanceTo.get(dest.toString());
        int stationsTravelled = route.isEmpty() ? 0 : route.size() - 1;

        // Remove line change at the beginning and end of journey
        if (route.size() > 1) {
            if (!StationCode.isSameLine(route.get(0), route.get(1))){
                distanceToDestination -= trainService.getTravellingTimeOf(route.get(0), route.get(1), boardingTime);
                stationsTravelled -= 1;
                route.remove(0);
            }
        }
        if (route.size() > 1) {
            if (!StationCode.isSameLine(route.get(route.size()-1), route.get(route.size()-2))){
                distanceToDestination -= trainService.getTravellingTimeOf(route.get(route.size()-2), route.get(route.size()-1), boardingTime);
                stationsTravelled -= 1;
                route.remove(route.size()-1);
            }
        }
        return new JourneyInfo(stationCodeToName.get(src.toString()), stationCodeToName.get(dest.toString()), distanceToDestination, stationsTravelled, route, new ArrayList<>());
    }

    private List<StationCode> reconstructRouteFrom(Map<String, String> previous, StationCode dest) {
        List<StationCode> route = new LinkedList<>();
        if (previous.get(dest.toString()) != null) {
            route.add(dest);
            String end = dest.toString();
            while (previous.containsKey(end)) {
                String previousStation = previous.get(end);
                route.add(0, new StationCode(previousStation));
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
            StationCode previous = null;
            for (StationCode current : journeyInfo.getTravelledStationCodes()) {
                if (previous != null) {
                    if (previous.getLineCode().equals(current.getLineCode())) {
                        journeyInstructions.add("Take " + previous.getLineCode() + " line from " + stationCodeToName.get(previous.toString()) + " to " + stationCodeToName.get(current.toString()));
                    } else {
                        journeyInstructions.add("Change from " + previous.getLineCode() + " line to " + current.getLineCode() + " line");
                    }
                }
                journeyInstructions.add(current.toString());
                previous = current;
            }
        }
        journeyInfo.setTravelSteps(journeyInstructions);
        return journeyInfo;
    }

    // Params: Station names. eg "Dhoby Ghaut", "Kovan"
    // Returns a list of shortest routes between multiple sources and destination.
    public List<JourneyInfo> findRoutesBetween(String stationA, String stationB, LocalDateTime dateTime) {
        List<JourneyInfo> possibleRoutes = new ArrayList<>();
        List<StationCode> stationACode = stationNameToCode.getStationCodesFrom(stationA);
        List<StationCode> stationBCode = stationNameToCode.getStationCodesFrom(stationB);

        for (int src = 0; src < stationACode.size(); src++) {
            for (int dest = 0; dest < stationBCode.size(); dest++) {
                possibleRoutes.add(dijkstra(stationACode.get(src), stationBCode.get(dest), dateTime));
            }
        }

        boolean hasAtLeastOneStationTravelled = false;
        for (JourneyInfo route: possibleRoutes) {
            if (route.getNumberOfStationsTravelled() > 0) {
                hasAtLeastOneStationTravelled = true;
                break;
            }
        }
        List<JourneyInfo> filteredJourneys;
        if (hasAtLeastOneStationTravelled) {
            filteredJourneys = possibleRoutes
                    .stream()
                    .filter(journey -> journey.getNumberOfStationsTravelled() >= 1)
                    .distinct()
                    .sorted(Comparator.comparingInt(JourneyInfo::getWeight))
                    .collect(Collectors.toList());
        } else {
            filteredJourneys = List.of(possibleRoutes.get(0));
        }

        return filteredJourneys;
    }

    public List<String> getStationNames() {
        return orderedStationList;
    }

    public static <T> Predicate<T> distinctByKey(
            Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}