package com.theokont.flightaggregator.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Destination {
    
    String cityCode;
    String cityName;
    String countryCode;

}
