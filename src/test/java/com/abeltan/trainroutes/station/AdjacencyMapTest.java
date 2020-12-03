package com.abeltan.trainroutes.station;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdjacencyMapTest {

    @Test
    void addAdjacencyBetweenThreeStations() {
        AdjacencyMap ts = new AdjacencyMap();
        ts.addAdjacent("Habourfront", "Outram Park");
        ts.addAdjacent("Outram Park", "Chinatown");

        List<String> adjacents = ts.getAdjacencyOf("Outram Park");
        assertTrue(adjacents.contains("Habourfront"));
        assertTrue(adjacents.contains("Chinatown"));
    }
}