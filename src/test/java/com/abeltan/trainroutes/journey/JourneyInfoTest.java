package com.abeltan.trainroutes.journey;

import com.abeltan.trainroutes.station.StationCode;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class JourneyInfoTest {

    @Test
    void testEquals() {
        JourneyInfo journeyInfo1 = new JourneyInfo("NS1", "NS2", 8,8, List.of(new StationCode("NS1"), new StationCode("NS2")), new ArrayList<>());
        JourneyInfo journeyInfo2 = new JourneyInfo("NS1", "NS2", 8,8, List.of(new StationCode("NS1"), new StationCode("NS2")), new ArrayList<>());
        assertTrue(journeyInfo1.equals(journeyInfo2));
    }

    @Test
    void testThat_StreamDistinct_RemovesDuplicates() {
        JourneyInfo journeyInfo1 = new JourneyInfo("NS1", "NS2", 8,8, List.of(new StationCode("NS1"), new StationCode("NS2")), new ArrayList<>());
        JourneyInfo journeyInfo2 = new JourneyInfo("NS1", "NS2", 8,8, List.of(new StationCode("NS1"), new StationCode("NS2")), new ArrayList<>());
        List<JourneyInfo> duplicates = new ArrayList<>();
        duplicates.add(journeyInfo1);
        duplicates.add(journeyInfo2);

        duplicates = duplicates
                .stream()
                .distinct()
                .collect(Collectors.toList());

        assertEquals(List.of(journeyInfo1), duplicates);
    }
}