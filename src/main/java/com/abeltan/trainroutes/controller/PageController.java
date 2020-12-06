package com.abeltan.trainroutes.controller;

import com.abeltan.trainroutes.graph.StationGraphGenerator;
import com.abeltan.trainroutes.journey.JourneyClient;
import com.abeltan.trainroutes.journey.TrainService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class PageController {

    private final JourneyClient journeyClient;

    @GetMapping(path = "/")
    public String getSourceToDestinationPage(Model model) {
        model.addAttribute("stationList", journeyClient.getStationsNames());
        return "source-to-dest-selection";
    }

    @GetMapping(path = "/no-time-cost")
    public String getSourceToDestinationPageWithNoTimeCost(Model model) {
        model.addAttribute("stationList", journeyClient.getStationsNames());
        return "source-to-dest-selection-no-time-cost";
    }

    @GetMapping("/routes")
    public String getRoute(
            @RequestParam String src,
            @RequestParam String dest,
            @RequestParam String date,
            @RequestParam String hour,
            @RequestParam String min,
            Model model
    ) {
        String source = StationGraphGenerator.removeStationCodesWithSquareBrackets(src);
        String destination = StationGraphGenerator.removeStationCodesWithSquareBrackets(dest);
        LocalDateTime boardingTime = LocalDateTime.parse(TrainService.formatDateTime(date, hour, min));
        model.addAttribute("timeOfDay", TrainService.getTypeOfService(boardingTime));
        model.addAttribute("routeSuggestions", journeyClient.getRouteInstructionsBetween(source, destination, boardingTime));
        return "route-suggestion";
    }

    @GetMapping("/no-time-cost-routes")
    public String getRoute(
            @RequestParam String src,
            @RequestParam String dest,
            Model model
    ) {
        model.addAttribute("timeOfDay", "no time consideration");
        String source = StationGraphGenerator.removeStationCodesWithSquareBrackets(src);
        String destination = StationGraphGenerator.removeStationCodesWithSquareBrackets(dest);
        model.addAttribute("routeSuggestions", journeyClient.getRouteInstructionsBetween(source, destination, null));
        return "route-suggestion";
    }
}
