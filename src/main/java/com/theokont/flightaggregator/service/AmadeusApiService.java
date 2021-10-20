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
import com.theokont.flightaggregator.model.Destination;
import com.theokont.flightaggregator.model.RankedDestinationPair;

import org.springframework.stereotype.Service;

@Service
public class AmadeusApiService {
    
    Amadeus amadeus;

    public AmadeusApiService() {
        amadeus = Amadeus
            .builder(System.getenv())
            .build();
    }

    /**
     * Gets the most booked destinations given an origin for a certain time period.
     *
     * @param originCityCode The city code. Maximum length = 3.
     * @param period The time period in YYYY-MM format.
     * @return Returns an array of AirTraffic objects that contains the most booked destinations.
     * @throws ResponseException A custom generic Amadeus error.
     */
    public Destination[] getMostBookedDestinations(String originCityCode, String period) throws ResponseException {
        AirTraffic[] mostBookedDestinations = amadeus.travel.analytics.airTraffic.booked.get(Params
            .with("originCityCode", getAirportName(originCityCode))
            .and("period", period));

        // Amadeus API rate limit in test environment is 100ms, so the method waits a bit before making a new API call
        try {
            Thread.sleep(100);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        Destination[] destinations = Arrays.stream(mostBookedDestinations)
            .map(airTraffic -> {
                String cityName = "";
                String countryCode = "";
                Address address = getAddress(airTraffic.getDestination());
                cityName = (address != null) ? address.getCityName() : "";
                countryCode = (address != null) ? address.getCountryCode() : "";
                
                // Amadeus API rate limit in test environment is 100ms, so the method waits a bit before making a new API call
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

                return new Destination(airTraffic.getDestination(), cityName, countryCode);
            })
            .toArray(Destination[]::new);
        
        return destinations;
    }

    /**
     * Get the most booked destinations given an origin for the current month 4 years ago (Test environment does not offer recent data).
     *
     * @param originCityCode The city code. Maximum length = 3.
     * @return Returns an array of AirTraffic objects that contains the most booked destinations.
     * @throws ResponseException A custom generic Amadeus error.
     */
    public Destination[] getMostBookedDestinations(String originCityCode) throws ResponseException {
        DateTimeFormatter yearMonthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        AirTraffic[] mostBookedDestinations = amadeus.travel.analytics.airTraffic.booked.get(Params
            .with("originCityCode", getAirportName(originCityCode))
            .and("period", LocalDate.now().minusYears(4).format(yearMonthFormatter)));

        // Amadeus API rate limit in test environment is 100ms, so the method waits a bit before making a new API call
        try {
            Thread.sleep(100);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        Destination[] destinations = Arrays.stream(mostBookedDestinations)
            .map(airTraffic -> {
                String cityName = "";
                String countryCode = "";
                Address address = getAddress(airTraffic.getDestination());
                cityName = (address != null) ? address.getCityName() : "";
                countryCode = (address != null) ? address.getCountryCode() : "";
                
                // Amadeus API rate limit in test environment is 100ms, so the method waits a bit before making a new API call
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

                return new Destination(airTraffic.getDestination(), cityName, countryCode);
            })
            .toArray(Destination[]::new);

        return destinations;
    }

    /**
     * Get the cheapest flight recommendations and prices on a given journey.
     *
     * @param origin The origin location code.
     * @param destination The destination location code.
     * @param departureDate The departure date in YYYY-MM-DD format.
     * @param returnDate The return date in YYYY-MM-DD format.
     * @param adultsNum The amount of adult passengers.
     * @param max The max number of results.
     * @return Returns an array of FlightOfferSearch objects that contain all the relevant information about the cheapest flight
     * @throws ResponseException A custom generic Amadeus error.
     */
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

    /**
     * Get all the flight destinations for a given origin grouped in two different lists, a list that contains the
     * most popular flight destinations and a list that contains the rest of them.
     *
     * @param origin The origin city code.
     * @return Returns a RankedDestinationPair object that contains the destination lists.
     * @throws ResponseException A custom generic Amadeus error.
     */
    public RankedDestinationPair getRankedFlightDestinations(String origin) throws ResponseException {
        FlightDestination[] flightDestinations = amadeus.shopping.flightDestinations.get(Params
            .with("origin", origin));

        Destination[] mostBookedDestinations = getMostBookedDestinations(origin);
        Set<String> mostBookedDestinationsSet = Arrays.stream(mostBookedDestinations)
                .map(destination -> destination.getCityCode()).collect(Collectors.toSet());

        List<FlightDestination> mostBookedDestinationsList = new ArrayList<>();
        List<FlightDestination> otherDestinationsList = new ArrayList<>();

        for (FlightDestination flightDestination : flightDestinations) {
            if (mostBookedDestinationsSet.contains(flightDestination.getDestination())) {
                mostBookedDestinationsList.add(flightDestination);
            } else {
                otherDestinationsList.add(flightDestination);
            }
        };

        return new RankedDestinationPair(mostBookedDestinationsList, otherDestinationsList);
    }

    /**
     * Get the geolocation of the given city code.
     *
     * @param cityCode The city code. Max length = 3.
     * @return Returns a GeoCode object that contains the latitude and the longitude of the given city.
     * @throws ResponseException A custom generic Amadeus error.
     */
    public GeoCode getCityGeoCode(String cityCode) throws ResponseException {
        Location[] locations = amadeus.referenceData.locations.get(Params
        .with("keyword", cityCode)
        // .and("countryCode", countryCode)
        .and("subType", "CITY"));
        
        return locations[0].getGeoCode();
     }

    /**
     * Get the address from the given city code.
     *
     * @param cityCode The city code. Max length = 3.
     * @return Returns an Address object that contains the address details of the city.
     * @throws ResponseException A custom generic Amadeus error.
     */
     public Address getAddress(String cityCode) {        
        Location[] locations;
        try {
            locations = amadeus.referenceData.locations.get(Params
            .with("keyword", cityCode)
            .and("subType", "CITY"));
        } catch (ResponseException e) {
            e.printStackTrace();
            return null;
        }

        if (locations.length == 0) {
            return null;
        } 

        return locations[0].getAddress();
     }

    /**
     * Get the airport name from the given city code.
     * @param cityCode The city code. Max length = 3.
     * @return Returns a String that contains the airport code.
     * @throws ResponseException A custom generic Amadeus error.
     */
     public String getAirportName(String cityCode) throws ResponseException {        
        Location[] locations = amadeus.referenceData.locations.get(Params
        .with("keyword", cityCode)
        .and("subType", "CITY,AIRPORT"));

        if (locations.length == 0) {
            return null;
        } 

        return locations[0].getIataCode();
     }

    /**
     * Gets the points of interest of a destination given the latitude and longitude.
     *
     * @param latitude A double that represents the latitude.
     * @param longitude A double that represents the longitude.
     * @return Returns an array of PointOfInterest objects.
     * @throws ResponseException A custom generic Amadeus error.
     */
     public PointOfInterest[] getPointsOfInterest(double latitude, double longitude) throws ResponseException {
        PointOfInterest[] pointsOfInterest = amadeus.referenceData.locations.pointsOfInterest.get(Params
            .with("latitude", latitude)
            .and("longitude", longitude));

            return pointsOfInterest;
     }

}
