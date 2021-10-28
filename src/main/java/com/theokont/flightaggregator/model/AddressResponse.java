package com.theokont.flightaggregator.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddressResponse {
    
    String cityName;
    String countryCode;
    String status;
}
