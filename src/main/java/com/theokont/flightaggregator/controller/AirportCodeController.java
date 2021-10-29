package com.theokont.flightaggregator.controller;

import com.amadeus.exceptions.ResponseException;
import com.theokont.flightaggregator.service.AmadeusApiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AirportCodeController {
 
    AmadeusApiService amadeusApiService;

    @Autowired
    public AirportCodeController(AmadeusApiService amadeusApiService) {
        this.amadeusApiService = amadeusApiService;
    }
    
    @GetMapping("/api/destination-airport-code/{cityName}")
    @CrossOrigin(origins = "http://localhost:3000")
    public String getFlightOffers(@PathVariable String cityName) throws ResponseException {
        return amadeusApiService.getAirportCode(cityName);
    }
}
