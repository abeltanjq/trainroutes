package com.abeltan.trainroutes.graph;

import com.abeltan.trainroutes.station.AdjacencyMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StationGraphGeneratorTest {
    StationGraphGenerator sp = new StationGraphGenerator();
    ;

    @Test
    void thatNumberOfTrainLines_IsEight() {
        assertEquals(8, sp.getNumberOfLines());
    }

    @Test
    void thatAdjacentStations_OfParsedJson_IsCorrect() {
        StationGraphGenerator sp = new StationGraphGenerator();
        AdjacencyMap adjacencyMap = sp.getStationCodeAdjMap();
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

    @Test
    void thatRegesRemoves_StationCodes_SurroundedBy_SquareBrackets() {
        String ns1 = "[NS1] Jurong East";
        String ns12 = "[NS12] Canberra";
        assertEquals("Jurong East", StationGraphGenerator.removeStationCodesWithSquareBrackets(ns1));
        assertEquals("Canberra", StationGraphGenerator.removeStationCodesWithSquareBrackets(ns12));
    }
}