package com.theokont.flightaggregator.controller;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightDate;
import com.theokont.flightaggregator.service.AmadeusApiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheapestDatesController {
 
    AmadeusApiService amadeusApiService;

    @Autowired
    public CheapestDatesController(AmadeusApiService amadeusApiService) {
        this.amadeusApiService = amadeusApiService;
    }

    /**
     * Get the cheapest dates for a flight to a given city or airport.
     *
     * @param origin The origin city code. Maximum length = 3.
     * @param destination The destination city code. Maximum length = 3.
     * @return Returns an array of FlightDate objects.
     * @throws ResponseException A custom generic Amadeus error.
     */
    @GetMapping("/api/cheapest-date-search/origin={origin}?destination={destination}")
    @CrossOrigin(origins = "http://localhost:3000")
    public FlightDate[] searchCheapestDates(@PathVariable String origin, @PathVariable String destination) throws ResponseException {
        return amadeusApiService.getCheapestDate(origin, destination);
    }

}


