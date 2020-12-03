package com.abeltan.trainroutes.station;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AdjacencyMap {
    private Map<String, List<String>> adjacencyList;

    public AdjacencyMap() {
        adjacencyList = new HashMap<>();
    }

    private void applyAdjacency(String vertexA, String vertexB) {
        List<String> adjList;
        if (adjacencyList.containsKey(vertexA)) {
            adjList = adjacencyList.get(vertexA);
        } else {
            adjList = new LinkedList<>();
            adjacencyList.put(vertexA, adjList);
        }
        adjList.add(vertexB);
    }

    public void addAdjacent(String vertexA, String vertexB) {
        applyAdjacency(vertexA, vertexB);
        applyAdjacency(vertexB, vertexA);
    }

    public List<String> getAdjacencyOf(String vertex) {
        return adjacencyList.get(vertex);
    }
}
