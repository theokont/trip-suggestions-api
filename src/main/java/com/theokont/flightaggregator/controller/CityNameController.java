package com.theokont.flightaggregator.controller;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Location.Address;
import com.theokont.flightaggregator.service.AmadeusApiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class CityNameController {
    
    AmadeusApiService amadeusApiService;

    @Autowired
    public CityNameController(AmadeusApiService amadeusApiService) {
        this.amadeusApiService = amadeusApiService;
    }

    @GetMapping("/api/destination-address/{city}")
    @CrossOrigin(origins = "http://localhost:3000")
    public Address getCityName(@PathVariable String city) throws ResponseException {
        Address address =  amadeusApiService.getCityName(city);
        if (address == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Location not found"
            );
        }

        return address;
    }

}
