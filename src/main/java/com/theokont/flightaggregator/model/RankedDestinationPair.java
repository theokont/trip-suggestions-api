package com.theokont.flightaggregator.model;

import java.util.List;

import com.amadeus.resources.FlightDestination;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RankedDestinationPair {
    
    List<FlightDestination> mostBookedDestinations;
    List<FlightDestination> otherDestinations;

    public RankedDestinationPair(List<FlightDestination> mostBookedDestinations, List<FlightDestination> otherDestinations) {
        this.mostBookedDestinations = mostBookedDestinations;
        this.otherDestinations = otherDestinations;
    }

}
