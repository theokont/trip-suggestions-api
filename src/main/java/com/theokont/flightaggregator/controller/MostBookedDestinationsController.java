package com.theokont.flightaggregator.controller;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.AirTraffic;
import com.theokont.flightaggregator.service.AmadeusApiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MostBookedDestinationsController {
    
    AmadeusApiService amadeusApiService;

    @Autowired
    public MostBookedDestinationsController(AmadeusApiService amadeusApiService) {
        this.amadeusApiService = amadeusApiService;
    }
    /**
     * Gets the most booked destinations for the previous month.
     * 
     * @param origin The origin from which to look for destinations 
     * @return A list of the most booked destinations.
     * Returns:
     */
    @GetMapping("/api/most-booked-destinations/{origin}")
    @CrossOrigin(origins = "http://localhost:3000")
    public AirTraffic[] getMostBookedDestinations(@PathVariable String origin) throws ResponseException {
        return amadeusApiService.getMostBookedDestinations(origin, "2017-01");
    }
}
