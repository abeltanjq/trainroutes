package com.abeltan.trainroutes.graph;

import com.abeltan.trainroutes.station.AdjacencyMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StationGraphGeneratorTest {
    StationGraphGenerator sp = new StationGraphGenerator();

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

    @Test
    void thatPeakHour_EdgeWeights_AreCorrect() {
        assertEquals(12, sp.getPeakEdgeWeight().get("NS"));
        assertEquals(12, sp.getPeakEdgeWeight().get("NE"));
        assertEquals(10, sp.getPeakEdgeWeight().get("CC"));
        assertEquals(10, sp.getPeakEdgeWeight().get("DT"));
        assertEquals(10, sp.getPeakEdgeWeight().get("CG"));
        assertEquals(10, sp.getPeakEdgeWeight().get("EW"));
        assertEquals(15, sp.getPeakEdgeWeight().get("change"));
    }

    @Test
    void thatNightHour_EdgeWeights_AreCorrect() {
        assertEquals(10, sp.getNightEdgeWeight().get("NS"));
        assertEquals(10, sp.getNightEdgeWeight().get("CC"));
        assertEquals(10, sp.getNightEdgeWeight().get("EW"));
        assertEquals(8, sp.getNightEdgeWeight().get("TE"));
        assertEquals(null, sp.getNightEdgeWeight().get("DT"));
        assertEquals(null, sp.getNightEdgeWeight().get("CG"));
        assertEquals(null, sp.getNightEdgeWeight().get("CE"));
        assertEquals(10, sp.getNightEdgeWeight().get("change"));
    }

    @Test
    void thatNormalHour_EdgeWeights_AreCorrect() {
        assertEquals(10, sp.getNormalEdgeWeight().get("NS"));
        assertEquals(10, sp.getNormalEdgeWeight().get("EW"));
        assertEquals(10, sp.getNormalEdgeWeight().get("CG"));
        assertEquals(10, sp.getNormalEdgeWeight().get("CC"));
        assertEquals(8, sp.getNormalEdgeWeight().get("DT"));
        assertEquals(8, sp.getNormalEdgeWeight().get("TE"));
        assertEquals(10, sp.getNormalEdgeWeight().get("change"));
    }
}