package com.theokont.flightaggregator.controller;

import java.util.List;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightDestination;
import com.theokont.flightaggregator.service.AmadeusApiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RankedFlightDestinationController {
    
    AmadeusApiService amadeusApiService;

    @Autowired
    public RankedFlightDestinationController(AmadeusApiService amadeusApiService) {
        this.amadeusApiService = amadeusApiService;
    }

    @GetMapping("/api/ranked-destinations/most-booked/{origin}")
    @CrossOrigin(origins = "http://localhost:3000")
    public FlightDestination[] getMostBookeDestinations(@PathVariable String origin) throws ResponseException {
        return amadeusApiService.getRankedFlightDestinations(origin).getMostBookedDestinations().toArray(new FlightDestination[0]);
    }

    @GetMapping("/api/ranked-destinations/other/{origin}")
    @CrossOrigin(origins = "http://localhost:3000")
    public List<FlightDestination> getOtherDestinations(@PathVariable String origin) throws ResponseException {
        return amadeusApiService.getRankedFlightDestinations(origin).getOtherDestinations();
    }

}
