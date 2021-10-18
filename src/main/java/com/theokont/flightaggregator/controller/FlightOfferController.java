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

    /**
     * Get the cheapest flight recommendations and prices on a given journey.
     *
     * @param origin The origin location code.
     * @param destination The destination location code.
     * @param departureDate The departure date in YYYY-MM-DD format.
     * @param returnDate The return date in YYYY-MM-DD format.
     * @param adults The amount of adult passengers.
     * @param max The max number of results.
     * @return Returns an array of FlightOfferSearch objects that contain all the relevant information about the cheapest flight
     * @throws ResponseException A custom generic Amadeus error.
     */
    @GetMapping("/api/flight-offer-search")
    @CrossOrigin(origins = "http://localhost:3000")
    public FlightOfferSearch[] getFlightOffers(@RequestParam("origin") String origin, @RequestParam("destination") String destination, @RequestParam("departureDate") String departureDate,
            @RequestParam("returnDate") String returnDate, @RequestParam("adults") String adults, @RequestParam("max") String max) throws ResponseException {
        return amadeusApiService.getFlightOffers(origin, destination, departureDate, returnDate, adults, max);
    }

}
