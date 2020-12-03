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
    @Getter
    private AdjacencyMap stationCodeAdjMap;
    @Getter
    private StationCodes nameToCodes;
    @Getter
    private Map<String, String> codeToName;
    @Getter
    private List<String> orderedStationList;

    public StationGraphGenerator() {
        parseTrainLineTypes();
        parseTrainLines();
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
                String lowerLine = line.toLowerCase();
                JsonNode jsonNode = mapper.readTree(new ClassPathResource("static/" + lowerLine + ".json").getFile()).get(lowerLine);
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
