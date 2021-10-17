package com.theokont.flightaggregator.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.PointOfInterest;
import com.amadeus.resources.AirTraffic;
import com.amadeus.resources.FlightDate;
import com.amadeus.resources.FlightDestination;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.Location;
import com.amadeus.resources.Location.Address;
import com.amadeus.resources.Location.GeoCode;
import com.theokont.flightaggregator.model.RankedDestinationPair;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class AmadeusApiService {
    
    Amadeus amadeus;

    public AmadeusApiService() {
        amadeus = Amadeus
            .builder(System.getenv())
            .build();
    }

    public AirTraffic[] getMostBookedDestinations(String originCityCode, String period) throws ResponseException {
        // TODO: get airport id
        AirTraffic[] mostBookedDestinations = amadeus.travel.analytics.airTraffic.booked.get(Params
            .with("originCityCode", getAirportName(originCityCode))
            .and("period", period));
        
        return mostBookedDestinations;
    }

    public AirTraffic[] getMostBookedDestinations(String originCityCode) throws ResponseException {
        DateTimeFormatter yearMonthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        AirTraffic[] mostBookedDestinations = amadeus.travel.analytics.airTraffic.booked.get(Params
            .with("originCityCode", getAirportName(originCityCode))
            .and("period", LocalDate.now().minusYears(4).format(yearMonthFormatter)));

        // TODO: Make wrapper class and set destination name here
        
        return mostBookedDestinations;
    }
    
    // Flight Offers Search v2 GET
    public FlightOfferSearch[] getFlightOffers(String origin, String destination, String departureDate, String returnDate, String adultsNum, String max) throws ResponseException {
        FlightOfferSearch[] flightOffersSearches = amadeus.shopping.flightOffersSearch.get(
            Params.with("originLocationCode", origin)
                    .and("destinationLocationCode", destination)
                    .and("departureDate", departureDate)
                    .and("returnDate", returnDate)
                    .and("adults", adultsNum)
                    .and("max", max));

        return flightOffersSearches;
    }

    // Flight Cheapest Date Search
    public FlightDate[] getCheapestDate(String origin, String destination) throws ResponseException {   
        FlightDate[] flightDates = amadeus.shopping.flightDates.get(Params
            .with("origin", "MAD")
            .and("destination", "MUC"));
        
        return flightDates;
    }

    public RankedDestinationPair getRankedFlightDestinations(String origin) throws ResponseException {
        FlightDestination[] flightDestinations = amadeus.shopping.flightDestinations.get(Params
            .with("origin", origin));

        AirTraffic[] mostBookedDestinations = getMostBookedDestinations(origin);
        Set<String> mostBookedDestinationsSet = Arrays.stream(mostBookedDestinations).map(AirTraffic -> AirTraffic.getDestination()).collect(Collectors.toSet());

        List<FlightDestination> mostBookedDestinationsList = new ArrayList<>();
        List<FlightDestination> otherDestinationsList = new ArrayList<>();

        for (FlightDestination flightDestination : flightDestinations) {
            if (mostBookedDestinationsSet.contains(flightDestination.getDestination())) {
                mostBookedDestinationsList.add(flightDestination);
            } else {
                otherDestinationsList.add(flightDestination);
            }
        };
        
        System.out.println(mostBookedDestinationsList);
        System.out.println("-----------------------------------------------");
        System.out.println(otherDestinationsList);
        
        return new RankedDestinationPair(mostBookedDestinationsList, otherDestinationsList);
    }

    public GeoCode getCityGeoCode(String cityCode) throws ResponseException {
        Location[] locations = amadeus.referenceData.locations.get(Params
        .with("keyword", cityCode)
        // .and("countryCode", countryCode)
        .and("subType", "CITY"));
        
        return locations[0].getGeoCode();
     }

     public Address getCityName(String cityCode) throws ResponseException {        
        Location[] locations = amadeus.referenceData.locations.get(Params
        .with("keyword", cityCode)
        .and("subType", "CITY"));

        if (locations.length == 0) {
            return null;
        } 

        return locations[0].getAddress();
     }

     public String getAirportName(String cityCode) throws ResponseException {        
        Location[] locations = amadeus.referenceData.locations.get(Params
        .with("keyword", cityCode)
        .and("subType", "CITY,AIRPORT"));

        if (locations.length == 0) {
            return null;
        } 

        return locations[0].getIataCode();
     }

     public PointOfInterest[] getPointsOfInterest(double latitude, double longtitude) throws ResponseException {
        PointOfInterest[] pointsOfInterest = amadeus.referenceData.locations.pointsOfInterest.get(Params
            .with("latitude", latitude)
            .and("longitude", longtitude));


            return pointsOfInterest;
     }

     
}
