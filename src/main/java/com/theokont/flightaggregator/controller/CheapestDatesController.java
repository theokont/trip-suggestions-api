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

    @GetMapping("/api/cheapest-date-search/origin={origin}?destination={destination}")
    @CrossOrigin(origins = "http://localhost:3000")
    public FlightDate[] searchCheapestDates(@PathVariable String origin, @PathVariable String destination) throws ResponseException {
        return amadeusApiService.getCheapestDate(origin, destination);
    }

}


