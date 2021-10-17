package com.theokont.flightaggregator.controller;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.theokont.flightaggregator.service.AmadeusApiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FlightOfferController {
    
    AmadeusApiService amadeusApiService;

    @Autowired
    public FlightOfferController(AmadeusApiService amadeusApiService) {
        this.amadeusApiService = amadeusApiService;
    }

    // @GetMapping("/api/flight-offer-search?origin={origin}&destination={destination}")
    // public void getFlightOffers(@RequestParam("origin") String origin, @RequestParam("destination") String destination) throws ResponseException {
    //     System.out.println(origin);
    //     System.out.println(destination);
    // }

    @GetMapping("/api/flight-offer-search")
    @CrossOrigin(origins = "http://localhost:3000")
    public FlightOfferSearch[] getFlightOffers(@RequestParam("origin") String origin, @RequestParam("destination") String destination, @RequestParam("departureDate") String departureDate,
            @RequestParam("returnDate") String returnDate, @RequestParam("adults") String adults, @RequestParam("max") String max) throws ResponseException {
        return amadeusApiService.getFlightOffers(origin, destination, departureDate, returnDate, adults, max);
    }

}
