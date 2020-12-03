package com.abeltan.trainroutes.controller;

import com.abeltan.trainroutes.graph.StationGraphGenerator;
import com.abeltan.trainroutes.journey.JourneyClient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class PageController {

    private final JourneyClient journeyClient;

    @GetMapping(path="/")
    public String getSourceToDestinationPage(Model model) {
        model.addAttribute("stationList", journeyClient.getStationsNames());
        return "source-to-dest-selection";
    }

    @GetMapping("/routes")
    public String getRoute(
            @RequestParam String src,
            @RequestParam String dest,
            Model model
    ) {
        String source = StationGraphGenerator.removeStationCodesWithSquareBrackets(src);
        String destination = StationGraphGenerator.removeStationCodesWithSquareBrackets(dest);
        model.addAttribute("routeSuggestions", journeyClient.getRouteInstructionsBetween(source, destination));
        return "route-suggestion";
    }
}
