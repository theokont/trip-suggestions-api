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

    /**
     * Get the address from the given city code.
     *
     * @param cityCode The city code. Max length = 3.
     * @return Returns an Address object that contains the address details of the city.
     * @throws ResponseException A custom generic Amadeus error.
     */
    @GetMapping("/api/destination-address/{cityCode}")
    @CrossOrigin(origins = "http://localhost:3000")
    public Address getCityName(@PathVariable String cityCode) throws ResponseException {
        Address address =  amadeusApiService.getCityName(cityCode);
        if (address == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Location not found"
            );
        }

        return address;
    }

}
