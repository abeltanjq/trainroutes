package com.abeltan.trainroutes.graph;

import com.abeltan.trainroutes.station.AdjacencyMap;
import com.abeltan.trainroutes.station.StationCode;
import com.abeltan.trainroutes.station.StationCodes;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.*;

public class StationGraphGenerator {
    List<String> trainLines;
    @Getter private AdjacencyMap stationCodeAdjMap;
    @Getter private StationCodes nameToCodes;
    @Getter private Map<String, String> codeToName;
    @Getter private List<String> orderedStationList;
    // Key: Line Code (eg. CC, NS), Value: mins between station in that line.
    @Getter private Map<String, Integer> peakEdgeWeight; // (6am-9am and 6pm-9pm on Mon-Fri)
    @Getter private Map<String, Integer> nightEdgeWeight; // (10pm-6am on Mon-Sun)
    @Getter private Map<String, Integer> normalEdgeWeight; // all other times
    @Getter private Map<String, Integer> uniformEdgeWeight;

    public StationGraphGenerator() {
        parseTrainLineTypes();
        parseTrainLines();
        processEdgeWeights();
    }

    private void processEdgeWeights() {
        processPeakHour();
        processNightHour();
        processNormalHour();
        processUniformEdges();
    }

    private void processUniformEdges() {
        uniformEdgeWeight = new HashMap<>();
        for (String line : trainLines) {
            uniformEdgeWeight.put(line, 1);
        }
        uniformEdgeWeight.put("change", 1);
    }

    private void processNormalHour() {
        normalEdgeWeight = new HashMap<>();
        normalEdgeWeight.put("change", 10);
        List<String> fastLines = List.of("DT", "TE");
        for (String fLines : fastLines) {
            normalEdgeWeight.put(fLines, 8);
        }

        for (String line : trainLines) {
            if (!fastLines.contains(line)) {
                normalEdgeWeight.put(line, 10);
            }
        }
    }

    private void processNightHour() {
        nightEdgeWeight = new HashMap<>();
        List<String> closedStations = List.of("DT", "CG", "CE");
        for (String station : closedStations) {
            nightEdgeWeight.put(station, null); // null to make the vertex "unreachable"
        }

        for (String line : trainLines) {
            if (!closedStations.contains(line)) {
                nightEdgeWeight.put(line, 10);
            }
        }

        nightEdgeWeight.put("TE", 8);
        nightEdgeWeight.put("change", 10);
    }

    private void processPeakHour() {
        peakEdgeWeight = new HashMap<>();
        peakEdgeWeight.put("change", 15);

        List<String> slowLines = List.of("NS", "NE");
        for (String sLine : slowLines) {
            peakEdgeWeight.put(sLine, 12);
        }

        for (String line : trainLines) {
            if (!slowLines.contains(line)) {
                peakEdgeWeight.put(line, 10);
            }
        }
    }


    private void parseTrainLineTypes() {
        trainLines = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(new ClassPathResource("static/lines.json").getFile()).get("lines");
            if (jsonNode.isArray()) {
                for (JsonNode node : jsonNode) {
                    trainLines.add(node.asText());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseTrainLines() {
        stationCodeAdjMap = new AdjacencyMap();
        nameToCodes = new StationCodes();
        codeToName = new HashMap<>();
        orderedStationList = new ArrayList<>();

        Iterator iterator = trainLines.iterator();
        ObjectMapper mapper = new ObjectMapper();
        while (iterator.hasNext()) {
            String line = (String) iterator.next();
            try {
                JsonNode jsonNode = mapper.readTree(new ClassPathResource("static/" + line + ".json").getFile()).get(line);
                JsonNode previous = null;
                if (jsonNode.isArray()) {
                    for (JsonNode node : jsonNode) {
                        String stationName = node.get("name").asText();
                        String stationCode = node.get("code").asText();

                        boolean isInterchange = nameToCodes.getStationCodesFrom(stationName) != null;
                        if (isInterchange) {
                            List<StationCode> codes = nameToCodes.getStationCodesFrom(stationName);
                            for (StationCode code : codes) {
                                stationCodeAdjMap.addAdjacent(stationCode, code.toString());
                            }
                        }

                        nameToCodes.add(stationName, stationCode);
                        codeToName.put(stationCode, stationName);
                        orderedStationList.add("[" + stationCode + "] " + stationName);
                        if (previous != null) {
                            stationCodeAdjMap.addAdjacent(previous.get("code").asText(), stationCode);
                        }
                        previous = node;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getNumberOfLines() {
        return trainLines.size();
    }

    public static String removeStationCodesWithSquareBrackets(String stationCodeWithSquareBrackets) {
        return stationCodeWithSquareBrackets.replaceAll("^(\\[\\D{2}\\d{1,2}\\]\\s){1}", "");
    }
}
