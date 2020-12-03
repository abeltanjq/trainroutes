package com.abeltan.trainroutes.graph;

import com.abeltan.trainroutes.station.AdjacencyMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StationGraphGeneratorTest {
    StationGraphGenerator sp = new StationGraphGenerator();;

    @Test
    void numberOfTrainLinesIsEight() {
        assertEquals(8, sp.getNumberOfLines());
    }

    @Test
    void adjacentStationsOfParsedJsonIsCorrect() {
        StationGraphGenerator sp = new StationGraphGenerator();
        AdjacencyMap adjacencyMap = sp.getTrainStations();
        // Bedok -> Tanah Merah
        assertTrue(adjacencyMap.getAdjacencyOf("EW5").contains("EW4"));
        // Bedok -> Kembangan
        assertTrue(adjacencyMap.getAdjacencyOf("EW5").contains("EW6"));

        // Dhoby Ghaut -> Dhoby Ghaut
        assertTrue(adjacencyMap.getAdjacencyOf("NS24").contains("NS23"));
        assertTrue(adjacencyMap.getAdjacencyOf("NS24").contains("NS25"));
        assertTrue(adjacencyMap.getAdjacencyOf("NS24").contains("NE6"));
        assertTrue(adjacencyMap.getAdjacencyOf("NS24").contains("CC1"));
        assertTrue(adjacencyMap.getAdjacencyOf("NE6").contains("NE5"));
        assertTrue(adjacencyMap.getAdjacencyOf("NE6").contains("NE7"));
        assertTrue(adjacencyMap.getAdjacencyOf("NE6").contains("NS24"));
        assertTrue(adjacencyMap.getAdjacencyOf("NE6").contains("CC1"));
        assertTrue(adjacencyMap.getAdjacencyOf("CC1").contains("CC2"));
        assertTrue(adjacencyMap.getAdjacencyOf("CC1").contains("NS24"));
        assertTrue(adjacencyMap.getAdjacencyOf("CC1").contains("NE6"));
    }
}