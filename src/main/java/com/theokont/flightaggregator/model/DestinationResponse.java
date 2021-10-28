package com.theokont.flightaggregator.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DestinationResponse {
    
    String cityCode;
    String cityName;
    String countryCode;
    String status;
}
