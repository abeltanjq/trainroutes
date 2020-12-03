package com.abeltan.trainroutes.graph;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;

@AllArgsConstructor
public class DistanceToV {
    // Distance between source and dest vertex
    @Getter private int distance;
    // Destination vertex
    @Getter private String vertex;

    public static class DistanceToVComparator implements Comparator<DistanceToV> {
        @Override
        public int compare(DistanceToV d1, DistanceToV d2) {
            return d1.getDistance() < d2.getDistance() ? -1 : 1;
        }
    }
}
