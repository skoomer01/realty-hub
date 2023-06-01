package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.classes.Address;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccommodationRequest {
    private String name;
    private double price;
    private String startingDate;
    private String streetName;
    private int houseNumber;
    private String postcode;
    private int area;
    private String interior;
    private int rooms;
    private int floors;
    private String image;
}
