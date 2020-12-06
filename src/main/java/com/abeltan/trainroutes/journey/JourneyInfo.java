package com.abeltan.trainroutes.journey;

import com.abeltan.trainroutes.station.StationCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
public class JourneyInfo {
    @Getter
    @Setter
    private String source;
    @Getter
    @Setter
    private String destination;
    @Getter
    @Setter
    private int weight;
    @Getter
    @Setter
    private int numberOfStationsTravelled;
    @Getter
    @Setter
    private List<StationCode> travelledStationCodes;
    @Getter
    @Setter
    private List<String> travelSteps;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 17;
        result = prime * result + source.hashCode();
        result = prime * result + destination.hashCode();
        result = prime * result + weight;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof JourneyInfo)) {
            return false;
        }
        return ((JourneyInfo) obj).travelledStationCodes.equals(this.travelledStationCodes) && ((JourneyInfo) obj).weight == this.weight;
    }
}
