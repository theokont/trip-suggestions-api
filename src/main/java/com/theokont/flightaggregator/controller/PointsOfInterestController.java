package com.theokont.flightaggregator.controller;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.PointOfInterest;
import com.amadeus.resources.Location.GeoCode;
import com.theokont.flightaggregator.service.AmadeusApiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PointsOfInterestController {
    
    AmadeusApiService amadeusApiService;

    @Autowired
    public PointsOfInterestController(AmadeusApiService amadeusApiService) {
        this.amadeusApiService = amadeusApiService;
    }

    /**
     * Gets the points of interest for a given destination.
     * @param destination The city name.
     * @param countryCode The country code of the city name.
     * @return Returns an array of PointOfInterest objects.
     * @throws ResponseException
     */
    @GetMapping("/api/points-of-interest")
    @CrossOrigin(origins = "http://localhost:3000")
    public PointOfInterest[] getPointsOfInterest(@RequestParam("destination") String destination, @RequestParam("countryCode") String countryCode) throws ResponseException {
        GeoCode geoCode = amadeusApiService.getCityGeoCode(destination);
        
        // Amadeus API has a rate limit in the test environment of no more than 1 request every 100ms
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        PointOfInterest[] pois = amadeusApiService.getPointsOfInterest(geoCode.getLatitude(), geoCode.getLongitude());

        return pois;
    }

}
